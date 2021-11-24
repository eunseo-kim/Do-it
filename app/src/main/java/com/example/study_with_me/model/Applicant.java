package com.example.study_with_me.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Applicant {
    String userID;
    String registerTime;
    String studyGroupTitle;
    String username;
    String studyGroupID;

    public Applicant(String userID, String username, String registerTime, String studyGroupID, String studyGroupTitle) {
        this.userID = userID;
        this.username = username;

        this.studyGroupTitle = studyGroupTitle;
        this.studyGroupID = studyGroupID;

        this.registerTime = registerTime;
        /* long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM-dd hh:mm");
        String time = dateFormat.format(date); */
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return username;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public String getStudyGroupTitle() { return studyGroupTitle; }

    public String getStudyGroupID() {
        return studyGroupID;
    }
}
