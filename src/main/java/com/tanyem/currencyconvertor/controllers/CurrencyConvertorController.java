package com.tanyem.currencyconvertor.controllers;

import com.tanyem.currencyconvertor.dtos.RateRequestDTO;
import com.tanyem.currencyconvertor.models.RateResponseModel;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class CurrencyConvertorController {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyConvertorController.class);

    public CurrencyConvertorController() {
        logger.info("CurrencyConvertorController created!");
    }

    @GetMapping(path = "/", produces = "application/json")
    public String index() {
        return "{ \"message\": \"Welcome to the Currency Convertor API!\" }";
    }

    @GetMapping(path = "/rates", produces = "application/json")
    public RateResponseModel rates(@Valid RateRequestDTO rateRequestDTO) {
        logger.info(
                "Convert: {} to {} with value: {}",
                rateRequestDTO.source_currency,
                rateRequestDTO.target_currency,
                rateRequestDTO.monetary_value);
        return new RateResponseModel(rateRequestDTO.monetary_value, System.currentTimeMillis());
    }
}
