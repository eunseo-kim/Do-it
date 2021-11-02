package com.example.study_with_me.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;

public class AttendanceRegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_register);

        // 상단 메뉴바
        getSupportActionBar().setTitle("출석 등록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
