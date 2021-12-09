package com.greenhouse.android.Wrappers;

import com.greenhouse.android.Wrappers.APIResponse.GreenData;

public class Device {
    private String title;
    private String details;
    private GreenData minThreshold;
    private GreenData maxThreshold;
    private GreenData target;
    private GreenData latest;

    public Device(String title, GreenData latest) {
        this.title = title;
        this.latest = latest;
    }

    public Device(String title, String details, GreenData minThreshold, GreenData maxThreshold, GreenData target, GreenData latest) {
        this.title = title;
        this.details = details;
        this.minThreshold = minThreshold;
        this.maxThreshold = maxThreshold;
        this.target = target;
        this.latest = latest;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public GreenData getMinThreshold() {
        return minThreshold;
    }

    public void setMinThreshold(GreenData minThreshold) {
        this.minThreshold = minThreshold;
    }

    public GreenData getMaxThreshold() {
        return maxThreshold;
    }

    public void setMaxThreshold(GreenData maxThreshold) {
        this.maxThreshold = maxThreshold;
    }

    public GreenData getTarget() {
        return target;
    }

    public void setTarget(GreenData target) {
        this.target = target;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public GreenData getLatest() {
        return latest;
    }

    public void setLatest(GreenData latest) {
        this.latest = latest;
    }
}
