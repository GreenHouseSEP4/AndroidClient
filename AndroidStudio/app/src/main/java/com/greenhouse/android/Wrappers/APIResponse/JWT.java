package com.greenhouse.android.Wrappers.APIResponse;

public class JWT {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public JWT(String token) {
        this.token = token;
    }
}
