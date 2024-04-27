package com.tanyem.currencyconvertor.controllers;

import com.tanyem.currencyconvertor.models.RateResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyConvertorController {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyConvertorController.class);

    public CurrencyConvertorController() {
        logger.info("CurrencyConvertorController created!");
    }

    @RequestMapping(path = "/", produces = "application/json")
    public String index() {
        return "{ \"message\": \"Welcome to the Currency Convertor API!\" }";
    }

    @RequestMapping(path = "/rates", produces = "application/json")
    public RateResponseModel rates(
            @RequestParam String source_currency,
            @RequestParam String target_currency,
            @RequestParam long monetary_value
    ) {
        logger.info("Convert: {} to {} with value: {}", source_currency, target_currency, monetary_value);
        return new RateResponseModel(monetary_value, System.currentTimeMillis());
    }
}
