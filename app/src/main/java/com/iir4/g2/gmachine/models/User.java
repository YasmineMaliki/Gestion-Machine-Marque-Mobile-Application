package com.iir4.g2.gmachine.models;

public class User {
    private Integer userId;
    private String username;
    private String password;
    static int userIdStatic = 0;

    public User(String username, String password) {
        userId = ++userIdStatic;
        this.username = username;
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

