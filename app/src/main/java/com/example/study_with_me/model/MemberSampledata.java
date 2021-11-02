package com.example.study_with_me.model;

public class MemberSampledata {
    private int memberImage;
    private String name;

    public MemberSampledata(int memberImage, String name) {
        this.memberImage = memberImage;
        this.name = name;
    }

    public int getMemberImage() {
        return this.memberImage;
    }
    public String getName() {
        return this.name;
    }
}
