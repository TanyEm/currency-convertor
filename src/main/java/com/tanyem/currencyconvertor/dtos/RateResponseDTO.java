package com.tanyem.currencyconvertor.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class RateResponseDTO {
    private String result;
    private long timestamp;

    public RateResponseDTO(String result, long timestamp) {
        this.result = result;
        this.timestamp = timestamp;
    }
}
