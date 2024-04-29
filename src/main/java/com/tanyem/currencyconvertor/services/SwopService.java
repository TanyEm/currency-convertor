package com.tanyem.currencyconvertor.services;

import com.tanyem.currencyconvertor.dtos.SwopRateDTO;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class SwopService {

    private final WebClient webClient;
    private final String apiKey;

    public SwopService(WebClient.Builder webClientBuilder, String apiKey) {
        this.apiKey = apiKey;
        this.webClient = webClientBuilder.baseUrl("https://swop.cx").build();
    }

    public List<SwopRateDTO> getRates() throws WebClientResponseException {
        Flux<SwopRateDTO> responseFlux = webClient.get()
                .uri("/rest/rates")
                .header("Authorization", ("ApiKey " + apiKey))
                .retrieve()
                .bodyToFlux(SwopRateDTO.class);

        return responseFlux.collectList().block();
    }
}
