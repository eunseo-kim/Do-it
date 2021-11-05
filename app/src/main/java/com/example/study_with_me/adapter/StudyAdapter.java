package com.example.study_with_me.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.study_with_me.R;
import com.example.study_with_me.activity.MyPageActivity;
import com.example.study_with_me.activity.StudyPostActivityMessage;
import com.example.study_with_me.activity.StudySearchActivity;
import com.example.study_with_me.model.Studydata;


import java.util.ArrayList;

public class StudyAdapter extends BaseAdapter  {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Studydata> studyList = null;
    RecyclerView.ViewHolder viewHolder;

    public StudyAdapter(Context context, ArrayList<Studydata> studyList) {
        this.context = context;
        this.studyList = studyList;
        this.layoutInflater = LayoutInflater.from(this.context);
        this.studyList = new ArrayList<Studydata>();
        this.studyList.addAll(studyList);
    }
    public class ViewHolder {
        TextView recuit;
        TextView field;
        TextView title;
        TextView register;
    }
    @Override
    public int getCount() {
        return studyList.size();
    }

    public Studydata getItem(int position) {
        return studyList.get(position);
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

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final int pos = i;
        final Context context = viewGroup.getContext();
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.study_search_item, viewGroup, false);
        }
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

//        getView2(i, convertView, viewGroup);

        return view;
    }
//    public View getView2(int i , View convertView, ViewGroup viewGroup) {
//        // list와 post게시물 대응하도록해야됨!!
//        LinearLayout studyArea = (LinearLayout) convertView.findViewById(R.id.studyArea);
//        studyArea.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), StudyPostActivityMessage.class);
//                v.getContext().startActivity(intent);
//            }
//        });
//        return convertView;
//    }

}
//
//    @Override
//    public Filter getFilter() {
//        return filteredItemList.size();
//    }
//}