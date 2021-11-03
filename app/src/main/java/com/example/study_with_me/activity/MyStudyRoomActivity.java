package com.example.study_with_me.activity;

import android.os.Bundle;
import android.view.MenuItem;

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

    // onOptionsItemSelected메소드에서 사용자가 선택한 MenuItem객체에 따라 이벤트를 정의합니다.
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.myPage) {
            // R.id.myPage(MyPage icon) 누르면 IntroduceME activity가 실행되도록
            // 명시적 Intent를 정의합니다.
//            Intent intent = new Intent(MainActivity.this, IntroduceMe.class);
//            startActivity(intent);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

}