package com.tanyem.currencyconvertor.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Currency;

@Getter
@Setter
public class MonetaryUnit {
    private Currency currency;
    private BigDecimal monitoryValue;

    public MonetaryUnit(Currency currency, BigDecimal monitoryValue) {
        this.currency = currency;
        this.monitoryValue = monitoryValue;
    }
}
