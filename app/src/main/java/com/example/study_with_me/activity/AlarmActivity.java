package com.example.study_with_me.activity;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.study_with_me.R;

import java.util.ArrayList;

public class AlarmActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.alarm_new);

        // 상단 메뉴바
        getSupportActionBar().setTitle("알림");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.listView);

        ArrayList<String> list = new ArrayList<>();
        list.add("'사용자 별명' 님이 스터디 가입을 신청합니다.");
        list.add("'사용자 별명' 님이 스터디 가입을 신청합니다.");
        list.add("'사용자 별명' 님이 스터디 가입을 신청합니다.");

        ArrayAdapter adapter = new ArrayAdapter(AlarmActivity.this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0x33)));
                // set item width
                openItem.setWidth(168);
                // set item title
                openItem.setTitle("수락");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0x33, 0xC9,
                        0xCE)));
                // set item width
                deleteItem.setWidth(168);
                // set a icon
                deleteItem.setTitle("거절");
                // add to menu
                deleteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Log.d(TAG, "onMenuItemClick: click item");
                        break;
                    case 1:
                        Log.d(TAG, "onMenuItemClick: click item");
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
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