package com.example.study_with_me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;
import com.example.study_with_me.model.Applicant;
import com.example.study_with_me.model.StudyGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyStudyRoomActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference studyGroupRef = databaseReference.child("studygroups");
    private DatabaseReference userRef = databaseReference.child("users");
    private FirebaseAuth firebaseAuth;
    private String userID;
    private ArrayList<String> studyGroupIDList = new ArrayList<>(); // 사용자가 가입한 스터디그룹 ID 리스트
    private ArrayList<StudyGroup> studyGroupList = new ArrayList<>(); // 사용자가 가입한 스터디그룹 StudyGroup 객체 리스트


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_study_room_main);
        getSupportActionBar().setTitle("내 스터디 목록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            userID = firebaseAuth.getCurrentUser().getUid();
        }

        getStudyGroupIDList();

        View myStudyRoomItem = (View)findViewById(R.id.myStudyRoomItem);
        myStudyRoomItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    /** [1] DB에서 사용자가 가입한 스터디 그룹 ID 리스트(studyGroupIDList) 가져오기 **/
    public void getStudyGroupIDList() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot studygroup : snapshot.child(userID).child("studyGroupIDList").getChildren()) {
                    String studyGroupID = studygroup.getValue(String.class);
                    studyGroupIDList.add(studyGroupID);
                    Log.d("getStudyGroupIDList : ", studyGroupIDList.toString());
                    getStudyGroupList();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    /** [2] DB의 studygroups > studygroupID으로 스터디그룹 데이터를 StudyGroup객체 리스트로 저장하기 **/
    public void getStudyGroupList() {
        for (String studyGroupID : studyGroupIDList) {
            studyGroupRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    DataSnapshot studygroup = snapshot.child(studyGroupID);
                    String name = studygroup.child("name").getValue(String.class);
                    String description = studygroup.child("description").getValue(String.class);
                    String endDate = studygroup.child("endDate").getValue(String.class);
                    String startDate = studygroup.child("startDate").getValue(String.class);
                    String leader = studygroup.child("leader").getValue(String.class);
                    int member = studygroup.child("member").getValue(Integer.class);
                    ArrayList<String> memberList = new ArrayList<>();
                    for (DataSnapshot memberSnapshot : studygroup.child("memberList").getChildren()) {
                        memberList.add(memberSnapshot.getValue(String.class));
                    }
                    String type = studygroup.child("type").getValue(String.class);
                    ArrayList<Applicant> applicants = new ArrayList<>();
                    for (DataSnapshot appSnapshot : studygroup.child("applicantList").getChildren()) {
                        String registerTime = appSnapshot.child("registerTime").getValue(String.class);
                        String studyGroupTitle = appSnapshot.child("studyGroupTitle").getValue(String.class);
                        String userID = appSnapshot.child("userID").getValue(String.class);
                        applicants.add(new Applicant(userID, registerTime, studyGroupTitle));
                    }

                    Log.d("TEST : ", name + "," + description + "," + endDate + "," + startDate
                     + "," + leader + "," + member + "," + memberList + "," + applicants);

                    /* 이제 데이터를 바탕으로 StudyGroup 객체를 만들어야 되는데... memberList와 applicantList를 StudyGroup에서 어떻게 처리하죠? */
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
    }


    /** [3] ArrayList<StudyGroup>를 리스트뷰에 띄우기 **/
    public void setListView() {

    }

    /** 액션바 오버라이딩 **/
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