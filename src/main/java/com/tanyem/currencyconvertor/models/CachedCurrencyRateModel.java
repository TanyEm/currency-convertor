package com.tanyem.currencyconvertor.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Currency;

@Getter
@Setter
public class CachedCurrencyRateModel {
    private String pair;
    private String date;
    private BigDecimal rate;

    public CachedCurrencyRateModel(Currency sourceCurrency, Currency targetCurrency, String date, BigDecimal rate) {
        this.pair = sourceCurrency.getCurrencyCode() + targetCurrency.getCurrencyCode();
        this.date = date;
        this.rate = rate;
    }
}
