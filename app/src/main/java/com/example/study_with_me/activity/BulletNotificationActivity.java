package com.example.study_with_me.activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;
import com.example.study_with_me.adapter.BulletNotificationAdapter;
import com.example.study_with_me.model.MemberNotification;

import java.util.ArrayList;

public class BulletNotificationActivity extends AppCompatActivity {
    ArrayList<MemberNotification> notiDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bullet_notification);

        this.InitializeNotiData();

        ListView listView = (ListView) findViewById(R.id.notiListView);
        final BulletNotificationAdapter notiAdapter = new BulletNotificationAdapter(this, notiDataList);

        listView.setAdapter(notiAdapter);
    }

    public void InitializeNotiData() {
        notiDataList = new ArrayList<MemberNotification>();
        notiDataList.add(new MemberNotification(R.drawable.tmp_person_icon, "Park Jeong Yong", "2021년 11월 1일", "안녕하세요. 반갑습니다."));
        notiDataList.add(new MemberNotification(R.drawable.tmp_person_icon, "Park Jeong Yong2", "2021년 11월 1일", "안녕하세요. 반갑습니다."));
        notiDataList.add(new MemberNotification(R.drawable.tmp_person_icon, "Park Jeong Yong3", "2021년 11월 1일", "안녕하세요. 반갑습니다."));
    }
}
