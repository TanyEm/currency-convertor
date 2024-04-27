package com.tanyem.currencyconvertor.models;

public class RateResponseModel {
    private double result;
    private long timestamp;

    public RateResponseModel(double result, long timestamp) {
        this.result = result;
        this.timestamp = timestamp;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
