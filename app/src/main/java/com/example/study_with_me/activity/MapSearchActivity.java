package com.example.study_with_me.activity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.study_with_me.R;
import com.example.study_with_me.adapter.MapAdapter;
import com.example.study_with_me.adapter.SearchAdapter;
import com.example.study_with_me.model.ApiClient;
import com.example.study_with_me.model.KakaoAPI;
import com.example.study_with_me.model.MapItem;
import com.example.study_with_me.model.Place;
import com.example.study_with_me.model.ResultSearchKeyword;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MapSearchActivity extends AppCompatActivity{
    private String BASE_URL= "https://dapi.kakao.com/";
    private String API_KEY = "KakaoAK ae9cc095bc96cc24e11a0deaee75a793";
    private ListView mapSearchListView;
    private ArrayList<MapItem> mapSearchList;
    // private MapView mapView;
    private ViewGroup mapViewContainer;
    private EditText mapSearchEditText;
    private String preText;
    private double x, y; // lat= y, long = x
    private String studyGroupID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_map_search);

        // 상단 메뉴바
        getSupportActionBar().setTitle("지도에서 위치 확인");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        studyGroupID = intent.getStringExtra("studyGroupID");

        // mapView = new MapView(this);
        mapSearchList = new ArrayList<>();
        mapSearchListView = findViewById(R.id.mapSearchListView);
        mapSearchEditText = findViewById(R.id.mapSearchEditText);

        mapSearch();
    }

    private void mapSearch() {
        mapSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence str, int i, int i1, int i2) {
                preText = str.toString();
            }

            @Override
            public void onTextChanged(CharSequence str, int i, int i1, int i2) {
                if(str.toString().equals(preText)) return;

                if(mapSearchEditText.isFocusable() && !str.toString().equals("")) {
                    searchKeyword(str.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void searchKeyword(String keyword){
        KakaoAPI spotInterface =  ApiClient.getApiClient().create(KakaoAPI.class);
        Call<ResultSearchKeyword> call = spotInterface.getSearchKeyword(API_KEY, keyword);
        call.enqueue(new Callback<ResultSearchKeyword>()
        {
            @Override
            public void onResponse(@NonNull Call<ResultSearchKeyword> call, @NonNull Response<ResultSearchKeyword> response)
            {
                Log.e("onSuccess", String.valueOf(response.body()));
                addItemAndMarkers(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ResultSearchKeyword> call, @NonNull Throwable error)
            {
                Log.e("onFail", error.getMessage());
            }
        });
    }

    /** 검색 결과 처리 **/
    private void addItemAndMarkers(ResultSearchKeyword searchResult) {
        if (searchResult != null) {
            mapSearchList.clear();
            for (Place document : searchResult.getDocuments()) {
                MapItem mapItem = new MapItem(document.place_name, document.road_address_name, document.x, document.y);
                mapSearchList.add(mapItem);
            }
            setListView(mapSearchList);
        }
    }

    private void setListView(ArrayList<MapItem> mapSearchList) {
        MapAdapter adapter = new MapAdapter(this, mapSearchList);
        mapSearchListView.setAdapter(adapter);

        mapSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MapItem item = (MapItem) adapter.getItem(position);
                Intent intent = new Intent(MapSearchActivity.this, MapFindActivity.class);
                intent.putExtra("MapItem", (Serializable) item);
                intent.putExtra("studyGroupID", studyGroupID);
                startActivity(intent);
            }
        });
    }
}