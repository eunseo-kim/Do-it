package com.example.study_with_me.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;

public class MyStudyRoomActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_study_room_main);
        getSupportActionBar().setTitle("내 스터디 목록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
