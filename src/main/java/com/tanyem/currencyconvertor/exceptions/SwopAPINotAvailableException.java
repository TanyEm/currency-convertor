package com.tanyem.currencyconvertor.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class SwopAPINotAvailableException extends RuntimeException {

        public SwopAPINotAvailableException(String message) {
            super(message);
        }

        @Override
        public String getMessage() {
            Map<String, Map<String, List<String>>> errors = new HashMap<>();
            Map<String, List<String>> error = new HashMap<>();
            error.put("bad_gateway", List.of(super.getMessage()));
            errors.put("errors", error);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writeValueAsString(errors);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

}
