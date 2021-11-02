package com.example.study_with_me.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.study_with_me.R;
import com.example.study_with_me.model.AlarmSampledata;

import java.util.ArrayList;

public class AlarmAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<AlarmSampledata> alarm;

    public AlarmAdapter(Context context, ArrayList<AlarmSampledata> data) {
        this.context = context;
        this.alarm = alarm;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return alarm.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public AlarmSampledata getItem(int position) {
        return alarm.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.bullet_notification_item, null);

        TextView textview1 = (TextView)view.findViewById(R.id.alarmMemberName);
        TextView textview2 = (TextView)view.findViewById(R.id.alarmRegisterTime);

        textview1.setText(alarm.get(position).getName());
        textview2.setText(alarm.get(position).getTime());

        return view;



    }
}
