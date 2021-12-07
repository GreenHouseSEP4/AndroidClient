package com.greenhouse.android;

public class Indicator {

    private int iconId;
    private String name;
    private String show;

    public Indicator(int iconId, String name, String show) {
        this.iconId = iconId;
        this.name = name;
        this.show = show;
    }


    public int getIconId() {
        return iconId;
    }

    public String getName() {
        return name;
    }

    public String getShow() {
        return show;
    }
}
