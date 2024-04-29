package com.tanyem.currencyconvertor.services;

import com.tanyem.currencyconvertor.dtos.SwopRateDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class SwopServiceTest {

    @MockBean
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Test
    void getRates() {
        SwopRateDTO rateEUR = new SwopRateDTO("USD", "EUR", 0.85, "2024-04-01");
        SwopRateDTO rateAUD = new SwopRateDTO("USD", "AUD", 1.63, "2024-04-01");
        List<SwopRateDTO> rates = Arrays.asList(rateEUR, rateAUD);
        Flux<SwopRateDTO> flux = Flux.fromIterable(rates);

        when(webClientBuilder.baseUrl("https://swop.cx")).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/rest/rates")).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.header("Authorization", "ApiKey test-api-key")).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(SwopRateDTO.class)).thenReturn(flux);

        SwopService swopService = new SwopService(webClientBuilder,"test-api-key");
        List<SwopRateDTO> actualRates = swopService.getRates();
        assertEquals(rates, actualRates);
    }

    @Test
    void getRatesWhenAPIKeysNotProvided() {
        when(webClientBuilder.baseUrl("https://swop.cx")).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/rest/rates")).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.header("Authorization", "ApiKey wrong-test-api-key")).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(SwopRateDTO.class)).thenReturn(Flux.error(new WebClientResponseException(401, "Unauthorized", null, null, null)));

        SwopService swopService = new SwopService(webClientBuilder,"wrong-test-api-key");

        WebClientResponseException exception = assertThrows(WebClientResponseException.class, swopService::getRates);
        assertEquals(401, exception.getRawStatusCode());
        assertEquals("401 Unauthorized", exception.getMessage());
    }
}