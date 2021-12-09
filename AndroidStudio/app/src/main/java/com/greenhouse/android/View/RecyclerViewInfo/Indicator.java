package com.greenhouse.android.View.RecyclerViewInfo;

public class Indicator {

    private int iconId;
    private String name;
    private int show;

    public Indicator(int iconId, String name, int show) {
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

    public int getShow() {
        return show;
    }
}
