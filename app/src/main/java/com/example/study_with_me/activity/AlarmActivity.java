package com.example.study_with_me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

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
