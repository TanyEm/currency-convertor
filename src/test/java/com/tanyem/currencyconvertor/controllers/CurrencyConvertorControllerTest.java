package com.tanyem.currencyconvertor.controllers;

import com.tanyem.currencyconvertor.models.RateResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
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
                )
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(100));
    }

    @Test
    void ratesDoesNotReturnConversionWithEmptyValues() throws Exception {
        this.mockMvc.perform(get("/rates"))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isBadRequest());
    }
}