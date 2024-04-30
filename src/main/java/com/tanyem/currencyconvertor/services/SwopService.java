package com.tanyem.currencyconvertor.services;

import com.tanyem.currencyconvertor.dtos.SwopRateDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

@Service
public class SwopService {

    private final WebClient webClient;

    public SwopService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://swop.cx").build();
    }

    public BigDecimal getRate(Currency sourceCurrency, Currency targetCurrency) {
        List<SwopRateDTO> rates = getRates();
        for (SwopRateDTO rate : rates) {
            if (rate.getBase_currency().equals(sourceCurrency.getCurrencyCode()) && rate.getQuote_currency().equals(targetCurrency.getCurrencyCode())) {
                return rate.getQuote();
            }
        }
        return null;
    }

    public List<SwopRateDTO> getRates() throws WebClientResponseException {
        Flux<SwopRateDTO> responseFlux = webClient.get()
                .uri("/rest/rates")
                .retrieve()
                .bodyToFlux(SwopRateDTO.class);

        return responseFlux.collectList().block();
    }
}
