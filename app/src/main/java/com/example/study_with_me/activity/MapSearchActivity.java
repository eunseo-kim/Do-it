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


public class MapSearchActivity extends AppCompatActivity {
    String BASE_URL= "https://dapi.kakao.com/";
    String API_KEY = "KakaoAK ae9cc095bc96cc24e11a0deaee75a793";

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

        searchKeyword("맛집");
    }

    private void searchKeyword(String keyword){

        KakaoAPI spotInterface =  ApiClient.getApiClient().create(KakaoAPI.class);
        Call<ResultSearchKeyword> call = spotInterface.getSearchKeyword(API_KEY, keyword);

        call.enqueue(new Callback<ResultSearchKeyword>()
        {
            //연결 성공 시에 싱행되는 부분
            @Override
            public void onResponse(@NonNull Call<ResultSearchKeyword> call, @NonNull Response<ResultSearchKeyword> response)
            {

                Log.e("onSuccess", String.valueOf(response.raw()));
                Log.e("onSuccess", String.valueOf(response.body()));

                System.out.println(response.body());
                System.out.println(response.body().getDocuments());

//                String status = response.body().getStatus();
//                System.out.println("안녕"+response.body().getMessage());
            }

            @Override
            public void onFailure(@NonNull Call<ResultSearchKeyword> call, @NonNull Throwable t)
            {
                Log.e("onfail", "에러 = " + t.getMessage());
            }
        });
    }
}