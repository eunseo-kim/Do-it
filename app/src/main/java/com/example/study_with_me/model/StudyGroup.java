package com.example.study_with_me.model;

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

    private String getName() { return name; }
    private String getDescription() { return description; }
    public String getType() { return type; }
    public int getMember() { return numOfMember; }

}