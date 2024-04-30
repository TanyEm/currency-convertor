package com.tanyem.currencyconvertor.services;

import com.tanyem.currencyconvertor.dtos.RateRequestDTO;
import com.tanyem.currencyconvertor.dtos.SwopRateDTO;
import com.tanyem.currencyconvertor.models.MonetaryUnit;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class CurrencyRateServiceTest {

    @Mock
    private SwopService SwopService;

    @Test
    void convertCurrencySuccessfully() {
        MonetaryUnit monetaryUnit = new MonetaryUnit(Currency.getInstance("USD"), new BigDecimal("100"));
        Currency targetCurrency = Currency.getInstance("EUR");
        when(SwopService.getRate(monetaryUnit.getCurrency(), targetCurrency)).thenReturn(new BigDecimal("0.85"));

        MonetaryUnit expectedMonetaryUnit = new MonetaryUnit(Currency.getInstance("EUR"), new BigDecimal("85"));
        CurrencyRateService currencyRateService = new CurrencyRateService(SwopService);
        MonetaryUnit actualMonetaryUnit = currencyRateService.convert(monetaryUnit, targetCurrency);
        assertEquals(expectedMonetaryUnit.getCurrency().getCurrencyCode(), actualMonetaryUnit.getCurrency().getCurrencyCode());
        assertTrue(expectedMonetaryUnit.getMonitoryValue().compareTo(actualMonetaryUnit.getMonitoryValue())== 0);
    }
}