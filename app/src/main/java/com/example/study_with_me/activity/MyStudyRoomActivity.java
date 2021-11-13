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
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;
import com.example.study_with_me.adapter.SearchAdapter;
import com.example.study_with_me.adapter.StudyGroupAdapter;
import com.example.study_with_me.model.Applicant;
import com.example.study_with_me.model.StudyGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MyStudyRoomActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference studyGroupRef = databaseReference.child("studygroups");
    private DatabaseReference userRef = databaseReference.child("users");
    private FirebaseAuth firebaseAuth;
    private String userID;
    private ArrayList<String> studyGroupIDList = new ArrayList<>(); // 사용자가 가입한 스터디그룹 ID 리스트
    private ArrayList<Map<String, Object>> studyGroupList = new ArrayList<>(); // 사용자가 가입한 스터디그룹 StudyGroup 객체 리스트
    private ListView myStudyRoomListView;

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

        myStudyRoomListView = (ListView)findViewById(R.id.myStudyRoomListView);

        getStudyGroupIDList();

        /* View myStudyRoomItem = (View)findViewById(R.id.myStudyRoomItem);
        myStudyRoomItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        }); */
    }

    /** [1] DB에서 사용자가 가입한 스터디 그룹 ID 리스트(studyGroupIDList) 가져오기 **/
    public void getStudyGroupIDList() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot studygroup : snapshot.child(userID).child("studyGroupIDList").getChildren()) {
                    String studyGroupID = studygroup.getValue(String.class);
                    studyGroupIDList.add(studyGroupID);
                }
                Log.d("getStudyGroupIDList : ", studyGroupIDList.toString());
                getStudyGroupList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void getStudyGroupList() {
        studyGroupRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue() != null) {
                            for (String id : studyGroupIDList) {
                                collectStudyGroupList(id, (Map<String, Object>) snapshot.getValue());
                            }
                            Log.d("studyList >>> ", studyGroupList.toString());
                            setListView();
                        }
                    }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }


    private void collectStudyGroupList(String id, Map<String, Object> studygroups) {
        for(Map.Entry<String, Object> entry : studygroups.entrySet()) {
            Map singleStudyGroup = (Map) entry.getValue();
            if (entry.getKey().equals(id)) {
                studyGroupList.add(singleStudyGroup);
                break;
            }
        }
    }

    public void setListView() {
        StudyGroupAdapter adapter = new StudyGroupAdapter(this, studyGroupList);
        myStudyRoomListView.setAdapter(adapter);
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