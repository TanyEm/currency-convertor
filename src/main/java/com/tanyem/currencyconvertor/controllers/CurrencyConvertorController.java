package com.tanyem.currencyconvertor.controllers;

import com.influxdb.client.InfluxDBClient;
import com.tanyem.currencyconvertor.configs.HttpExceptionHandler;
import com.tanyem.currencyconvertor.dtos.RateRequestDTO;
import com.tanyem.currencyconvertor.dtos.RateResponseDTO;
import com.tanyem.currencyconvertor.exceptions.CurrencyPairNotSupportedException;
import com.tanyem.currencyconvertor.exceptions.SwopAPINotAvailableException;
import com.tanyem.currencyconvertor.models.MonetaryUnit;
import com.tanyem.currencyconvertor.services.CurrencyRateService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

@RestController
public class CurrencyConvertorController {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyConvertorController.class);

    private final LocaleResolver localeResolver = new AcceptHeaderLocaleResolver();

    private final CurrencyRateService currencyRateService;

    @Autowired
    private LocalValidatorFactoryBean validator;

    @Autowired
    public CurrencyConvertorController(CurrencyRateService currencyRateService, InfluxDBClient influxDBClient) {
        this.currencyRateService = currencyRateService;
        logger.info("CurrencyConvertorController created!");
    }

    @GetMapping(path = "/rates", produces = "application/json")
    public ResponseEntity<?> rates(HttpServletRequest request,
                                   @RequestParam("source_currency") String sourceCurrency,
                                   @RequestParam("target_currency") String targetCurrency,
                                   @RequestParam("monetary_value") String monetaryValue) {
        RateRequestDTO rateRequestDTO = RateRequestDTO.builder()
                .sourceCurrency(sourceCurrency)
                .targetCurrency(targetCurrency)
                .monetaryValue(monetaryValue)
                .build();

        BindingResult bindingResult = new BeanPropertyBindingResult(rateRequestDTO, "rateRequestDTO");
        validator.validate(rateRequestDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
            HttpExceptionHandler exceptionHandler = new HttpExceptionHandler();
            return exceptionHandler.handleValidationErrors(ex);
        } else {
            return convertCurrencyAndPrepareResponse(request, rateRequestDTO);
        }
    }

    @PostMapping(path = "/convert", produces = "application/json")
    public ResponseEntity<?> ratesPost(HttpServletRequest request, @RequestBody @Valid RateRequestDTO rateRequestDTO) {
        return convertCurrencyAndPrepareResponse(request, rateRequestDTO);
    }

    private ResponseEntity<?> convertCurrencyAndPrepareResponse(HttpServletRequest request, @NotNull RateRequestDTO rateRequestDTO) {
        try {
        logger.info(
                "Convert: {} to {} with value: {}",
                rateRequestDTO.getSourceCurrency(),
                rateRequestDTO.getTargetCurrency(),
                rateRequestDTO.getMonetaryValue());

        MonetaryUnit monetaryUnit = new MonetaryUnit(
                Currency.getInstance(rateRequestDTO.getSourceCurrency()),
                new BigDecimal(rateRequestDTO.getMonetaryValue())
        );

        Currency targetCurrency = Currency.getInstance(rateRequestDTO.getTargetCurrency());
        MonetaryUnit targetMonetaryUnit;

            targetMonetaryUnit = currencyRateService.convert(monetaryUnit, targetCurrency);
            Locale locale = localeResolver.resolveLocale(request);
            NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
            nf.setCurrency(targetMonetaryUnit.getCurrency());
            RateResponseDTO rateResponseDTO = new RateResponseDTO(nf.format(targetMonetaryUnit.getMonitoryValue()), System.currentTimeMillis());

            return ResponseEntity.ok(rateResponseDTO);
        } catch (CurrencyPairNotSupportedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (SwopAPINotAvailableException e) {
            return ResponseEntity.status(502).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
