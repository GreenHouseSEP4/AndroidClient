package com.greenhouse.android;

public class TemperatureResponse {
    private String value;

    public Temperature getData()
    {
        return new Temperature(value);
    }
}
