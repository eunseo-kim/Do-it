package com.example.study_with_me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.MenuEvaluateMemberFragment;
import com.example.study_with_me.R;
import com.example.study_with_me.adapter.TeamEvaluationAdapter;
import com.example.study_with_me.model.MemberNotification;
import com.example.study_with_me.model.MemberSampledata;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EvaluateMemberActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userRef = databaseReference.child("users");

    private RatingBar evalRatingBar;
    private EditText evalComment;
    private Button evalRegisterBtn;
    private Button evalCancelBtn;

    private String evalUserID;
    private float memberRating;
    private String comment;
    private float existRating;
    private int joinCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluate_member_rating);

        Intent intent = getIntent();
        evalUserID = intent.getStringExtra("userID");

        /** 상단 액션바 설정 **/
        getSupportActionBar().setTitle("팀원 평가");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /** View들 가져오기 **/
        evalRatingBar = findViewById(R.id.evalRatingBar);
        evalComment = findViewById(R.id.evalComment);
        evalRegisterBtn = findViewById(R.id.evalRegisterBtn);
        evalCancelBtn = findViewById(R.id.evalCancelBtn);

        setUserInfo();
        setOnClickListenerEvalRatingBar();
        setOnClickListenerEvalBtns();
    }

    /** 평가하는 user의 스터디 참여 횟수 얻기 **/
    private void setUserInfo() {
        Log.d("userID >>> ", evalUserID);
        userRef.child(evalUserID).child("joinCount").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                joinCount = Integer.parseInt(task.getResult().getValue().toString());
            }
        });
        userRef.child(evalUserID).child("rating").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                existRating = Float.parseFloat(task.getResult().getValue().toString());
            }
        });
    }

    /** DB에 입력한 정보 등록 **/
    private void registerInfoOnDB() {
        userRef.child(evalUserID).child("rating").setValue((memberRating+existRating) / joinCount);
    }

    /** rating bar에 onCilckListener 등록 **/
    private void setOnClickListenerEvalRatingBar() {
        evalRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                memberRating = ratingBar.getRating();
            }
        });
    }

    /** EvaluateMemberActivity에 있는 버튼들에 onClickListener 등록 **/
    private void setOnClickListenerEvalBtns() {
        evalRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerInfoOnDB();

                // 버튼 클릭 불가 처리 구현

                onBackPressed();
            }
        });
        evalCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 뒤로가기
            }
        });
    }

    /** editText에 입력된 평가 가져오기 **/
    private String getComment() {
        return evalComment.getText().toString();
    }
}
