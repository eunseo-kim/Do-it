package com.example.study_with_me.model;

public class Studydata {
    private String recuit;
    private String field;
    private String title;
    private String date;


    public void setRecuit(String recuit) {
        this.recuit = recuit;
    }
    public void setField(String field) {
        this.field = field;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getStudyRecuit() {return this.recuit;}
    public String getStudyField() {return this.field;}
    public String getStudyTitle() {return this.title;}
    public String getStudydate() {return this.date;}


}
