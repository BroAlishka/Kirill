package com.example.mapchat.model;

public class User {
    private final String displayName;
    private final String login;
    private final String password;

    public User(String displayName, String login, String password) {
        this.displayName = displayName;
        this.login = login;
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
