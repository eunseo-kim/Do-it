package com.example.study_with_me.activity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import com.example.study_with_me.R;
import com.example.study_with_me.model.ApiClient;
import com.example.study_with_me.model.KakaoAPI;
import com.example.study_with_me.model.ResultSearchKeyword;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;


public class MapFindActivity extends AppCompatActivity {
    String BASE_URL= "https://dapi.kakao.com/";
    String API_KEY = "KakaoAK ae9cc095bc96cc24e11a0deaee75a793";

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

        /*서울 시청에 마커 추가*/
        MapPOIItem marker = new MapPOIItem();
        onPOIItemSelected(mapView, marker);
    }

    private void onPOIItemSelected(MapView mapView, MapPOIItem poiItem) {
        MapPOIItem marker = new MapPOIItem();
        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(37.5666805, 126.9784147);
        marker.setItemName("Default Marker");
        marker.setTag(0);
        marker.setMapPoint(MARKER_POINT);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);
    }
}