package com.tanyem.currencyconvertor.dtos;

import com.tanyem.currencyconvertor.validators.CurrencyCode;
import com.tanyem.currencyconvertor.validators.CurrencyVariety;
import com.tanyem.currencyconvertor.validators.ValidBigDecimalValueString;
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
    @ValidBigDecimalValueString(message = "Monetary value must be a valid positive number")
    public String monetary_value;

}
