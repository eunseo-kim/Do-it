package com.example.study_with_me.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.study_with_me.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserInfoActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private TextView nameTextView, emailTextView, joinCountTextView, dropCountTextView, ratingNumber;
    private RatingBar ratingBar;
    private String userName, userEmail;
    private float userRating;
    private int dropCount, joinCount;
    private String applicantUserID;

    TextView btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.study_search_user_click_item);
//        getSupportActionBar().setTitle("회원 정보");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent= getIntent();
        applicantUserID = intent.getStringExtra("userID");
//        Log.d("받은이름>>", intent.getStringExtra("userID"));
        // 회원의 userID를 받아옴
//        userName = intent.getStringExtra("userID");

        nameTextView = (TextView) findViewById(R.id.userName);
        emailTextView = (TextView)findViewById(R.id.evaluateContents);
        joinCountTextView = (TextView)findViewById(R.id.joinCount);
        dropCountTextView = (TextView)findViewById(R.id.dropCount);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ratingNumber = (TextView) findViewById(R.id.ratingNumber);

        setUserInfo();
        buttonClick();
    }
    public void setUserInfo() {
        databaseReference.child("users").child(applicantUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName = snapshot.child("username").getValue(String.class);
                userEmail = snapshot.child("email").getValue(String.class);
                userRating = snapshot.child("rating").getValue(Float.class);
                joinCount = snapshot.child("joinCount").getValue(Integer.class);
                dropCount = snapshot.child("dropCount").getValue(Integer.class);

                getSupportActionBar().setTitle(userName + "님의 정보");

                nameTextView.setText(userName);
                emailTextView.setText(userEmail);
                joinCountTextView.setText(String.valueOf(joinCount));
                dropCountTextView.setText(String.valueOf(dropCount));
                ratingBar.setRating(userRating);
                ratingNumber.setText(String.valueOf(userRating));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void buttonClick() {
        btn1 = (TextView) findViewById(R.id.close_button);
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void userJoinStudyButtonClicked(View view) {
        Intent joinStudyList = new Intent(this, UserInfoJoinActivity.class);
        joinStudyList.putExtra("userID", applicantUserID);
        startActivity(joinStudyList);
    }
    public void teamEvaluateButtonClicked(View view) {
        Intent teamEvaluateList = new Intent(view.getContext(), UserInfoEvaluateActivity.class);
        teamEvaluateList.putExtra("userID", applicantUserID);
        startActivity(teamEvaluateList);
    }
}
