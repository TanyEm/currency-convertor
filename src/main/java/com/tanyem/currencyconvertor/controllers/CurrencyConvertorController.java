package com.tanyem.currencyconvertor.controllers;

import com.tanyem.currencyconvertor.models.RateResponseModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyConvertorController {

    public CurrencyConvertorController() {
        System.out.println("CurrencyConvertorController created!");
    }

    @RequestMapping(path = "/", produces = "application/json")
    public String index() {
        return "{ \"message\": \"Welcome to the Currency Convertor API!\" }";
    }

    @RequestMapping(path = "/rates", produces = "application/json")
    public RateResponseModel rates() {
        return new RateResponseModel(21, 1620000000);
    }
}
