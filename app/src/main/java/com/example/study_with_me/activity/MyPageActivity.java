package com.example.study_with_me.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class MyPageActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userRef = databaseReference.child("users");
    private DatabaseReference studyRef = databaseReference.child("studygroups");

    private TextView nameTextView, emailTextView, joinCountTextView, dropCountTextView, ratingNumber;
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

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            currentUserID = firebaseAuth.getCurrentUser().getUid();
        }

        nameTextView = (TextView) findViewById(R.id.userName);
        emailTextView = (TextView)findViewById(R.id.evaluateContents);
        joinCountTextView = (TextView)findViewById(R.id.joinCount);
        dropCountTextView = (TextView)findViewById(R.id.dropCount);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ratingNumber = (TextView) findViewById(R.id.ratingNumber);
        user = FirebaseAuth.getInstance().getCurrentUser();

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
                        userRating = snapshot.child(USER_RATING_KEY).getValue(Float.class) == null ? 0 : snapshot.child(USER_RATING_KEY).getValue(Float.class);
                        joinCount = snapshot.child(USER_JOIN_KEY).getValue(Integer.class) == null ? 0 : snapshot.child(USER_JOIN_KEY).getValue(Integer.class);
                        dropCount = snapshot.child(USER_DROP_KEY).getValue(Integer.class) == null ? 0 : snapshot.child(USER_DROP_KEY).getValue(Integer.class);

                        nameTextView.setText(userName);
                        emailTextView.setText(userEmail);
                        joinCountTextView.setText(String.valueOf(joinCount));
                        dropCountTextView.setText(String.valueOf(dropCount));
                        ratingBar.setRating(userRating);
                        ratingNumber.setText(String.valueOf(userRating));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
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

    /** 로그아웃 **/
    public void signOut(View view) {
        firebaseAuth.signOut();
        Toast.makeText(MyPageActivity.this,"로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SplachActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /** 회원 탈퇴 **/
    public void removeUser(View view) {
        // DB 삭제
        userRef.child(currentUserID).setValue(null);
        String curUid = currentUserID;

        // FirebaseAuth 삭제
        FirebaseUser user = firebaseAuth.getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            removeStudyGroupAboutDeletedUser(curUid);
                            Toast.makeText(MyPageActivity.this,"Do it을 탈퇴합니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MyPageActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void removeStudyGroupAboutDeletedUser(String uid) {
        studyRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Map> map = (Map<String, Map>) task.getResult().getValue();
                for(Map.Entry<String, Map> entry : map.entrySet()) {
                    if(entry.getKey().contains(uid)) {
                        studyRef.child(entry.getKey()).removeValue();
                        continue;
                    }

                    /** memberList에서 해당 유저 삭제 **/
                    Map<String, String> memberList = (Map<String, String>) entry.getValue().get("memberList");
                    for(Map.Entry entry1 : memberList.entrySet()) {
                        if(entry1.getValue().equals(uid))
                            memberList.remove(entry1.getKey());
                    }
                    studyRef.child(entry.getKey()).child("memberList").setValue(memberList);

                    /** applicantList에서 해당 유저 삭제 **/
                    Map<String, Map> applicantList = (Map<String, Map>) entry.getValue().get("applicantList");
                    if(applicantList != null) {
                        for(Map.Entry<String, Map> info : applicantList.entrySet()) {
                            for(Map<String, String> vals : applicantList.values()) {
                                if(vals.get("userID").equals(uid)) {
                                    applicantList.remove(info.getKey());
                                }
                            }
                        }
                    }
                    studyRef.child(entry.getKey()).child("applicantList").setValue(applicantList);
                }
            }
        });
    }

    /** 회원 정보 수정 **/
    public void editUserInfo(View view) {
        Intent intent = new Intent(MyPageActivity.this, EditUserInfoActivity.class);
        startActivity(intent);
    }
}