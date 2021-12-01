package com.example.study_with_me.activity;

import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;
import com.example.study_with_me.adapter.StudyGroupAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    private ListView myStudyRoomListView;
    private StudyGroupAdapter adapter;
    private int joinCount;

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
        cancelOrClose(); /*[마감설정-길게클릭-마감 팝업]*/


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
        RadioGroup myStudyRadioGroup = (RadioGroup)findViewById(R.id.myStudyRadioGroup);
        switch (myStudyRadioGroup.getCheckedRadioButtonId()) {
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

    /**대기중 스터디 그룹 필터링**/
    public void filterWaiting() throws ParseException {
        setListView(appliedStudyGroupList);
    }

    /**마감설정 스터디 그룹 필터링**/
    public void filterClosing() throws ParseException {
        filteredList.clear();
        for (Map<String, Object> sg : studyGroupList) {
            if (sg != null && sg.get("leader").equals(userID)) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy.mm.dd");
                Date endDate = format.parse(String.valueOf(sg.get("endDate")));
                Date currDate = new Date();
                if (currDate.before(endDate) && !(Boolean) sg.get("closed")) {
                    filteredList.add(sg);
                }
            }
        }
        setListView(filteredList);
    }

    /**종료됨 스터디 그룹 필터링**/
    public void filterFinished() throws ParseException {
        filteredList.clear();
        for (Map<String, Object> sg : studyGroupList) {
            if (sg != null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy.mm.dd");
                Date endDate = format.parse(String.valueOf(sg.get("endDate")));
                Date currDate = new Date();
                if (endDate.before(currDate)) {
                    filteredList.add(sg);
                }
            }
        }
        setListView(filteredList);
    }

    public void myStudyRoomTabClicked(View view) throws ParseException {
        setRadioClicked();
    }

    /** 마감설정 탭에서 listView 길게 클릭 시 마감버튼 팝업 **/
    public void cancelOrClose() {
        myStudyRoomListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> studyGroup = (Map<String, Object>) adapter.getItem(position);
                RadioGroup myStudyRadioGroup = (RadioGroup)findViewById(R.id.myStudyRadioGroup);
                if(myStudyRadioGroup.getCheckedRadioButtonId() == R.id.closing) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyStudyRoomActivity.this)
                    .setTitle("스터디 모집을 마감하시겠습니까?")
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(), "스터디 모집을 마감합니다.", Toast.LENGTH_SHORT).show();
                            String studyGroupID = (String)studyGroup.get("studyGroupID");

                            /* studyGroup의 closed=true */
                            studyGroupRef.child(studyGroupID).child("closed").setValue(true);

                            /* memberList의 각 memberID의 joinCount + 1 */
                            studyGroupRef.child(studyGroupID).child("memberList")
                                    .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot members : snapshot.getChildren()) {
                                        String memberID = members.getValue(String.class);
                                        userRef.child(memberID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                joinCount = Integer.parseInt(task.getResult()
                                                        .child("joinCount").getValue().toString());
                                                userRef.child(memberID).child("joinCount").setValue(joinCount+1);

                                                /*모집 마감을 하면 memberLIst의 각 user DB에 attendance 추가하기*/
                                                Map<String, Object> attendanceMap = new HashMap<>();
                                                attendanceMap.put("isSet", false);
                                                attendanceMap.put("attend", false);
                                                attendanceMap.put("hour", "");
                                                attendanceMap.put("minute", "");
                                                attendanceMap.put("x", "");
                                                attendanceMap.put("y", "");
                                                attendanceMap.put("place", "");
                                                attendanceMap.put("range", "");
                                                attendanceMap.put("dates", "");
                                                userRef.child(memberID).child("attendance").child(studyGroupID).setValue(attendanceMap);
                                            }
                                        });
                                    }
                                    getStudyGroups();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {}
                            });
                        }
                    })
                    .setNegativeButton("아니오", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else if (myStudyRadioGroup.getCheckedRadioButtonId() == R.id.waiting) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyStudyRoomActivity.this)
                    .setTitle("스터디 신청을 취소하시겠습니까?")
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(), "스터디 신청을 취소합니다.", Toast.LENGTH_SHORT).show();

                            /*[Users-UserID-appliedStudyGroupIDList]에서 삭제*/
                            Query userAppliedStudyQuery = userRef.child(userID)
                                    .child("appliedStudyGroupIDList")
                                    .orderByValue()
                                    .equalTo(String.valueOf(studyGroup.get("studyGroupID")));

                            userAppliedStudyQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                                        userSnapshot.getRef().removeValue();
                                    }
                                    /*[studygroups-studyGroupID-applicantList]에서 삭제*/
                                    Query studyGroupApplicantQuery = studyGroupRef
                                            .child(String.valueOf(studyGroup.get("studyGroupID")))
                                            .child("applicantList")
                                            .orderByChild("userID")
                                            .equalTo(userID);

                                    studyGroupApplicantQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot studySnapshot: snapshot.getChildren()) {
                                                studySnapshot.getRef().removeValue();
                                                getAppliedStudyGroups();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {}
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {}
                            });
                        }
                    })
                    .setNegativeButton("아니오", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return false;
            }
        });
    }

    /** 대기중 탭에서 listView 길게 클릭 시 신청 취소 팝업 **/
    public void cancelApplication() {
        myStudyRoomListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> studyGroup = (Map<String, Object>) adapter.getItem(position);
                RadioGroup myStudyRadioGroup = (RadioGroup)findViewById(R.id.myStudyRadioGroup);
                if(myStudyRadioGroup.getCheckedRadioButtonId() == R.id.waiting) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyStudyRoomActivity.this)
                    .setTitle("스터디 신청을 취소하시겠습니까?")
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(), "스터디 신청을 취소합니다.", Toast.LENGTH_SHORT).show();

                            /*[Users-UserID-appliedStudyGroupIDList]에서 삭제*/
                            Query userAppliedStudyQuery = userRef.child(userID)
                                    .child("appliedStudyGroupIDList")
                                    .orderByValue()
                                    .equalTo(String.valueOf(studyGroup.get("studyGroupID")));

                            userAppliedStudyQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                                        userSnapshot.getRef().removeValue();
                                    }
                                    getAppliedStudyGroups();

                                    /*[studygroups-studyGroupID-applicantList]에서 삭제*/
                                    Query studyGroupApplicantQuery = studyGroupRef
                                            .child(String.valueOf(studyGroup.get("studyGroupID")))
                                            .child("applicantList")
                                            .orderByChild("userID")
                                            .equalTo(userID);

                                    studyGroupApplicantQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot studySnapshot: snapshot.getChildren()) {
                                                studySnapshot.getRef().removeValue();
                                                Log.d("DELETE~", "DELETE");
                                            }
                                            Log.d("PRINT~", "PRINT");
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {}
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {}
                            });
                        }
                    })
                    .setNegativeButton("아니오", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return false;
            }
        });
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