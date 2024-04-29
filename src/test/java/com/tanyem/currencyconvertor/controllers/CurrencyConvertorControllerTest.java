package com.tanyem.currencyconvertor.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class CurrencyConvertorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void indexReturnsWelcomeMessage() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"message\": \"Welcome to the Currency Convertor API!\" }"));
    }

    @Test
    void ratesSuccessfullyReturnsConversion() throws Exception {
        this.mockMvc.perform(
                get("/rates")
                        .param("source_currency", "USD")
                        .param("target_currency", "EUR")
                        .param("monetary_value", "100")
                        .header("Accept-Language", "en-US")
                )
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("$100.00"));
    }

    @Test
    void ratesDoesNotReturnConversionWithEmptyValues() throws Exception {
        mockMvc.perform(
                get("/rates")
                        .param("source_currency", "")
                        .param("target_currency", "")
                        .param("monetary_value", "")
                        .header("Accept-Language", "en-US")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.errors.source_currency").isArray())
                .andExpect(jsonPath("$.errors.source_currency[0]").value("Source currency and target currency must be different"))
                .andExpect(jsonPath("$.errors.source_currency[1]").value("Source currency cannot be empty"))
                .andExpect(jsonPath("$.errors.source_currency[2]").value("Source currency must be 3 characters"))
                .andExpect(jsonPath("$.errors.target_currency").isArray())
                .andExpect(jsonPath("$.errors.target_currency[0]").value("Source currency and target currency must be different"))
                .andExpect(jsonPath("$.errors.target_currency[1]").value("Target currency cannot be empty"))
                .andExpect(jsonPath("$.errors.target_currency[2]").value("Target currency must be 3 characters"))
                .andExpect(jsonPath("$.errors.monetary_value").isArray())
                .andExpect(jsonPath("$.errors.monetary_value[0]").value("Monetary value cannot be empty"))
                .andExpect(jsonPath("$.errors.monetary_value[1]").value("Monetary value must be a positive number in minor units (e.g. 1_000_000 for $1)"));
    }

    @Test
    void ratesDoesNotReturnConversionWithInvalidCurrency() throws Exception {
        mockMvc.perform(
                        get("/rates")
                                .param("source_currency", "USD")
                                .param("target_currency", "EURR")
                                .param("monetary_value", "100")
                                .header("Accept-Language", "en-US")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.errors.target_currency").isArray())
                .andExpect(jsonPath("$.errors.target_currency[0]").value("Target currency must be 3 characters"))
                .andExpect(jsonPath("$.errors.target_currency[1]").value("Unexpected currency code"));
    }

    @Test
    void ratesDoesNotReturnConversionWithEqualCurrencies() throws Exception {
        mockMvc.perform(
                        get("/rates")
                                .param("source_currency", "USD")
                                .param("target_currency", "USD")
                                .param("monetary_value", "100")
                                .header("Accept-Language", "en-US")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.errors.source_currency").isArray())
                .andExpect(jsonPath("$.errors.source_currency[0]").value("Source currency and target currency must be different"))
                .andExpect(jsonPath("$.errors.target_currency").isArray())
                .andExpect(jsonPath("$.errors.target_currency[0]").value("Source currency and target currency must be different"));
    }

    @Test
    void ratesDoesNotReturnConversionWithNegativeMonetaryValue() throws Exception {
        mockMvc.perform(
                        get("/rates")
                                .param("source_currency", "USD")
                                .param("target_currency", "EUR")
                                .param("monetary_value", "-100")
                                .header("Accept-Language", "en-US")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.errors.monetary_value").isArray())
                .andExpect(jsonPath("$.errors.monetary_value[0]").value("Monetary value must be a positive number in minor units (e.g. 1_000_000 for $1)"));
    }

    @Test
    void ratesDoesNotReturnConversionWithZeroMonetaryValue() throws Exception {
        mockMvc.perform(
                        get("/rates")
                                .param("source_currency", "USD")
                                .param("target_currency", "EUR")
                                .param("monetary_value", "0")
                                .header("Accept-Language", "en-US")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.errors.monetary_value").isArray())
                .andExpect(jsonPath("$.errors.monetary_value[0]").value("Monetary value must be a positive number in minor units (e.g. 1_000_000 for $1)"));
    }

    @Test
    void ratesDoesNotReturnConversionWithNaNMonetaryValue() throws Exception {
        mockMvc.perform(
                        get("/rates")
                                .param("source_currency", "USD")
                                .param("target_currency", "EUR")
                                .param("monetary_value", "o")
                                .header("Accept-Language", "en-US")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.errors.monetary_value").isArray())
                .andExpect(jsonPath("$.errors.monetary_value[0]").value("Monetary value must be a positive number in minor units (e.g. 1_000_000 for $1)"));
    }
}