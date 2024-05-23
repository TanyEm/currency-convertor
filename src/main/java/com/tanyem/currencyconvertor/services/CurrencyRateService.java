package com.tanyem.currencyconvertor.services;

import com.tanyem.currencyconvertor.models.MonetaryUnit;
import com.tanyem.currencyconvertor.models.RateModel;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Locale;

@Service
public class CurrencyRateService {

    private final CurrencyExchangeService currencyExchangeService;

    public CurrencyRateService(CurrencyExchangeService currencyExchangeService) {
        this.currencyExchangeService = currencyExchangeService;
    }

    public MonetaryUnit convert(MonetaryUnit monetaryUnit, Currency targetCurrency) {
        RateModel rateModel = currencyExchangeService.getRate(monetaryUnit.getCurrency(), targetCurrency);
        String currentDate = DateTimeFormatter
                .ofPattern("yyyy-MM-dd", Locale.ENGLISH)
                .format(LocalDateTime.now());
        if (!rateModel.getDate().equals(currentDate)) {
            currencyExchangeService.clearCache(monetaryUnit.getCurrency(), targetCurrency);
            rateModel = currencyExchangeService.getRate(monetaryUnit.getCurrency(), targetCurrency);
        }
        BigDecimal result = monetaryUnit.getMonitoryValue().multiply(rateModel.getRate());
        return new MonetaryUnit(targetCurrency, result);
    }
}
