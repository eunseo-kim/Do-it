package com.example.study_with_me.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.study_with_me.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class BulletNotificationAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Map<String, Object>> bulletinList;
    TextView userName, date, textView;
    ImageView imageView;
    long registerTime;

    public BulletNotificationAdapter(Context context, ArrayList<Map<String, Object>> bulletinList) {
        this.context = context;
        this.bulletinList = bulletinList;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return bulletinList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setItemImage(int position, Uri uri) {

    }

    @Override
    public Map<String, Object> getItem(int position) {
        return bulletinList.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.bullet_item, null);

        userName = (TextView)view.findViewById(R.id.userName);
        date = (TextView)view.findViewById(R.id.date);
        textView = (TextView)view.findViewById(R.id.textView);

        /* set registerTime(milliSecond to Date) */
        registerTime = Long.parseLong(bulletinList.get(position).get("registerTime").toString());
        String dateString = getDate(registerTime, "yyyy/MM/dd HH:mm:ss");
        date.setText(dateString);

        /* set writer userName */
        String writerName = bulletinList.get(position).get("writerName").toString();
        userName.setText(writerName);

        String text = bulletinList.get(position).get("text").toString();
        textView.setText(text);

        return view;
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
