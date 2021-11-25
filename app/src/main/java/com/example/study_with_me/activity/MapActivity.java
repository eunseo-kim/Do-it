package com.example.study_with_me.activity;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;

import net.daum.mf.map.api.MapView;

public class MapActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_map);

        // 상단 메뉴바
        getSupportActionBar().setTitle("출석 등록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MapView mapView = new MapView(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
    }
}
