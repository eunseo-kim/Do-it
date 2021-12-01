package com.example.study_with_me.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.study_with_me.MenuEvaluateMemberFragment;
import com.example.study_with_me.R;
import com.example.study_with_me.adapter.TeamEvaluationAdapter;
import com.example.study_with_me.model.MemberNotification;
import com.example.study_with_me.model.MemberSampledata;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    private String comment;
    private float memberRating;
    private float existRating;
    private int ratingCount;
    private ArrayList<String> evalMembers = new ArrayList<>();
    private ArrayList<Map<String, String>> comments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluate_member_rating);

        firebaseAuth = FirebaseAuth.getInstance();
        curUserID = firebaseAuth.getCurrentUser().getUid();

        Intent intent = getIntent();
        evalUserID = intent.getStringExtra("userID");
        studyID = intent.getStringExtra("studyID");

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

    /** 상단 바 버튼 **/
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /** 평가하는 user의 스터디 참여 횟수 얻기 **/
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

    /** DB에 입력한 정보 등록 **/
    private void registerInfoOnDB() {
        userRef.child(evalUserID).child("rating").setValue((memberRating + (ratingCount * existRating)) / (ratingCount+1));
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

    /** comments에 comment 추가 **/
    private void setComment() {
        if(evalComment.getText().toString().equals(""))
            return;
        Map<String, String> eComment = new HashMap<>();
        eComment.put(curUserID, evalComment.getText().toString());
        comments.add(eComment);

        userRef.child(evalUserID).child("comments").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().getValue() == null)
                    userRef.child(evalUserID).child("comments").setValue(comments);
                else {
                    ArrayList<Map<String, String>> commentList = (ArrayList<Map<String, String>>) task.getResult().getValue();
                    commentList.add(eComment);
                    userRef.child(evalUserID).child("comments").setValue(commentList);
                }
            }
        });
    }

    /** 평가자 리스트에 평가자 추가 **/
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
