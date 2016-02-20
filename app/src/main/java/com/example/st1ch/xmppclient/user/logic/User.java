package com.example.st1ch.xmppclient.user.logic;

/**
 * Created by st1ch on 01.12.15.
 */
public class User {
    private String password;
    private String login;
    public String name;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
