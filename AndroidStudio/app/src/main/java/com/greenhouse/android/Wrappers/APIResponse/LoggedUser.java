package com.greenhouse.android.Wrappers.APIResponse;


import com.greenhouse.android.Wrappers.User;
public class LoggedUser {
    private String token;
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoggedUser(String token) {
        this.token = token;
    }

    public LoggedUser(String token, User user) {
        this.token = token;
        this.user = user;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "LoggedUser{" +
                "token='" + token + '\'' +
                ", user=" + user +
                '}';
    }
}