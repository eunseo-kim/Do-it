package com.example.study_with_me.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.study_with_me.R;
import com.example.study_with_me.model.MemberSampledata;
import com.example.study_with_me.model.StudyGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class StudyGroupAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    private ArrayList<Map<String, Object>> studyList = new ArrayList<>();

    public StudyGroupAdapter(Context context, ArrayList<Map<String, Object>> studyList) {
        this.context = context;
        this.studyList = studyList;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return studyList.size();
    }

    @Override
    public Object getItem(int position) {
        return studyList.get(position);
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
        TextView currentMemberCount = (TextView)view.findViewById(R.id.currentMemberCount);
        TextView maxMemberCount = (TextView)view.findViewById(R.id.maxMemberCount);
        TextView studyRegisterDay = (TextView) view.findViewById(R.id.studyRegisterDay);

        Map<String, Object> studyGroup = studyList.get(position);
        Log.d("checkCheck", String.valueOf(studyGroup.get("memberList")));
//        ArrayList<String> memberList = studyGroup.get("memberList").getMemberList();
        studyField.setText(String.valueOf(studyGroup.get("type")));
        studyTitle.setText(String.valueOf(studyGroup.get("name")));
        studyRegisterDay.setText(String.valueOf(studyGroup.get("endDate")));

        ArrayList memberList = new ArrayList((Collection) studyGroup.get("memberList"));
        currentMemberCount.setText(String.valueOf(memberList.size()));
        maxMemberCount.setText(String.valueOf(studyGroup.get("member")));

        boolean closed = new Boolean(String.valueOf(studyGroup.get("closed")));
        String currentRecruit;
        if (closed) {
            currentRecruit = "모집마감";
        } else {
            currentRecruit = "모집중";
        }
        studyRecuit.setText(currentRecruit);


        return view;
    }
}
