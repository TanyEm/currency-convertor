package com.tanyem.currencyconvertor.controllers;

import com.influxdb.client.InfluxDBClient;
import com.tanyem.currencyconvertor.dtos.RateRequestDTO;
import com.tanyem.currencyconvertor.dtos.RateResponseDTO;
import com.tanyem.currencyconvertor.exceptions.CurrencyPairNotSupportedException;
import com.tanyem.currencyconvertor.exceptions.SwopAPINotAvailableException;
import com.tanyem.currencyconvertor.models.MonetaryUnit;
import com.tanyem.currencyconvertor.services.CurrencyRateService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

@RestController
public class CurrencyConvertorController {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyConvertorController.class);

    private final LocaleResolver localeResolver = new AcceptHeaderLocaleResolver();

    private final CurrencyRateService currencyRateService;

    private final InfluxDBClient influxDBClient;

    @Autowired
    public CurrencyConvertorController(CurrencyRateService currencyRateService, InfluxDBClient influxDBClient) {
        this.currencyRateService = currencyRateService;
        this.influxDBClient = influxDBClient;
        logger.info("CurrencyConvertorController created!");
    }

    @GetMapping(path = "/", produces = "application/json")
    public String index() {
        return "{ \"message\": \"Welcome to the Currency Convertor API!\" }";
    }

    @GetMapping(path = "/rates", produces = "application/json")
    public ResponseEntity<?> rates(HttpServletRequest request, @Valid RateRequestDTO rateRequestDTO) {
        logger.info(
                "Convert: {} to {} with value: {}",
                rateRequestDTO.source_currency,
                rateRequestDTO.target_currency,
                rateRequestDTO.monetary_value);

        MonetaryUnit monetaryUnit = new MonetaryUnit(
                Currency.getInstance(rateRequestDTO.source_currency),
                new BigDecimal(rateRequestDTO.monetary_value)
        );
        Currency targetCurrency = Currency.getInstance(rateRequestDTO.target_currency);

        MonetaryUnit targetMonetaryUnit;
        try {
            targetMonetaryUnit = currencyRateService.convert(monetaryUnit, targetCurrency);
        } catch (CurrencyPairNotSupportedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (SwopAPINotAvailableException e) {
            return ResponseEntity.status(502).body(e.getMessage());
        }

        Locale locale = localeResolver.resolveLocale(request);
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        nf.setCurrency(targetMonetaryUnit.getCurrency());
        RateResponseDTO rateResponseDTO = new RateResponseDTO(nf.format(targetMonetaryUnit.getMonitoryValue()), System.currentTimeMillis());

        return ResponseEntity.ok(rateResponseDTO);
    }
}
