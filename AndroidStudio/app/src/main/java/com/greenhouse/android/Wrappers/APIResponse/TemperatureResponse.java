package com.greenhouse.android.Wrappers.APIResponse;

import com.greenhouse.android.Wrappers.Temperature;

public class TemperatureResponse {
    private String value;

    public Temperature getData()
    {
        return new Temperature(value);
    }
}
