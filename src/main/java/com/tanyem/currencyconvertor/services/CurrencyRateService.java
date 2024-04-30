package com.tanyem.currencyconvertor.services;

import com.tanyem.currencyconvertor.models.MonetaryUnit;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;

@Service
public class CurrencyRateService {

    private final SwopService swopService;

    public CurrencyRateService(SwopService swopService) {
        this.swopService = swopService;
    }

    public MonetaryUnit convert(MonetaryUnit monetaryUnit, Currency targetCurrency) {
        BigDecimal rate = swopService.getRate(monetaryUnit.getCurrency(), targetCurrency);
        BigDecimal result = monetaryUnit.getMonitoryValue().multiply(rate);
        return new MonetaryUnit(targetCurrency, result);
    }
}
