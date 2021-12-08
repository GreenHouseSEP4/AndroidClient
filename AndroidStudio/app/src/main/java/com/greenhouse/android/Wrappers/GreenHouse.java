package com.greenhouse.android.Wrappers;

import com.greenhouse.android.Wrappers.APIResponse.GreenData;

public class GreenHouse {
    private String title;
    private GreenData latest;

    public GreenHouse(String title, GreenData latest) {
        this.title = title;
        this.latest = latest;
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
