package com.example.study_with_me.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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



        final Button button1 = (Button) findViewById(R.id.applyButton);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(StudySearchActivity.this);
                dlg.setTitle("스터디를 신청하겠습니까?");
                dlg.setMessage("스터디 수락까지\n시간이 걸릴 수 있습니다.");
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(StudySearchActivity.this, "신청되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.show();
            }
        });
    }
}
