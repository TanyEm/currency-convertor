package com.tanyem.currencyconvertor.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Currency;

@Getter
@Setter
@AllArgsConstructor
public class MonetaryUnit {
    private Currency currency;
    private BigDecimal monitoryValue;
}
