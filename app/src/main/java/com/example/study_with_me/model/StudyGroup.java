package com.example.study_with_me.model;

import com.google.firebase.database.PropertyName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudyGroup {

    private String name;        // 스터디 이름
    private String description; // 스터디 설명
    private String type;        // 스터디 종류
    private int numOfMember;    // 스터디 인원
    private Date startDate;     // 스터디 시작 날짜
    private Date endDate;       // 스터디 종료 날짜
    private String leader;      // 스터디 방장 정보
    private boolean closed;     // 스터디 마감 여부
    private ArrayList<Applicant> applicantList = new ArrayList<>();   // 스터디 신청자 리스트
    private ArrayList<String> memberList = new ArrayList<>();        // 스터디 멤버 리스트


    public StudyGroup(String leader, String name, String description, String type, int numOfMember, Date startDate, Date endDate) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.numOfMember = numOfMember;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leader = leader;
        this.closed = true;
        memberList.add(leader);

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd hh:mm");
        String registerTime = dateFormat.format(date);

        applicantList.add(new Applicant(leader, registerTime, name));
    }


    public StudyGroup(String leader, String name, String description,
                      String type, int numOfMember, Date startDate,
                      Date endDate, boolean closed,
                      ArrayList<Applicant> applicantList,
                      ArrayList<String> memberList) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.numOfMember = numOfMember;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leader = leader;
        this.closed = closed;
        this.applicantList = applicantList;
        this.memberList = memberList;
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
    public String getLeader() { return leader; }
    public List<Applicant> getApplicantList() { return applicantList; }
    public ArrayList<String> getMemberList() { return memberList; }

    public boolean getClosed() {
        return this.closed;
    }

    public int getCurrentMemberCount() {
        return this.memberList.size();
    }
}