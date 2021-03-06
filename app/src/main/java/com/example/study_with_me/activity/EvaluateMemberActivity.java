package com.example.study_with_me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EvaluateMemberActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userRef = databaseReference.child("users");
    private DatabaseReference studyRef = databaseReference.child("studygroups");

    private RatingBar evalRatingBar;
    private EditText evalComment;
    private Button evalRegisterBtn;
    private Button evalCancelBtn;

    private String curUserID;
    private String evalUserID;
    private String studyID;
    private String username;
    private float memberRating;
    private float existRating;
    private int ratingCount;
    private ArrayList<String> evalMembers = new ArrayList<>();
    private ArrayList<Map<String, Map>> comments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluate_member_rating);

        firebaseAuth = FirebaseAuth.getInstance();
        curUserID = firebaseAuth.getCurrentUser().getUid();

        Intent intent = getIntent();
        evalUserID = intent.getStringExtra("userID");
        studyID = intent.getStringExtra("studyID");
        username = intent.getStringExtra("username");

        /** ?????? ????????? ?????? **/
        getSupportActionBar().setTitle("?????? ??????");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /** View??? ???????????? **/
        evalRatingBar = findViewById(R.id.evalRatingBar);
        evalComment = findViewById(R.id.evalComment);
        evalRegisterBtn = findViewById(R.id.evalRegisterBtn);
        evalCancelBtn = findViewById(R.id.evalCancelBtn);

        setUserInfo();
        setOnClickListenerEvalRatingBar();
        setOnClickListenerEvalBtns();
    }

    /** ?????? ??? ?????? **/
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** ???????????? user??? ????????? ?????? ?????? ?????? **/
    private void setUserInfo() {
        userRef.child(evalUserID).child("ratingCount").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ratingCount = Integer.parseInt(task.getResult().getValue().toString());
            }
        });
        userRef.child(evalUserID).child("rating").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                existRating = Float.parseFloat(task.getResult().getValue().toString());
            }
        });
    }

    /** DB??? ????????? ?????? ?????? **/
    private void registerInfoOnDB() {
        userRef.child(evalUserID).child("rating").setValue((memberRating + (ratingCount * existRating)) / (ratingCount+1));
    }

    /** rating bar??? onCilckListener ?????? **/
    private void setOnClickListenerEvalRatingBar() {
        evalRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                memberRating = ratingBar.getRating();
            }
        });
    }

    /** EvaluateMemberActivity??? ?????? ???????????? onClickListener ?????? **/
    private void setOnClickListenerEvalBtns() {
        evalRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerInfoOnDB();
                userRef.child(evalUserID).child("ratingCount").setValue(ratingCount+1);
                evalMembers.add(curUserID);
                setEvaluatingMembers();
                setComment();
                finish();
            }
        });
        evalCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /** comments??? comment ?????? **/
    private void setComment() {
        if(evalComment.getText().toString().equals(""))
            return;

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put(username, evalComment.getText().toString());

        Map<String, Map> eComment = new HashMap<>();
        eComment.put(curUserID, userInfo);
        comments.add(eComment);

        userRef.child(evalUserID).child("comments").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().getValue() == null)
                    userRef.child(evalUserID).child("comments").setValue(comments);
                else {
                    ArrayList<Map<String, Map>> commentList = (ArrayList<Map<String, Map>>) task.getResult().getValue();
                    commentList.add(eComment);
                    userRef.child(evalUserID).child("comments").setValue(commentList);
                }
            }
        });
    }

    /** ????????? ???????????? ????????? ?????? **/
    private void setEvaluatingMembers() {
        studyRef.child(studyID).child("evalMembers").child(evalUserID).child("evaluating").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().getValue() == null) {
                    studyRef.child(studyID).child("evalMembers").child(evalUserID).child("evaluating").setValue(evalMembers);
                } else {
                    ArrayList<String> evaluatingMembers = (ArrayList<String>) task.getResult().getValue();
                    evaluatingMembers.add(curUserID);
                    Set<String> evalSet = new HashSet<>(evaluatingMembers);
                    evalMembers = new ArrayList<>(evalSet);
                    studyRef.child(studyID).child("evalMembers").child(evalUserID).child("evaluating").setValue(evalMembers);
                }
            }
        });
    }

}
