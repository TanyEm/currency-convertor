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

    private final SwopService swopService;

    public CurrencyRateService(SwopService swopService) {
        this.swopService = swopService;
    }

    public MonetaryUnit convert(MonetaryUnit monetaryUnit, Currency targetCurrency) {
        RateModel rateModel = swopService.getRate(monetaryUnit.getCurrency(), targetCurrency);
        String currentDate = DateTimeFormatter
                .ofPattern("yyyy-MM-dd", Locale.ENGLISH)
                .format(LocalDateTime.now());
        if (!rateModel.getDate().equals(currentDate)) {
            swopService.clearCache(monetaryUnit.getCurrency(), targetCurrency);
            rateModel = swopService.getRate(monetaryUnit.getCurrency(), targetCurrency);
        }
        BigDecimal result = monetaryUnit.getMonitoryValue().multiply(rateModel.getRate());
        return new MonetaryUnit(targetCurrency, result);
    }
}
