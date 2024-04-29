package com.tanyem.currencyconvertor.controllers;

import com.tanyem.currencyconvertor.dtos.RateRequestDTO;
import com.tanyem.currencyconvertor.dtos.RateResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@RestController
public class CurrencyConvertorController {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyConvertorController.class);
    private final LocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
    private final View error;

    public CurrencyConvertorController(View error) {
        logger.info("CurrencyConvertorController created!");
        this.error = error;
    }

    @GetMapping(path = "/", produces = "application/json")
    public String index() {
        return "{ \"message\": \"Welcome to the Currency Convertor API!\" }";
    }

    @GetMapping(path = "/rates", produces = "application/json")
    public RateResponseDTO rates(HttpServletRequest request, @Valid RateRequestDTO rateRequestDTO) {
        logger.info(
                "Convert: {} to {} with value: {}",
                rateRequestDTO.source_currency,
                rateRequestDTO.target_currency,
                rateRequestDTO.monetary_value);

        Locale locale = localeResolver.resolveLocale(request);
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

        return new RateResponseDTO(nf.format(new BigDecimal(rateRequestDTO.monetary_value)), System.currentTimeMillis());
    }
}
