package com.tanyem.currencyconvertor.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RateResponseModel {
    private long result;
    private long timestamp;

    public RateResponseModel(long result, long timestamp) {
        this.result = result;
        this.timestamp = timestamp;
    }

}
