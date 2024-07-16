package com.tanyem.currencyconvertor.services;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.tanyem.currencyconvertor.configs.InfluxDBConfig;
import com.tanyem.currencyconvertor.dtos.SwopRateDTO;
import com.tanyem.currencyconvertor.exceptions.CurrencyPairNotSupportedException;
import com.tanyem.currencyconvertor.exceptions.SwopAPINotAvailableException;
import com.tanyem.currencyconvertor.models.RateModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.*;

@Service
public class SwopServiceCurrency implements CurrencyExchangeService {

    private static final Logger logger = LoggerFactory.getLogger(SwopServiceCurrency.class);

    private final WebClient webClient;

    private InfluxDBClient influxDBClient;

    @Autowired
    private InfluxDBConfig influxDBConfig;

    public SwopServiceCurrency(InfluxDBClient influxDBClient, WebClient.Builder webClientBuilder) {
        this.influxDBClient = influxDBClient;
        this.webClient = webClientBuilder.baseUrl("https://swop.cx").build();
    }

    @CacheEvict(value = "rates", key = "#sourceCurrency.currencyCode + #targetCurrency.currencyCode")
    public void clearCache(Currency sourceCurrency, Currency targetCurrency) {
        logger.info("Clearing cache for {}{}", sourceCurrency.getCurrencyCode(), targetCurrency.getCurrencyCode());
    }

    @Cacheable(value = "rates", key = "#sourceCurrency.currencyCode + #targetCurrency.currencyCode")
    public RateModel getRate(Currency sourceCurrency, Currency targetCurrency) {
        Map<String, RateModel> rates = getRates();
        RateModel found = rates.get(sourceCurrency.getCurrencyCode() + targetCurrency.getCurrencyCode());
        if (found == null) {
            throw new CurrencyPairNotSupportedException("Requested currency pair "
                    + sourceCurrency.getCurrencyCode()
                    + " to "
                    + targetCurrency.getCurrencyCode() + " is not supported");
        }
        return found;
    }

    Map<String, RateModel> getRates() {
        Map<String, RateModel> ratesMap;
        getInfluxDBWriteApi();
        try {
            ratesMap = fetchRatesFromSwopAPI();
            logSwopApiCallToInfluxDB(true);
        } catch (SwopAPINotAvailableException e) {
            logSwopApiCallToInfluxDB(false);
            throw e;
        }

        return ratesMap;
    }

    private void initializeInfluxDBClient() {
        try {
            this.influxDBClient = InfluxDBClientFactory.create(
                    influxDBConfig.getUrl(),
                    influxDBConfig.getToken().toCharArray(),
                    influxDBConfig.getOrg(),
                    influxDBConfig.getBucket()
            );
        } catch (Exception e) {
            logger.error("InfluxDB is not available: {}", e.getMessage());
            this.influxDBClient = null;
        }
    }

    private Map<String, RateModel> fetchRatesFromSwopAPI() {
        try {
            logger.info("Fetching rates from Swop API");
            Flux<SwopRateDTO> responseFlux = webClient.get()
                    .uri("/rest/rates")
                    .retrieve()
                    .bodyToFlux(SwopRateDTO.class);

            List<SwopRateDTO> rates = responseFlux.collectList().block();
            Map<String, RateModel> ratesMap = new HashMap<>();
            for (SwopRateDTO rate : rates) {
                RateModel rateModel = new RateModel(rate.getQuote(), rate.getDate());
                ratesMap.put(rate.getBaseCurrency() + rate.getQuoteCurrency(), rateModel);
            }
            return ratesMap;
        } catch (WebClientRequestException e) {
            logger.error("Swop API can not be reached: {}", e.getMessage());
            throw new SwopAPINotAvailableException("Swop API is not available. Please try again later.");
        } catch (WebClientResponseException e) {
            logger.error("Swop API call returned an error: {}", e.getMessage());
            throw new SwopAPINotAvailableException("Swop API is not available. Please try again later.");
        }

    }

    private Optional<WriteApiBlocking> getInfluxDBWriteApi() {
        initializeInfluxDBClient();
        if (this.influxDBClient == null) {
            logger.error("InfluxDBClient is not initialized.");
            return Optional.empty();
        }
        try {
            return Optional.of(this.influxDBClient.getWriteApiBlocking());
        } catch (Exception e) {
            logger.error("Failed to create WriteApiBlocking: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private void logSwopApiCallToInfluxDB(boolean success) {
        try {
            Point point = Point.measurement("swop_api_call")
                    .addField("success", success)
                    .time(Instant.now(), WritePrecision.MS);
            this.influxDBClient.getWriteApiBlocking().writePoint(point);
        } catch (Exception e) {
            logger.error("Failed to write point to InfluxDB: {}", e.getMessage());
        }
    }
}
