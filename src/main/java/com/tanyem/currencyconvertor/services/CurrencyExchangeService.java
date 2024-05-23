package com.tanyem.currencyconvertor.services;

import com.tanyem.currencyconvertor.models.RateModel;

import java.util.Currency;

public interface CurrencyExchangeService {
    RateModel getRate(Currency sourceCurrency, Currency targetCurrency);
    void clearCache(Currency sourceCurrency, Currency targetCurrency);
}
