package com.example.study_with_me.model;

public class UserModel {
    public String email;
    public String password;
    public String username;

    public UserModel(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String setUsername() {
        return username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
