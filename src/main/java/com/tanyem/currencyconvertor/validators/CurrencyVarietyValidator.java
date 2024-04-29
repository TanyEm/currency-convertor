package com.tanyem.currencyconvertor.validators;

import com.tanyem.currencyconvertor.dtos.RateRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CurrencyVarietyValidator implements ConstraintValidator<CurrencyVariety, RateRequestDTO> {

        @Override
        public boolean isValid(RateRequestDTO value, ConstraintValidatorContext context) {

            if (value == null) {
                return true;
            }

            if (value.getSource_currency().equals(value.getTarget_currency())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode("source_currency")
                        .addConstraintViolation();
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode("target_currency")
                        .addConstraintViolation();
                return false;
            }

            return true;
        }
}
