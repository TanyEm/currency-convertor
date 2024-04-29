package com.tanyem.currencyconvertor.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class SwopRateDTO {
    public String base_currency;
    public String quote_currency;
    public BigDecimal quote;
    public String date;

    public SwopRateDTO(String base_currency, String quote_currency, BigDecimal quote, String date) {
        this.base_currency = base_currency;
        this.quote_currency = quote_currency;
        this.quote = quote;
        this.date = date;
    }
}
