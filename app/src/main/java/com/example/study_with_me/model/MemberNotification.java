package com.example.study_with_me.model;

import android.widget.ImageView;

public class MemberNotification {
    private int memberImage;
    private String name;
    private String date;
    private String comment;

    public MemberNotification(int memberImage, String name, String date, String comment) {
        this.memberImage = memberImage;
        this.name = name;
        this.date = date;
        this.comment = comment;
    }

    public int getMemberImage() { return this.memberImage; }
    public String getName() { return this.name; }
    public String getDate() { return this.date; }
    public String getComment() { return this.comment; }
}
