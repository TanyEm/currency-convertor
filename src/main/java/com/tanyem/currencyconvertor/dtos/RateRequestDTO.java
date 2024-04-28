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

    @Positive(message = "Monetary value must be greater than 0")
    public long monetary_value;

}
