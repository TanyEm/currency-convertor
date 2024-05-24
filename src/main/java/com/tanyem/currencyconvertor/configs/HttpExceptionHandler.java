package com.tanyem.currencyconvertor.configs;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class HttpExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Map<String, List<String>>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errorsMap = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = toSnakeCase(((FieldError) error).getField());
            String errorMessage = error.getDefaultMessage();
            if (!errorsMap.containsKey(fieldName)) {
                errorsMap.put(fieldName, new ArrayList<>());
            }
            errorsMap.get(fieldName).add(errorMessage);
        });

        // sort the error lists alphabetically for consistency
        errorsMap.forEach((key, value) -> value.sort(String::compareTo));

        Map<String, Map<String, List<String>>> errorsResponse = new HashMap<>();
        errorsResponse.put("errors", errorsMap);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        return new ResponseEntity<>(errorsResponse,
                headers,
                HttpStatus.BAD_REQUEST);
    }

    private String toSnakeCase(String s) {
        return s.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
