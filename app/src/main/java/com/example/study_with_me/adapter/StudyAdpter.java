package com.example.study_with_me.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.example.study_with_me.R;
import com.example.study_with_me.model.Studydata;


import java.util.ArrayList;

public class StudyAdpter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Studydata> studyList;

    // 필터링된 결과 데이터를 저장하기 위한 데이터
//    ArrayList<Studydata> filteredItemList ;

    public StudyAdpter(Context context, ArrayList<Studydata> studyList) {
        this.context = context;
        this.studyList = studyList;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return studyList.size();
    }
//
//    @Override
//    public Object getItem(int i) {
//        return i;
//    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public Studydata getItem(int position) {
        return studyList.get(position);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = layoutInflater.inflate(R.layout.study_search_item, null);

        TextView recuit = (TextView) view.findViewById(R.id.studyRecuit);
        TextView field = (TextView) view.findViewById(R.id.studyField);
        TextView title = (TextView) view.findViewById(R.id.studyTitle);
        TextView date = (TextView) view.findViewById(R.id.studyRegisterDay);

        recuit.setText(studyList.get(i).getStudyRecuit());
        field.setText(studyList.get(i).getStudyField());
        title.setText(studyList.get(i).getStudyTitle());
        date.setText(studyList.get(i).getStudydate());

//        final TextView study_name = (TextView) convertView.findViewById(R.id.studyTitle);
//        MyItem myItem = getItem(i);
//        study_name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context.getApplicationContext(), "halo", Toast.LENGTH_SHORT).show();
//            }
//        });
        return view;
    }
}
//
//    @Override
//    public Filter getFilter() {
//        return filteredItemList.size();
//    }
//}