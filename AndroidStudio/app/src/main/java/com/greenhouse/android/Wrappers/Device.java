package com.greenhouse.android.Wrappers;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.greenhouse.android.Wrappers.APIResponse.GreenData;

@Entity(tableName = "device_table")
public class Device {
    @PrimaryKey @NonNull
    public String eui;
    public String location;
    public int minTemperature;
    public int maxTemperature;
    public int minHumidity;
    public int maxHumidity;
    public int minCO2;
    public int maxCO2;
    public int minLight;
    public int maxLight;
    public int targetTemperature;
    public int targetHumidity;
    public int targetLight;
    public int targetCO2;

    @Embedded
    public GreenData lastData;

    public GreenData getLatest() {
        return lastData;
    }

    public void setLatest(GreenData lastData) {
        this.lastData = lastData;
    }

    public Device(String eui, String location, int minTemperature, int maxTemperature, int minHumidity, int maxHumidity, int minCO2, int maxCO2, int minLight, int maxLight, int targetTemperature, int targetHumidity, int targetLight, int targetCO2) {
        this.eui = eui;
        this.location = location;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.minHumidity = minHumidity;
        this.maxHumidity = maxHumidity;
        this.minCO2 = minCO2;
        this.maxCO2 = maxCO2;
        this.minLight = minLight;
        this.maxLight = maxLight;
        this.targetTemperature = targetTemperature;
        this.targetHumidity = targetHumidity;
        this.targetLight = targetLight;
        this.targetCO2 = targetCO2;
    }

    @Ignore
    public Device(String eui, String location) {
        this.eui = eui;
        this.location = location;
    }


//    @Override
//    public String toString() {
//        return eui;
//    }


    @Override
    public String toString() {
        return eui;
    }

    public String getEui() {
        return eui;
    }

    public void setEui(String eui) {
        this.eui = eui;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(int minTemperature) {
        this.minTemperature = minTemperature;
    }

    public int getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(int maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public int getMinHumidity() {
        return minHumidity;
    }

    public void setMinHumidity(int minHumidity) {
        this.minHumidity = minHumidity;
    }

    public int getMaxHumidity() {
        return maxHumidity;
    }

    public void setMaxHumidity(int maxHumidity) {
        this.maxHumidity = maxHumidity;
    }

    public int getMinCO2() {
        return minCO2;
    }

    public void setMinCO2(int minCO2) {
        this.minCO2 = minCO2;
    }

    public int getMaxCO2() {
        return maxCO2;
    }

    public void setMaxCO2(int maxCO2) {
        this.maxCO2 = maxCO2;
    }

    public int getMinLight() {
        return minLight;
    }

    public void setMinLight(int minLight) {
        this.minLight = minLight;
    }

    public int getMaxLight() {
        return maxLight;
    }

    public void setMaxLight(int maxLight) {
        this.maxLight = maxLight;
    }

    public int getTargetTemperature() {
        return targetTemperature;
    }

    public void setTargetTemperature(int targetTemperature) {
        this.targetTemperature = targetTemperature;
    }

    public int getTargetHumidity() {
        return targetHumidity;
    }

    public void setTargetHumidity(int targetHumidity) {
        this.targetHumidity = targetHumidity;
    }

    public int getTargetLight() {
        return targetLight;
    }

    public void setTargetLight(int targetLight) {
        this.targetLight = targetLight;
    }

    public int getTargetCO2() {
        return targetCO2;
    }

    public void setTargetCO2(int targetCO2) {
        this.targetCO2 = targetCO2;
    }
}



