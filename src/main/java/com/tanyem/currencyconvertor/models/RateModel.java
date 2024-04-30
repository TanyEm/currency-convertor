package com.tanyem.currencyconvertor.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class RateModel {
    public BigDecimal rate;
    public String date;
}
