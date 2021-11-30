package com.example.study_with_me.adapter;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.study_with_me.R;
import com.example.study_with_me.model.MemberNotification;

import java.util.ArrayList;

public class BulletNotificationAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<MemberNotification> notiList;

    public BulletNotificationAdapter(Context context, ArrayList<MemberNotification> notiList) {
        this.context = context;
        this.notiList = notiList;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return notiList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public MemberNotification getItem(int position) {
        return notiList.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.bullet_item, null);

        TextView userName = (TextView)view.findViewById(R.id.userName);
        TextView date = (TextView)view.findViewById(R.id.date);
        TextView textView = (TextView)view.findViewById(R.id.textView);
        ImageView imageView = view.findViewById(R.id.imageView);
        Button fileView = view.findViewById(R.id.fileView);

        return view;
    }
}
