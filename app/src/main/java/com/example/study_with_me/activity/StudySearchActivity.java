package com.example.study_with_me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.study_with_me.R;
import com.example.study_with_me.adapter.SearchAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StudySearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_search_main);

        // 상단 메뉴바
        getSupportActionBar().setTitle("스터디 검색");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listView;
        SearchAdapter adapter;

        adapter = new SearchAdapter();

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        adapter.addItem("모집중", "프로그래밍", "모바일 프로그래밍 성실한 스터디 그룹원 구합니다.", "오늘 04:12");
        adapter.addItem("모집중", "취업", "청주 공기업 NCS스터디 모집합니다.", "2021.10.17");
        adapter.addItem("모집중", "프로그래밍", "코테준비하실분", "2021.10.15");
        adapter.addItem("모집중", "어학", "한기대 TOEIC스터디 하실 분!", "2021.10.13");
        adapter.addItem("모짐중", "프로그래밍", "딥러닝 스터디 구해요~", "2021.10.15");

        EditText editTextFilter = (EditText)findViewById(R.id.editText);
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable edit) {
                String filterText = edit.toString();
                if(filterText.length() > 0) {
                    listView.setFilterText(filterText);
                } else {
                    listView.clearTextFilter();
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


    // 분류하는거 구현해야됨,,
    public void onClick(View v) {
        int viewId = v.getId();

        if(viewId == R.id.all) {
            Toast.makeText(getApplicationContext(),"전체 스터디", Toast.LENGTH_SHORT).show();

        }
        else if(viewId == R.id.programming) {
            Toast.makeText(getApplicationContext(), "프로그래밍만 분류", Toast.LENGTH_SHORT).show();
        }
        else if(viewId == R.id.employ) {
            Toast.makeText(getApplicationContext(), "취업만 분류", Toast.LENGTH_SHORT).show();
        }
        else if(viewId == R.id.language) {
            Toast.makeText(getApplicationContext(), "어학 분류", Toast.LENGTH_SHORT).show();
        }
        else if(viewId == R.id.ect) {
            Toast.makeText(getApplicationContext(), "기타만 분류", Toast.LENGTH_SHORT).show();
        }
        else if (viewId ==  R.id.studyArea) {
            // 각각 글에 맞는 글이 매치되어야 됨!
            Intent intent = new Intent(this, StudyPostActivityMessage.class);
            startActivity(intent);
        }
    }
}
