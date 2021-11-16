package com.example.study_with_me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;
import com.example.study_with_me.adapter.StudyGroupAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyStudyRoomActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference studyGroupRef = databaseReference.child("studygroups");
    private DatabaseReference userRef = databaseReference.child("users");
    private FirebaseAuth firebaseAuth;
    private String userID;
    private Map<String, String> studyGroupIDMap = new HashMap<>();
    private Map<String, String> appliedStudyGroupIDMap = new HashMap<>();
    private ArrayList<Map<String, Object>> studyGroupList = new ArrayList<>();
    private ArrayList<Map<String, Object>> appliedStudyGroupList = new ArrayList<>();
    private ArrayList<Map<String, Object>> filteredList = new ArrayList<>();
    private Map<String, Object> singleStudyGroup;
    private ListView myStudyRoomListView;
    private StudyGroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_study_room_main);
        getSupportActionBar().setTitle("내 스터디 목록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        myStudyRoomListView = (ListView)findViewById(R.id.myStudyRoomListView);

        getAppliedStudyGroups();
        getStudyGroups();
    }

    /** 신청한 스터디 그룹 가져오기 **/
    public void getAppliedStudyGroups() {
        userRef.child(userID).child("appliedStudyGroupIDList")
            .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                appliedStudyGroupIDMap = (Map<String, String>) task.getResult().getValue();
                for(Map.Entry<String, String> entry : appliedStudyGroupIDMap.entrySet()) {
                    String studyGroupID = entry.getValue();

                    studyGroupRef.child(studyGroupID).get()
                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            appliedStudyGroupList.add((Map<String, Object>)task.getResult().getValue());
                        }
                    });
                }
            }
        });
    }



    /** 가입한 스터디 그룹 가져오기 **/
    public void getStudyGroups() {
        userRef.child(userID).child("studyGroupIDList")
            .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                studyGroupIDMap = (Map<String, String>) task.getResult().getValue();
                for(Map.Entry<String, String> entry : studyGroupIDMap.entrySet()) {
                    String studyGroupID = entry.getValue();

                    studyGroupRef.child(studyGroupID).get()
                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            studyGroupList.add((Map<String, Object>)task.getResult().getValue());
                        }
                    });
                }
            }
        });
    }

    public void setListView(ArrayList<Map<String, Object>> studyGroupList) throws ParseException {
        adapter = new StudyGroupAdapter(this, studyGroupList);
        myStudyRoomListView.setAdapter(adapter);
        myStudyRoomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Map<String, Object> item = (Map<String, Object>) adapter.getItem(position);
                Intent intent = new Intent(MyStudyRoomActivity.this, MainActivity.class);
                intent.putExtra("studyGroup", (Serializable) item);
                startActivity(intent);
            }
        });
    }
    /**진행중 스터디 그룹 필터링**/
    public void filterStarted() throws ParseException {
        filteredList.clear();
        for (Map<String, Object> sg : studyGroupList) {
            // closed && not finished
            SimpleDateFormat format = new SimpleDateFormat("yyyy.mm.dd");
            Date endDate = format.parse(String.valueOf(sg.get("endDate")));
            Date currDate = new Date();
            if (currDate.before(endDate) && (Boolean)sg.get("closed")) {
                filteredList.add(sg);
            }
        }
        setListView(filteredList);
    }

    /**대기중 스터디 그룹 필터링**/
    public void filterWaiting() throws ParseException {
        setListView(appliedStudyGroupList);
    }


    /**마감설정 스터디 그룹 필터링**/
    public void filterClosing() throws ParseException {
        filteredList.clear();
        for (Map<String, Object> sg : studyGroupList) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy.mm.dd");
            Date endDate = format.parse(String.valueOf(sg.get("endDate")));
            Date currDate = new Date();
            if (currDate.before(endDate)&&!(Boolean)sg.get("closed")) {
                filteredList.add(sg);
            }
        }
        setListView(filteredList);
    }


    /**종료됨 스터디 그룹 필터링**/
    public void filterFinished() throws ParseException {
        filteredList.clear();
        for (Map<String, Object> sg : studyGroupList) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy.mm.dd");
            Date endDate = format.parse(String.valueOf(sg.get("endDate")));
            Date currDate = new Date();
            if (endDate.before(currDate)) {
                filteredList.add(sg);
            }
        }
        setListView(filteredList);
    }

    public void myStudyRoomTabClicked(View view) throws ParseException {
        switch (view.getId()) {
            case R.id.started:
                filterStarted();
                break;
            case R.id.waiting:
                filterWaiting();
                break;
            case R.id.closing:
                filterClosing();
                break;
            case R.id.finished:
                filterFinished();
                break;
        }
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