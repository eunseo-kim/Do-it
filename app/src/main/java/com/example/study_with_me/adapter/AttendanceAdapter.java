package com.example.study_with_me.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.study_with_me.R;

import java.util.ArrayList;
import java.util.Map;

public class AttendanceAdapter extends BaseAdapter {
    private ArrayList<Map<String, Object>> memberList = new ArrayList<>();
    Context context;
    LayoutInflater layoutInflater;


    public AttendanceAdapter(Context context, ArrayList<Map<String, Object>> memberList) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(this.context);
        this.memberList = memberList;
    }

    @Override
    public int getCount() {
        return memberList.size();
    }

    @Override
    public Object getItem(int position) {
        return memberList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.member_attendance_item, null);
        TextView memberName = (TextView) view.findViewById(R.id.memberName);            // 멤버 이름
        TextView attendTime = (TextView) view.findViewById(R.id.attendTime);            // 등록 시간
        TextView attendPlace = (TextView) view.findViewById(R.id.attendPlace);          // 등록 장소
        TextView checkAttendance = (TextView) view.findViewById(R.id.checkAttendance);  // 출석 여부

        Map<String, Object> member = memberList.get(position);
        memberName.setText(member.get("memberName").toString());
        attendTime.setText(member.get("time").toString());
        attendPlace.setText(member.get("place").toString());
        Boolean attended = (Boolean) member.get("attend");
        Boolean isSet = (Boolean) member.get("isSet");
        if (!isSet) {
            checkAttendance.setText("미등록");
        } else {
            if (!attended) checkAttendance.setText("미출석");
            else checkAttendance.setText("출석");
        }
        return view;
    }
}