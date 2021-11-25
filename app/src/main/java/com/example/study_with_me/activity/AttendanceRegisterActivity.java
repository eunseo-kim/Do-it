package com.example.study_with_me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;

import java.io.Serializable;
import java.util.Map;

public class AttendanceRegisterActivity extends AppCompatActivity {
    private Map<String, Object> attendInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_register);

        // 상단 메뉴바
        getSupportActionBar().setTitle("출석 등록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        attendInfo = (Map<String, Object>) intent.getSerializableExtra("attendInfo");
        Log.d("attendInfo", attendInfo.toString());

        EditText searchView = (EditText) findViewById(R.id.map_search);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MapActivity.class);
                view.getContext().startActivity(intent);
            }
        });
    }
}
