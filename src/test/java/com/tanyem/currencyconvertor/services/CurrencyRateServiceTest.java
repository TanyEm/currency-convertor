package com.tanyem.currencyconvertor.services;

import com.tanyem.currencyconvertor.models.MonetaryUnit;
import com.tanyem.currencyconvertor.models.RateModel;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
class CurrencyRateServiceTest {

    @Mock
    private SwopService SwopService;

    @Test
    void convertCurrencySuccessfully() {
        MonetaryUnit monetaryUnit = new MonetaryUnit(Currency.getInstance("USD"), new BigDecimal("100"));
        Currency targetCurrency = Currency.getInstance("EUR");
        String currentDate = DateTimeFormatter
                .ofPattern("yyyy-MM-dd", Locale.ENGLISH)
                .format(LocalDateTime.now());
        when(SwopService.getRate(monetaryUnit.getCurrency(), targetCurrency)).thenReturn(new RateModel(new BigDecimal("0.85"), currentDate));

        MonetaryUnit expectedMonetaryUnit = new MonetaryUnit(Currency.getInstance("EUR"), new BigDecimal("85"));
        CurrencyRateService currencyRateService = new CurrencyRateService(SwopService);
        MonetaryUnit actualMonetaryUnit = currencyRateService.convert(monetaryUnit, targetCurrency);
        assertEquals(expectedMonetaryUnit.getCurrency().getCurrencyCode(), actualMonetaryUnit.getCurrency().getCurrencyCode());
        assertTrue(expectedMonetaryUnit.getMonitoryValue().compareTo(actualMonetaryUnit.getMonitoryValue())== 0);
    }

    @Test
    void convertCurrencySuccessfullyWhenCacheExpired() {
        MonetaryUnit monetaryUnit = new MonetaryUnit(Currency.getInstance("USD"), new BigDecimal("100"));
        Currency targetCurrency = Currency.getInstance("EUR");
        String oldDate = "2024-04-01";
        String currentDate = DateTimeFormatter
                .ofPattern("yyyy-MM-dd", Locale.ENGLISH)
                .format(LocalDateTime.now());
        when(SwopService.getRate(monetaryUnit.getCurrency(), targetCurrency)).thenReturn(new RateModel(new BigDecimal("0.71"), oldDate));
        doNothing().when(SwopService).clearCache(monetaryUnit.getCurrency(), targetCurrency);
        when(SwopService.getRate(monetaryUnit.getCurrency(), targetCurrency)).thenReturn(new RateModel(new BigDecimal("0.85"), currentDate));

        MonetaryUnit expectedMonetaryUnit = new MonetaryUnit(Currency.getInstance("EUR"), new BigDecimal("85"));
        CurrencyRateService currencyRateService = new CurrencyRateService(SwopService);
        MonetaryUnit actualMonetaryUnit = currencyRateService.convert(monetaryUnit, targetCurrency);
        assertEquals(expectedMonetaryUnit.getCurrency().getCurrencyCode(), actualMonetaryUnit.getCurrency().getCurrencyCode());
        assertTrue(expectedMonetaryUnit.getMonitoryValue().compareTo(actualMonetaryUnit.getMonitoryValue())== 0);
    }
}