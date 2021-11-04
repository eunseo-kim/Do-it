package com.example.study_with_me.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StudySearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_search_main);

        // 상단 메뉴바
        getSupportActionBar().setTitle("스터디 검색");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 펼치기 버튼(expandButton) 클릭 시 필터링 검색 창 펼침
        ImageView expandButton = (ImageView)findViewById(R.id.expandButton);
        LinearLayout filteringScreen = (LinearLayout)findViewById(R.id.filteringScreen);
        expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filteringScreen.getVisibility() == View.VISIBLE) {
                    filteringScreen.setVisibility(View.GONE);
                } else {
                    filteringScreen.setVisibility(View.VISIBLE);
                }
            }
        });

        // floatingActionButton 누르면 스터디 생성 화면
        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StudyRegisterActivity.class);
                startActivity(intent);
            }
        });

        // 스터디 모집글 누르면 StudyPostActivityMessage 창 보이기 (임시로 맨 처음꺼 연결)
        View alarmItem = (View)findViewById(R.id.alarm_item);
        alarmItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StudyPostActivityMessage.class);
                startActivity(intent);
            }
        });
    }

    // 액션바 오버라이딩
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
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
