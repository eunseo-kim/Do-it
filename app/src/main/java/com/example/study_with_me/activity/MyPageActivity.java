package com.example.study_with_me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;

public class MyPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        // 상단 메뉴바
        getSupportActionBar().setTitle("마이페이지");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // myStudyRoomButton 누르면 내 스터디룸으로 이동
        Button myStudyRoomButton = (Button)findViewById(R.id.myStudyRoomButton);
        myStudyRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent studyroom = new Intent(view.getContext(), MyStudyRoomActivity.class);
                startActivity(studyroom);
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