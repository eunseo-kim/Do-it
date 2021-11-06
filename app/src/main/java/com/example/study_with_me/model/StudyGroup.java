package com.example.study_with_me.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudyGroup {
    private String name;
    private String description;
    private String type;
    private int numOfMember;
    private Date startDate;
    private Date endDate;

    public StudyGroup(String name, String description, String type, int numOfMember, Date startDate, Date endDate) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.numOfMember = numOfMember;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public String getStartDate() {
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        return df.format(startDate);
    }
    public String getEndDate() {
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        return df.format(endDate);
    }
    public int getMember() { return numOfMember; }
}