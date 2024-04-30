package com.tanyem.currencyconvertor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CurrencyConvertorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyConvertorApplication.class, args);
    }
}
