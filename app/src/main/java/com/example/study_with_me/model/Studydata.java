package com.example.study_with_me.model;

public class Studydata {
    private String recuit;
    private String field;
    private String title;
    private String date;

    public Studydata(String recuit, String field, String title, String date) {
        this.recuit = recuit;
        this.field = field;
        this.title = title;
        this.date = date;
    }
    public String getStudyRecuit() {return this.recuit;}
    public String getStudyField() {return this.field;}
    public String getStudyTitle() {return this.title;}
    public String getStudydate() {return this.date;}


}
