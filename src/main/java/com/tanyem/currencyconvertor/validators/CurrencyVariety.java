package com.tanyem.currencyconvertor.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CurrencyVarietyValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrencyVariety {

    String message() default "Source currency and target currency must be different";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
