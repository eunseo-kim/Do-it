package com.example.study_with_me.model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UserModel {
    private String email;
    private String password;
    private String username;
    private float rating;
    private ArrayList<String> studyGroupIDList;
    private int dropCount;
    private int joinCount;
    private int ratingCount;
    private String userID;
    private Map<String, String> appliedStudyGroupIDList;

    public UserModel(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.rating = 0.0F;
        this.dropCount = 0;
        this.joinCount = 0;
        this.ratingCount = 0;
        studyGroupIDList = new ArrayList<>();
        appliedStudyGroupIDList = new HashMap<>();
    }

    public void addDropCount() { this.dropCount += 1; }
    public void addJoinCount() { this.joinCount += 1; }
    public void addRatingCount() { this.ratingCount += 1; }
    public int getDropCount() { return this.dropCount; }
    public int getJoinCount() { return this.joinCount; }
    public int getRatingCount() { return this.ratingCount; }

    public void setUsername(String name) {
        this.username = name;
    }
    public String getUsername() { return username; }
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
    public Map<String, String> getAppliedStudyGroupIDList() {
        return appliedStudyGroupIDList;
    }
}
