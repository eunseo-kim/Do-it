package com.example.study_with_me.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;

public class UserInfo extends AppCompatActivity {

    private TextView nameTextView;
    TextView btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.study_search_user_click_item);
        getSupportActionBar().setTitle("사용자정보");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent= getIntent();

        Log.d("받은이름", intent.getStringExtra("닉네임"));
        String nickName = intent.getStringExtra("닉네임");
        Log.d("닉네임", nickName);

        nameTextView = (TextView) findViewById(R.id.userName);
        nameTextView.setText(nickName);
        btn1 = (TextView) findViewById(R.id.close_button);
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
    }
}
