package com.tanyem.currencyconvertor.services;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.write.Point;
import com.tanyem.currencyconvertor.dtos.SwopRateDTO;
import com.tanyem.currencyconvertor.exceptions.CurrencyPairNotSupportedException;
import com.tanyem.currencyconvertor.exceptions.SwopAPINotAvailableException;
import com.tanyem.currencyconvertor.models.RateModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
class SwopServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private InfluxDBClient influxDBClient;

    @Mock
    private WriteApiBlocking writeApiBlocking;

    @BeforeEach
    void setUp() {
        when(webClientBuilder.baseUrl("https://swop.cx")).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(influxDBClient.getWriteApiBlocking()).thenReturn(writeApiBlocking);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/rest/rates")).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void getRateSuccess() {
        SwopRateDTO rateEUR = new SwopRateDTO("USD", "EUR", new BigDecimal("0.85"), "2024-04-01");
        List<SwopRateDTO> ratesList = Arrays.asList(rateEUR);

        Currency sourceCurrency = Currency.getInstance("USD");
        Currency targetCurrency = Currency.getInstance("EUR");
        RateModel rateModel = new RateModel(new BigDecimal("0.85"), "2024-04-01");

        Flux<SwopRateDTO> flux = Flux.fromIterable(ratesList);
        when(responseSpec.bodyToFlux(SwopRateDTO.class)).thenReturn(flux);
        SwopServiceCurrency swopService = new SwopServiceCurrency(influxDBClient, webClientBuilder);

        RateModel actualRate = swopService.getRate(sourceCurrency, targetCurrency);
        assertEquals(rateModel.getRate(), actualRate.getRate());
        assertEquals(rateModel.getDate(), actualRate.getDate());
    }

    @Test
    void getRateWhenCurrencyPairNotSupported() {
        SwopRateDTO rateEUR = new SwopRateDTO("USD", "EUR", new BigDecimal("0.85"), "2024-04-01");
        List<SwopRateDTO> ratesList = Arrays.asList(rateEUR);

        Currency sourceCurrency = Currency.getInstance("USD");
        Currency targetCurrency = Currency.getInstance("AUD");

        Flux<SwopRateDTO> flux = Flux.fromIterable(ratesList);
        when(responseSpec.bodyToFlux(SwopRateDTO.class)).thenReturn(flux);
        SwopServiceCurrency swopService = new SwopServiceCurrency(influxDBClient, webClientBuilder);

        CurrencyPairNotSupportedException exception = assertThrows(CurrencyPairNotSupportedException.class, () -> swopService.getRate(sourceCurrency, targetCurrency));
        String expectedMessage = "{\"errors\":{\"source_currency\":[\"Requested currency pair USD to AUD is not supported\"],\"target_currency\":[\"Requested currency pair USD to AUD is not supported\"]}}";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void getRates() {
        SwopRateDTO rateEUR = new SwopRateDTO("USD", "EUR", new BigDecimal("0.85"), "2024-04-01");
        SwopRateDTO rateAUD = new SwopRateDTO("USD", "AUD", new BigDecimal("1.63"), "2024-04-01");
        List<SwopRateDTO> ratesList = Arrays.asList(rateEUR, rateAUD);
        Map<String, RateModel> rates = Map.of(
                "USDEUR", new RateModel(new BigDecimal("0.85"), "2024-04-01"),
                "USDAUD", new RateModel(new BigDecimal("1.63"), "2024-04-01")
        );
        Flux<SwopRateDTO> flux = Flux.fromIterable(ratesList);

        when(responseSpec.bodyToFlux(SwopRateDTO.class)).thenReturn(flux);
        doNothing().when(writeApiBlocking).writePoint(any(Point.class));

        SwopServiceCurrency swopService = new SwopServiceCurrency(influxDBClient, webClientBuilder);
        Map<String, RateModel> actualRates = swopService.getRates();
        assertEquals(rates.get("USDEUR").getRate(), actualRates.get("USDEUR").getRate());
        assertEquals(rates.get("USDEUR").getDate(), actualRates.get("USDEUR").getDate());
        assertEquals(rates.get("USDAUD").getRate(), actualRates.get("USDAUD").getRate());
        assertEquals(rates.get("USDAUD").getDate(), actualRates.get("USDAUD").getDate());
    }

    @Test
    void getRatesWhenAPIKeysNotProvided() {
        when(responseSpec.bodyToFlux(SwopRateDTO.class)).thenReturn(Flux.error(new WebClientResponseException(401, "Unauthorized", null, null, null)));
        doNothing().when(writeApiBlocking).writePoint(any(Point.class));

        SwopServiceCurrency swopService = new SwopServiceCurrency(influxDBClient, webClientBuilder);

        SwopAPINotAvailableException exception = assertThrows(SwopAPINotAvailableException.class, swopService::getRates);
        assertEquals("{\"errors\":{\"bad_gateway\":[\"Swop API is not available. Please try again later.\"]}}", exception.getMessage());
    }
}