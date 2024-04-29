package com.tanyem.currencyconvertor.dtos;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class RateRequestDTO {

    @NotBlank(message = "Source currency is required")
    @NotNull(message = "Source currency is required")
    @Size(min = 3, max = 3, message = "Source currency must be 3 characters")
    public String source_currency;

    @NotBlank(message = "Target currency is required")
    @NotNull(message = "Target currency is required")
    @Size(min = 3, max = 3, message = "Target currency must be 3 characters")
    public String target_currency;

    @Pattern(
            regexp = "^[1-9][0-9]+$",
            message = "Monetary value must be a positive number in minor units (e.g. 1_000_000 for $1)"
    )
    @Size(max = 18, message = "Monetary value must be less or equal than 18 digits")
    public String monetary_value;

}
