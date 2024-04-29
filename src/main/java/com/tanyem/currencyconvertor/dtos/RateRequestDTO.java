package com.tanyem.currencyconvertor.dtos;

import com.tanyem.currencyconvertor.validators.CurrencyCode;
import com.tanyem.currencyconvertor.validators.CurrencyVariety;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@CurrencyVariety
public class RateRequestDTO {

    @NotBlank(message = "Source currency cannot be empty")
    @NotNull(message = "Source currency is required")
    @Size(min = 3, max = 3, message = "Source currency must be 3 characters")
    @CurrencyCode
    public String source_currency;

    @NotBlank(message = "Target currency cannot be empty")
    @NotNull(message = "Target currency is required")
    @Size(min = 3, max = 3, message = "Target currency must be 3 characters")
    @CurrencyCode
    public String target_currency;

    @NotBlank(message = "Monetary value cannot be empty")
    @NotNull(message = "Monetary value is required")
    @Pattern(
            regexp = "^[1-9][0-9]+$",
            message = "Monetary value must be a positive number in minor units (e.g. 1_000_000 for $1)"
    )
    @Size(max = 18, message = "Monetary value must be less or equal than 18 digits")
    public String monetary_value;

}
