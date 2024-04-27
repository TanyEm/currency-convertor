package com.tanyem.currencyconvertor.models;

public class RateResponseModel {
    private long result;
    private long timestamp;

    public RateResponseModel(long result, long timestamp) {
        this.result = result;
        this.timestamp = timestamp;
    }

    public long getResult() {
        return result;
    }

    public void setResult(long result) {
        this.result = result;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
