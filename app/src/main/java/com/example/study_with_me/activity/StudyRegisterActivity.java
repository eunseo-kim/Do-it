package com.example.study_with_me.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;

public class StudyRegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_register);
        getSupportActionBar().setTitle("스터디 등록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}