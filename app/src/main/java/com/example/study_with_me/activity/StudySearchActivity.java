package com.example.study_with_me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.study_with_me.R;

public class StudySearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_search);

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
    }

    @Override
    // action_bar
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemsSelected(MenuItem item) {
        if(item.getItemId() == R.id.alarmBell) {
            Intent intent = new Intent(StudySearchActivity.this, AlarmActivity.class);
            startActivity(intent);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}

