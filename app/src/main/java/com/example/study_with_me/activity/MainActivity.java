package com.example.study_with_me.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.study_with_me.MenuAuthorizeAttendanceFragment;
import com.example.study_with_me.MenuBulletFragment;
import com.example.study_with_me.MenuEvaluateMemberFragment;
import com.example.study_with_me.MenuScheduleManagementFragment;
import com.example.study_with_me.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();

    // bottom Navigation의 4개의 메뉴에 들어갈 각 Fragment들
    private MenuBulletFragment bulletFragment = new MenuBulletFragment();
    private MenuEvaluateMemberFragment evaluateMemberFragment = new MenuEvaluateMemberFragment();
    private MenuAuthorizeAttendanceFragment authAttendanceFragment = new MenuAuthorizeAttendanceFragment();
    private MenuScheduleManagementFragment scheduleManagementFragment = new MenuScheduleManagementFragment();
    private Map<String, Object> studyInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Intent intent = getIntent();
        studyInfo = (Map<String, Object>) intent.getSerializableExtra("studyGroup");

        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, authAttendanceFragment).commitAllowingStateLoss();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navBullet:
                        transaction.replace(R.id.frame_layout, bulletFragment).commitAllowingStateLoss();
                        transaction.addToBackStack(null);
                        break;
                    case R.id.navEvaluateMember:
                        transaction.replace(R.id.frame_layout, evaluateMemberFragment).commitAllowingStateLoss();
                        transaction.addToBackStack(null);
                        break;
                    case R.id.navAuthAttendance:
                        transaction.replace(R.id.frame_layout, authAttendanceFragment).commitAllowingStateLoss();
                        transaction.addToBackStack(null);
                        break;
                    case R.id.navManageSchedule:
                        transaction.replace(R.id.frame_layout, scheduleManagementFragment).commitAllowingStateLoss();
                        transaction.addToBackStack(null);
                        break;
                }
                return true;
            }
        });
    }

    public Map<String, Object> getStudyInfo() {
        return this.studyInfo;
    }
}