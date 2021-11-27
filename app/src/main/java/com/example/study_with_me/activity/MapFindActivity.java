package com.example.study_with_me.activity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import com.example.study_with_me.R;
import com.example.study_with_me.model.ApiClient;
import com.example.study_with_me.model.KakaoAPI;
import com.example.study_with_me.model.MapItem;
import com.example.study_with_me.model.ResultSearchKeyword;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.Map;


public class MapFindActivity extends AppCompatActivity {
    String BASE_URL= "https://dapi.kakao.com/";
    String API_KEY = "KakaoAK ae9cc095bc96cc24e11a0deaee75a793";
    MapItem mapItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_map);

        // 상단 메뉴바
        getSupportActionBar().setTitle("지도에서 위치 확인");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MapView mapView = new MapView(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        Intent intent = getIntent();
        mapItem = (MapItem) intent.getSerializableExtra("MapItem");
        onPOIItemSelected(mapView, mapItem);
    }

    private void onPOIItemSelected(MapView mapView, MapItem mapItem) {
        MapPOIItem marker = new MapPOIItem();
        double latitude = Double.valueOf(mapItem.x);
        double longitude = Double.valueOf(mapItem.y);
        Log.d("위치", latitude + ", " + longitude);
        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(longitude, latitude);

        /* 지도 view 중심을 검색 결과로 */
        mapView.setMapCenterPoint(MARKER_POINT, true);

        /* 지도 마커 표시 */
        marker.setItemName(mapItem.place_name);
        marker.setTag(0);
        marker.setMapPoint(MARKER_POINT);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView.addPOIItem(marker);
    }
}