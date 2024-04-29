package com.tanyem.currencyconvertor.services;

public class CurrencyRateService {

    private final SwopService swopService;

    public CurrencyRateService(SwopService swopService) {
        this.swopService = swopService;
    }
}
