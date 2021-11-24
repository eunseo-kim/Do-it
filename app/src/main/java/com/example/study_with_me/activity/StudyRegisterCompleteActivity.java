package com.example.study_with_me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;
import com.google.firebase.auth.FirebaseAuth;

/** 스터디 등록 완료 액티비티 **/
public class StudyRegisterCompleteActivity extends AppCompatActivity {
    private String userID;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_creation_complete);

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        Button myPageGoBtn = findViewById(R.id.myPageGoBtn);

        /** 마이페이지로 이동 버튼 클릭이벤트 **/
        myPageGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                startActivity(intent);
            }
        });
    }
}
