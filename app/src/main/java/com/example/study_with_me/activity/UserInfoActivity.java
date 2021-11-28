// UserInfo.java
package com.example.study_with_me.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UserInfoActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseAuth firebaseAuth;

    private String currentUserID;

    private TextView nameTextView, emailTextView, joinCountTextView, dropCountTextView, ratingNumber;
    private RatingBar ratingBar;
    private String userName, userEmail;
    private float userRating;
    private int dropCount, joinCount;
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
        userName = intent.getStringExtra("닉네임");

        nameTextView = (TextView) findViewById(R.id.userName);
        nameTextView.setText(userName);

        Log.d("!!", databaseReference.child("users").child("username").equalTo(userName).toString());

        databaseReference.child("users").child("username").equalTo(userName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userEmail = snapshot.child("email").getValue(String.class);
                Log.d("이메일", userEmail.toString());
//                userRating = snapshot.child("rating").getValue(Float.class);
//                joinCount = snapshot.child("dropCount").getValue(Integer.class);
//                dropCount = snapshot.child("joinCount").getValue(Integer.class);

//                emailTextView.setText(userEmail);
//                joinCountTextView.setText(String.valueOf(joinCount));
//                dropCountTextView.setText(String.valueOf(dropCount));
//                ratingBar.setRating(userRating);
//                ratingNumber.setText(String.valueOf(userRating));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        databaseReference.child("users").child(currentUserID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
////                Log.d("과연", userName); userName 잘 나옴
//
//                userEmail = snapshot.child("email").getValue(String.class);
//                Log.d("이메일", userEmail);
////                userRating = snapshot.child("rating").getValue(Float.class);
////                joinCount = snapshot.child("dropCount").getValue(Integer.class);
////                dropCount = snapshot.child("joinCount").getValue(Integer.class);
//
////                emailTextView.setText(userEmail);
////                joinCountTextView.setText(String.valueOf(joinCount));
////                dropCountTextView.setText(String.valueOf(dropCount));
////                ratingBar.setRating(userRating);
////                ratingNumber.setText(String.valueOf(userRating));
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });

        btn1 = (TextView) findViewById(R.id.close_button);
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
    }
}
