package com.tanyem.currencyconvertor.services;

import com.tanyem.currencyconvertor.dtos.SwopRateDTO;
import com.tanyem.currencyconvertor.models.RateModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import java.util.*;

@Service
public class SwopService {

    private static final Logger logger = LoggerFactory.getLogger(SwopService.class);

    private final WebClient webClient;

    public SwopService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://swop.cx").build();
    }

    @CacheEvict(value = "rates", key = "#sourceCurrency.currencyCode + #targetCurrency.currencyCode")
    public void clearCache(Currency sourceCurrency, Currency targetCurrency) {
        logger.info("Clearing cache for {}{}", sourceCurrency.getCurrencyCode(), targetCurrency.getCurrencyCode());
    }

    @Cacheable(value = "rates", key = "#sourceCurrency.currencyCode + #targetCurrency.currencyCode")
    public RateModel getRate(Currency sourceCurrency, Currency targetCurrency) {
        try {
            Map<String, RateModel> rates = getRates();
            return rates.get(sourceCurrency.getCurrencyCode() + targetCurrency.getCurrencyCode());
        } catch (WebClientResponseException e) {
            return null;
        }
    }

    Map<String, RateModel> getRates() throws WebClientResponseException {
        logger.info("Fetching rates from Swop API");
        Flux<SwopRateDTO> responseFlux = webClient.get()
                .uri("/rest/rates")
                .retrieve()
                .bodyToFlux(SwopRateDTO.class);

        List<SwopRateDTO> rates = responseFlux.collectList().block();
        Map<String, RateModel> ratesMap = new HashMap<String, RateModel>();

        for (SwopRateDTO rate : rates) {
            RateModel rateModel = new RateModel(rate.getQuote(), rate.getDate());
            ratesMap.put((rate.getBase_currency() + rate.getQuote_currency()), rateModel);
        }

        return ratesMap;
    }
}
