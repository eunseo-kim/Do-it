package com.example.study_with_me.model;
import java.util.ArrayList;

public class UserModel {
    public String email;
    public String password;
    public String username;
    public float rating;
    public ArrayList<String> studyGroupList;
    public int dropCount;
    public int joinCount;
    public String userID;

    public UserModel(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.rating = 5.0F;
        this.studyGroupList = new ArrayList();
        this.dropCount = 0;
        this.joinCount = 0;
    }

    public void addDropCount() { this.dropCount += 1; }
    public void addJoinCount() { this.joinCount += 1; }
    public int getDropCount() { return this.dropCount; }
    public int getJoinCount() { return this.joinCount; }

    public void setUsername(String name) {
        this.username = name;
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

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }
}
