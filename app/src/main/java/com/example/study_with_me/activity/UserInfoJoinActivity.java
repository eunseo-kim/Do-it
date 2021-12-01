package com.example.study_with_me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;
import com.example.study_with_me.adapter.StudyGroupAdapter;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

public class UserInfoJoinActivity extends AppCompatActivity {
    private ArrayList<Map<String, Object>> filteredList = new ArrayList<>();
    private StudyGroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_join_list);
        getSupportActionBar().setTitle("참여 스터디 목록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        myStudyRoomListView = (Listview)findViewById();
    }


//    진행중인 스터디와 종료된 스터디만 보이도록


    /** 액션바 오버라이딩 **/
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

    public void setRadoiClicked() {
        RadioGroup userStudyRadioGroup = (RadioGroup) findViewById(R.id.userStudyRadioGroup);
        switch (userStudyRadioGroup.getCheckedRadioButtonId()) {
            case R.id.started:
//                filterStarted();
                break;
            case R.id.finished:
//                filterFinished();
                break;
        }
    }

    public void setListView(ArrayList<Map<String, Object>> studyGroupList) throws ParseException {
        adapter = new StudyGroupAdapter(this, studyGroupList);
//        userStudyRoomListView.
    }

    // 진행중 스터디 그룹 필터링

    public void filterFinished() throws ParseException {
        filteredList.clear();
//        for() {
//
//        }
        setListView(filteredList);
    }
}
