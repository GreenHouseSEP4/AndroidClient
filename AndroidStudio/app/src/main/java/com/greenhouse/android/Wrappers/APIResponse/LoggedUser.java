package com.greenhouse.android.Wrappers.APIResponse;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.greenhouse.android.Wrappers.User;
@Entity(tableName = "user_table")
public class LoggedUser {
    @PrimaryKey
    private int userId;
    @Embedded
    private String token;
    @Embedded
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Ignore
    public LoggedUser(String token) {
        this.token = token;
    }

    public LoggedUser(String token, User user) {
        this.token = token;
        this.user = user;
        this.userId = user.getId();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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