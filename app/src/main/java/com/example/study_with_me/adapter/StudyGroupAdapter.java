package com.example.study_with_me.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.study_with_me.R;
import com.example.study_with_me.activity.UserInfoJoinActivity;

import java.util.ArrayList;
import java.util.Map;

public class StudyGroupAdapter extends BaseAdapter {
    private ArrayList<Map<String, Object>> studyGroupList = new ArrayList<>();
    Context context;
    LayoutInflater layoutInflater;
    private int MODE;
    private static final int ING = -4;
    private static final int WAITING = -3;
    private static final int CLOSING_SETTING = -2;
    private static final int CLOSED = -1;

    public StudyGroupAdapter(Context context, ArrayList<Map<String, Object>> studyGroupList, int MODE) {
        this.context = context;
        this.studyGroupList = studyGroupList;
        this.layoutInflater = LayoutInflater.from(this.context);
        this.MODE = MODE;
    }

    public StudyGroupAdapter(UserInfoJoinActivity context, ArrayList<Map<String, Object>> studyGroupList) {
        this.context = context;
        this.studyGroupList = studyGroupList;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return studyGroupList.size();
    }

    @Override
    public Object getItem(int position) {
        return studyGroupList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.my_study_room_item, null);

        TextView studyRecuit = (TextView) view.findViewById(R.id.studyRecuit);
        TextView studyField = (TextView) view.findViewById(R.id.studyField);
        TextView studyTitle = (TextView) view.findViewById(R.id.studyTitle);
        TextView currentMemberCount = (TextView) view.findViewById(R.id.currentMemberCount);
        TextView maxMemberCount = (TextView) view.findViewById(R.id.maxMemberCount);
        TextView studyRegisterDay = (TextView) view.findViewById(R.id.studyRegisterDay);

        if (studyGroupList.size() > 0) {
            Map<String, Object> studyGroup = studyGroupList.get(position);
            studyField.setText(String.valueOf(studyGroup.get("type")));
            studyTitle.setText(String.valueOf(studyGroup.get("name")));
            studyRegisterDay.setText(String.valueOf(studyGroup.get("endDate")));

            Map<String, String> memberList = (Map<String, String>) studyGroup.get("memberList");
            if(memberList != null) currentMemberCount.setText(String.valueOf(memberList.size()));
            maxMemberCount.setText(String.valueOf(studyGroup.get("member")));

            boolean closed = new Boolean(String.valueOf(studyGroup.get("closed")));
            String currentRecruit;
            if (closed) currentRecruit = "모집마감";
            else currentRecruit = "모집중";
            studyRecuit.setText(currentRecruit);
        }

        return view;
    }
}