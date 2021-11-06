package com.example.study_with_me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;
import com.example.study_with_me.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyPageActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private Intent intent;
    private TextView nameTextView, emailTextView, joinCountTextView, dropCountTextView;
    private RatingBar ratingBar;
    private String currentUserID;

    private String userName, userEmail;
    private float userRating;
    private int dropCount, joinCount;
    final String USER_NAME_KEY = "username";
    final String USER_EMAIL_KEY = "email";
    final String USER_RATING_KEY = "rating";
    final String USER_DROP_KEY = "dropCount";
    final String USER_JOIN_KEY = "joinCount";

    private FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        // 상단 메뉴바
        getSupportActionBar().setTitle("마이페이지");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameTextView = (TextView) findViewById(R.id.userName);
        emailTextView = (TextView)findViewById(R.id.userEmail);
        joinCountTextView = (TextView)findViewById(R.id.joinCount);
        dropCountTextView = (TextView)findViewById(R.id.dropCount);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        user = FirebaseAuth.getInstance().getCurrentUser();

        // 로그인 화면으로부터 currentUserID 인텐트로 데이터 받기
        intent = getIntent();
        currentUserID = intent.getStringExtra("userID");
        Log.d("tag", "currentUserID : " + currentUserID);

        setUserInfo();
    }

    // currentUserID로 현재 사용자 정보 가져와서 마이페이지에 띄우기
    public void setUserInfo() {
        databaseReference.child("users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName = snapshot.child(USER_NAME_KEY).getValue(String.class);
                userEmail = snapshot.child(USER_EMAIL_KEY).getValue(String.class);
                userRating = snapshot.child(USER_RATING_KEY).getValue(Float.class);
                joinCount = snapshot.child(USER_JOIN_KEY).getValue(Integer.class);
                dropCount = snapshot.child(USER_DROP_KEY).getValue(Integer.class);

                nameTextView.setText(userName);
                emailTextView.setText(userEmail);
                joinCountTextView.setText(String.valueOf(joinCount));
                dropCountTextView.setText(String.valueOf(dropCount));
                ratingBar.setRating(userRating);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        });
    }

    public void myStudyRoomButtonClicked(View view) {
        Intent studyroom = new Intent(view.getContext(), MyStudyRoomActivity.class);
        startActivity(studyroom);
    }

    // 액션바 오버라이딩
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.alarmBell:
                Intent intent1 = new Intent(this, AlarmActivity.class);
                startActivity(intent1);
                return true;
            case R.id.myPage:
                Intent intent2 = new Intent(this, MyPageActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}