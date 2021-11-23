package com.example.study_with_me.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.study_with_me.R;
import java.util.ArrayList;
import java.util.Map;

public class SchedulerAdapter extends BaseAdapter {
    private ArrayList<String> scheduleList = new ArrayList<>();
    Context context;
    LayoutInflater layoutInflater;


    public SchedulerAdapter(Context context, ArrayList<String> scheduleList) {
        this.context = context;
        this.scheduleList = scheduleList;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return scheduleList.size();
    }

    @Override
    public Object getItem(int position) {
        return scheduleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.schedule_item, null);
        TextView scheduleTextView = (TextView) view.findViewById(R.id.scheduleTextView);
        scheduleTextView.setText(scheduleList.get(position));
        return view;
    }
}