package com.example.study_with_me.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.study_with_me.R;
import com.example.study_with_me.model.MemberSampledata;
import com.example.study_with_me.model.StudyGroup;

import java.util.ArrayList;

public class StudyGroupAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<StudyGroup> studyGroups;

    public StudyGroupAdapter(Context context, ArrayList<StudyGroup> studyGroups) {
        this.context = context;
        this.studyGroups = studyGroups;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() { return studyGroups.size(); }

    @Override
    public StudyGroup getItem(int position) { return studyGroups.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.study_search_item, null);

        TextView studyField = (TextView) view.findViewById(R.id.studyField);
        TextView studyTitle = (TextView) view.findViewById(R.id.studyTitle);
        TextView studyRegisterDate = (TextView) view.findViewById(R.id.studyRegisterDay);

        studyField.setText(studyGroups.get(position).getType());
        studyTitle.setText(studyGroups.get(position).getName());
        studyRegisterDate.setText(studyGroups.get(position).getStartDate());

        return view;
    }
}
