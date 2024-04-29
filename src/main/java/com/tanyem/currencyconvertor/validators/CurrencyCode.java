package com.tanyem.currencyconvertor.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CurrencyCodeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrencyCode {

    String message() default "Unexpected currency code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
