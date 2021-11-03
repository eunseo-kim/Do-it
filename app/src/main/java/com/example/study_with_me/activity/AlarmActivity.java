package com.example.study_with_me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.study_with_me.R;
import com.example.study_with_me.model.AlarmSampledata;

import java.util.ArrayList;

public class AlarmActivity extends AppCompatActivity {
    ArrayList<AlarmSampledata> alarmDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.alarm);

        // 상단 메뉴바
        getSupportActionBar().setTitle("알림");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // ListView adapter
        ListView listView = findViewById(R.id.alarmListView);
        String[] nameList = {"김은서", "박정용", "고다혜", "김진욱"}; // 임의로 이름 4개만 전달
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.alarm_item, R.id.alarmMemberName, nameList) ; //어댑터를 리스트 뷰에 적용
        listView.setAdapter(adapter);
    }

    @Override
    // action_bar
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void InitalizeAlarmData() {
        alarmDataList.add(new AlarmSampledata("kodahye", "14:33"));
        alarmDataList.add(new AlarmSampledata("kodahmi", "13:33"));
    }
}
