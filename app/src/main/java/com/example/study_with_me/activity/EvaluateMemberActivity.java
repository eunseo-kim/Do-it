package com.example.study_with_me.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;
import com.example.study_with_me.adapter.TeamEvaluationAdapter;
import com.example.study_with_me.model.MemberNotification;
import com.example.study_with_me.model.MemberSampledata;

import java.util.ArrayList;
import java.util.List;

public class EvaluateMemberActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluate_member_rating);
    }
}
