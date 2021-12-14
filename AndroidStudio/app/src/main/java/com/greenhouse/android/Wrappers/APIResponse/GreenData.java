package com.greenhouse.android.Wrappers.APIResponse;

import androidx.room.TypeConverters;

import com.greenhouse.android.Util.Converters;

import java.util.Date;

@TypeConverters(Converters.class)
public class GreenData {
    public int id;
    public Date date;
    public int humidity;
    public int temperature;
    public int light;
    public int co2;

    public GreenData(int id, Date date, int humidity, int temperature, int light, int co2) {
        this.id = id;
        this.date = date;
        this.humidity = humidity;
        this.temperature = temperature;
        this.light = light;
        this.co2 = co2;
    }

    public GreenData() {
        this.humidity = -1;
        this.temperature = -1;
        this.light = -1;
        this.co2 = -1;
        this.date = new Date();
        this.id = -1;
    }
    public GreenData(int humidity, int temperature, int light, int co2) {
        this.id = -1;
        this.date = new Date();
        this.humidity = humidity;
        this.temperature = temperature;
        this.light = light;
        this.co2 = co2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public int getCo2() {
        return co2;
    }

    public void setCo2(int co2) {
        this.co2 = co2;
    }

    @Override
    public String toString() {
        return "GreenData{" +
                "id=" + id +
                ", date=" + date +
                ", humidity=" + humidity +
                ", temperature=" + temperature +
                ", light=" + light +
                ", co2=" + co2 +
                '}';
    }
}
