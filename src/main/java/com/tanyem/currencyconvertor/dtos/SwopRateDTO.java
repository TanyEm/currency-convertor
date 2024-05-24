package com.tanyem.currencyconvertor.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SwopRateDTO {
    @JsonProperty("base_currency")
    public String baseCurrency;
    @JsonProperty("quote_currency")
    public String quoteCurrency;
    public BigDecimal quote;
    public String date;
}
