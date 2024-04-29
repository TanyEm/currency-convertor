package com.tanyem.currencyconvertor.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class ValidBigDecimalValueStringValidator implements ConstraintValidator<ValidBigDecimalValueString, String> {
    @Override
    public void initialize(ValidBigDecimalValueString constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            return (new BigDecimal(value)).compareTo(BigDecimal.ZERO) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
