package com.example.study_with_me.model;

public class Applicant {
    String userID;
    String registerTime;
    String studyGroupTitle;

    public Applicant(String userID, String registerTime, String studyGroupTitle) {
        this.userID = userID;
        this.registerTime = registerTime;
        this.studyGroupTitle = studyGroupTitle;
    }

    public String getUserID() {
        return userID;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public String getStudyGroupTitle() { return studyGroupTitle; }
}
