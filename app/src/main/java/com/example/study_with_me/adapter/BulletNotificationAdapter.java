package com.example.study_with_me.adapter;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.study_with_me.R;
import com.example.study_with_me.model.MemberNotification;

import java.util.ArrayList;

public class BulletNotificationAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<MemberNotification> noti;

    public BulletNotificationAdapter(Context context, ArrayList<MemberNotification> noti) {
        this.context = context;
        this.noti = noti;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return noti.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public MemberNotification getItem(int position) {
        return noti.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.bullet_notification_item, null);

        ImageView memberImage = (ImageView)view.findViewById(R.id.member_profile);
        TextView memberName = (TextView)view.findViewById(R.id.alarmMemberName);
        TextView date = (TextView)view.findViewById(R.id.notiDate);
        TextView comment = (TextView)view.findViewById(R.id.notiComment);

        memberImage.setImageResource(noti.get(position).getMemberImage());
        memberImage.setBackground(new ShapeDrawable(new OvalShape()));
        memberImage.setClipToOutline(true);

        memberName.setText(noti.get(position).getName());
        date.setText(noti.get(position).getDate());
        comment.setText(noti.get(position).getComment());

        return view;
    }
}
