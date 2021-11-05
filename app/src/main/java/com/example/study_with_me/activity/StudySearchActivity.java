package com.example.study_with_me.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;
import com.example.study_with_me.adapter.SearchAdapter;

public class StudySearchActivity extends AppCompatActivity {
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.study_search_main);

        ListView listView;
        SearchAdapter adapter;

        adapter = new SearchAdapter();

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        adapter.addItem("모집중", "취업", "모바일 프로그래밍 성실한 스터디 그룹원 구합니다.", "오늘 04:12");
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

    }
}

