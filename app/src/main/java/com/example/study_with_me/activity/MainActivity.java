package com.example.study_with_me.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Adapter;

import com.example.study_with_me.MenuAuthorizeAttendanceFragment;
import com.example.study_with_me.MenuBulletFragment;
import com.example.study_with_me.MenuEvaluateMemberFragment;
import com.example.study_with_me.MenuScheduleManagementFragment;
import com.example.study_with_me.R;
import com.example.study_with_me.adapter.TeamEvaluationAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    public FragmentManager fragmentManager = getSupportFragmentManager();

    // bottom Navigation의 4개의 메뉴에 들어갈 각 Fragment들
    private MenuBulletFragment bulletFragment = new MenuBulletFragment();
    private MenuEvaluateMemberFragment evaluateMemberFragment = new MenuEvaluateMemberFragment();
    private MenuAuthorizeAttendanceFragment authAttendanceFragment = new MenuAuthorizeAttendanceFragment();
    private MenuScheduleManagementFragment scheduleManagementFragment = new MenuScheduleManagementFragment();
    private Map<String, Object> studyInfo;
    private FragmentTransaction transaction;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Intent intent = getIntent();
        studyInfo = (Map<String, Object>) intent.getSerializableExtra("studyGroup");

        // 첫 화면 지정
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, authAttendanceFragment).commitAllowingStateLoss();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navBullet:
                        transaction.replace(R.id.frame_layout, bulletFragment, "BULLET").addToBackStack("BULLET").commit();
                        break;
                    case R.id.navEvaluateMember:
                        transaction.replace(R.id.frame_layout, evaluateMemberFragment, "EVALUATE").addToBackStack("EVALUATE").commit();
                        break;
                    case R.id.navAuthAttendance:
                        transaction.replace(R.id.frame_layout, authAttendanceFragment, "ATTEND").addToBackStack("ATTEND").commit();
                        break;
                    case R.id.navManageSchedule:
                        transaction.replace(R.id.frame_layout, scheduleManagementFragment, "SCHEDULE").addToBackStack("SCHEDULE").commit();
                        break;
                }
                return true;
            }
        });
    }


    public Map<String, Object> getStudyInfo() {
        return this.studyInfo;
    }

    /**
     * fragment 뒤로가기 누를 때 Navigation bar 맞춰줌
     **/

    @Override
    public void onBackPressed() {
        int count = fragmentManager.getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else if (count == 1) {
            fragmentManager.popBackStack();
            bottomNavigationView.getMenu().findItem(R.id.navAuthAttendance).setChecked(true);
        } else if (count >= 2) {
            fragmentManager.popBackStack();

            int index = count - 2;
            FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(index);
            switch (backEntry.getName()) {
                case "BULLET":
                    bottomNavigationView.getMenu().findItem(R.id.navBullet).setChecked(true);
                    break;
                case "EVALUATE":
                    bottomNavigationView.getMenu().findItem(R.id.navEvaluateMember).setChecked(true);
                    break;
                case "ATTEND":
                    bottomNavigationView.getMenu().findItem(R.id.navAuthAttendance).setChecked(true);
                    break;
                case "SCHEDULE":
                    bottomNavigationView.getMenu().findItem(R.id.navManageSchedule).setChecked(true);
                    break;
            }
        }
    }

    /** 액션바 오버라이딩 **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.alarmBell:
                Intent intent1 = new Intent(this, AlarmActivity.class);
                startActivity(intent1);
                return true;
            case R.id.myPage:
                Intent intent2 = new Intent(this, MyPageActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}