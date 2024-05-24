package com.tanyem.currencyconvertor.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class RateResponseDTO {
    private String result;
    private long timestamp;
}
