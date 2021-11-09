package com.example.study_with_me.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Applicant {
    String userID;
    String registerTime;
    String studyGroupTitle;

    public Applicant(String userID, String registerTime, String studyGroupTitle) {
        this.userID = userID;
        this.studyGroupTitle = studyGroupTitle;

        /* long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM-dd hh:mm");
        String time = dateFormat.format(date); */
        this.registerTime = registerTime;

    }

    public String getUserID() {
        return userID;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public String getStudyGroupTitle() { return studyGroupTitle; }
}
