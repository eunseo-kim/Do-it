package com.example.study_with_me.model;

import java.util.Date;

public class StudyGroup {
    private String name;
    private String type;
    private int numOfMember;
    private Date term;

    private String getName() { return name; }
    public String getType() { return type; }
    public int getMember() { return numOfMember; }
    public Date getTerm() { return term; }
}