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
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.study_with_me.R;
import com.example.study_with_me.adapter.StudyGroupAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInfoJoinActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference studyGroupRef = databaseReference.child("studygroups");
    private Map<String, String> studyGroupIDMap = new HashMap<>();
    private ArrayList<Map<String, Object>> filteredList = new ArrayList<>();
    private ArrayList<Map<String, Object>> studyGroupList = new ArrayList<>();
    private ArrayList<Map<String, Object>> appliedStudyGroupList = new ArrayList<>();
    private Map<String, String> appliedStudyGroupIDMap = new HashMap<>();
    private DatabaseReference userRef = databaseReference.child("users");
    private ListView myStudyRoomListView;
    private StudyGroupAdapter adapter;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_join_list);
        getSupportActionBar().setTitle("참여 스터디 목록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent= getIntent();
        userID = intent.getStringExtra("userID");

        myStudyRoomListView = (ListView) findViewById(R.id.userJoinStudyListView);
        getAppliedStudyGroups();
        getStudyGroups();
    }

    /** 신청한 스터디 그룹 가져오기 **/
    public void getAppliedStudyGroups() {
        appliedStudyGroupList.clear();
        userRef.child(userID).child("appliedStudyGroupIDList")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().getValue() != null) {
                    appliedStudyGroupIDMap = (Map<String, String>) task.getResult().getValue();
                    long groupsCount = appliedStudyGroupIDMap.size();
                    long currentCount = 0;

                    for(Map.Entry<String, String> entry : appliedStudyGroupIDMap.entrySet()) {
                        currentCount += 1;
                        long temp = currentCount;
                        String studyGroupID = entry.getValue();

                        studyGroupRef.child(studyGroupID).get()
                                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        appliedStudyGroupList.add((Map<String, Object>) task.getResult().getValue());

                                        if(groupsCount == temp) {
                                            try {
                                                Log.d("setRadioClicked", "setRadioClicked");
                                                setRadioClicked();
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                    }
                } else {
                    try {
                        setRadioClicked();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /** 가입한 스터디 그룹 가져오기 **/
    public void getStudyGroups() {
        studyGroupList.clear();

        userRef.child(userID).child("studyGroupIDList")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.getResult().getValue() != null) {
                    studyGroupIDMap = (Map<String, String>) task.getResult().getValue();
                    long groupsCount = studyGroupIDMap.size();
                    long currentCount = 0;

                    for (Map.Entry<String, String> entry : studyGroupIDMap.entrySet()) {
                        currentCount += 1;
                        long temp = currentCount;

                        String studyGroupID = entry.getValue();

                        studyGroupRef.child(studyGroupID).get()
                                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        studyGroupList.add((Map<String, Object>) task.getResult().getValue());

                                        if (groupsCount == temp) {
                                            try {
                                                setRadioClicked();
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                    }
                } else {
                    try {
                        setRadioClicked();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void setRadioClicked() throws ParseException {
        RadioGroup userStudyRadioGroup = (RadioGroup) findViewById(R.id.userStudyRadioGroup);
        switch (userStudyRadioGroup.getCheckedRadioButtonId()) {
            case R.id.started:
                filterStarted();
                break;
            case R.id.finished:
                filterFinished();
                break;
        }
    }

    private void filterStarted() throws ParseException {
        filteredList.clear();
        for (Map<String, Object> sg : studyGroupList) {
            if (sg != null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy.mm.dd");
                Date endDate = format.parse(String.valueOf(sg.get("endDate")));
                Date currDate = new Date();
                if (currDate.before(endDate) && (Boolean) sg.get("closed")) {
                    filteredList.add(sg);
                }
            }
        }
        setListView(filteredList);
    }

    public void setListView(ArrayList<Map<String, Object>> studyGroupList) throws ParseException {
        adapter = new StudyGroupAdapter(this, studyGroupList);
        myStudyRoomListView.setAdapter(adapter);

    }

    public void filterFinished() throws ParseException {
        filteredList.clear();
        for(Map<String, Object> sg: studyGroupList) {
            if(sg != null && sg./**/equals(userID)) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy.mm.dd");
                Date endDate = format.parse(String.valueOf(sg.get("endDate")));
                Date currDate = new Date();
                if(currDate.before(endDate) && !(Boolean) sg.get("close")) {
                    filteredList.add(sg);
                }
            }
        }
        setListView(filteredList);
    }

    public void myStudyRoomTabClicked(View view) throws ParseException {
        setRadioClicked();
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
}
