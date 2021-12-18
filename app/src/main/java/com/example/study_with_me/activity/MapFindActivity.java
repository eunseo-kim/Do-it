package com.example.study_with_me.activity;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.study_with_me.R;
import com.example.study_with_me.model.MapItem;

import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class MapFindActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener{
    String BASE_URL= "https://dapi.kakao.com/";
    String API_KEY = "KakaoAK ae9cc095bc96cc24e11a0deaee75a793";
    private double latitude, longitude;
    private MapItem mapItem;
    private MapView mapView;
    private TextView placeName, placeRoad;
    private EditText locationRange;
    private Map<String, Object> attendInfo;
    private Button registerButton;
    private String studyGroupID;
    private MapPoint MARKER_POINT;
    private String preText;
    private int ATTENDACE_RANGE = 100;

    /*현재 위치 GPS 권한 설정*/
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_map_find);

        // 상단 메뉴바
        getSupportActionBar().setTitle("지도에서 위치 확인");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        placeName = findViewById(R.id.placeName);
        placeRoad = findViewById(R.id.placeRoad);
        locationRange = findViewById(R.id.locationRange);

        mapView = new MapView(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        Intent intent = getIntent();
        mapItem = (MapItem) intent.getSerializableExtra("MapItem");
        studyGroupID = intent.getStringExtra("studyGroupID");
        placeName.setText(mapItem.place_name);
        placeRoad.setText(mapItem.road_address_name);


        latitude = Double.valueOf(mapItem.x);
        longitude = Double.valueOf(mapItem.y);
        MARKER_POINT = MapPoint.mapPointWithGeoCoord(longitude, latitude);
        setMapView(MARKER_POINT);
        setMapPOIItem(MARKER_POINT);

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerButtonClicked();
            }
        });

        /* draw a MapCircle */
        locationRange.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
                preText = s.toString();
            }
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                mapView.removeAllCircles();

                if (locationRange.isFocusable() && !s.toString().equals("")) {
                    int range = Integer.parseInt(locationRange.getText().toString());
                    /* 범위 제한 (50m) => 일단 확인용으로 500m로 할게요! */
                    if (range > ATTENDACE_RANGE) {
                        range = ATTENDACE_RANGE;
                        locationRange.setText(String.valueOf(range));
                        Toast.makeText(getApplicationContext(), "최대 100m까지 설정 가능합니다.", Toast.LENGTH_SHORT).show();
                    }
                    MapCircle mapCircle = new MapCircle(MARKER_POINT, range,R.color.strokeColor, R.color.fillColor);
                    mapView.addCircle(mapCircle);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    /* 지도 마커를 MARKER_POINT로 */
    private void setMapPOIItem(MapPoint MARKER_POINT) {
        MapPOIItem marker = new MapPOIItem();
        Log.d("marker name", marker.toString());
        marker.setItemName(mapItem.place_name);
        marker.setTag(0);
        marker.setMapPoint(MARKER_POINT);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView.addPOIItem(marker);
    }

    /* 지도 view 중심을 MARKER_POINT로 */
    private void setMapView(MapPoint MARKER_POINT) {
        mapView.setMapCenterPoint(MARKER_POINT, true);
    }


    /* AttendanceRegisterActivity로 x, y, place, range 전달 */
    public void registerButtonClicked() {
        if (locationRange.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "출석 인정 범위를 입력하세요", Toast.LENGTH_SHORT).show();
        } else {
            attendInfo = new HashMap<>();
            attendInfo.put("hour", "");
            attendInfo.put("minute", "");
            attendInfo.put("x", mapItem.x);
            attendInfo.put("y", mapItem.y);
            attendInfo.put("place", mapItem.place_name);
            attendInfo.put("range", locationRange.getText());

            Intent intent = new Intent(MapFindActivity.this, AttendanceRegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("attendInfo", (Serializable)attendInfo);
            intent.putExtra("studyGroupID", studyGroupID);
            startActivity(intent);
        }
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
        longitude = mapPointGeo.longitude;
        latitude = mapPointGeo.latitude;
        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(longitude, latitude);
        setMapView(MARKER_POINT);
        setMapPOIItem(MARKER_POINT);
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }
}