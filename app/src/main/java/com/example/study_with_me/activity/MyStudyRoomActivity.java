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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import java.io.Serializable;
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
    private ArrayList<Map<String, Object>> filteredList = new ArrayList<>(); // 필터링 된 리스트
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
    }

    public void myStudyRoomTabClicked(View view) throws ParseException {
        filteredList.clear();

        switch (view.getId()) {
            case R.id.all:
                filteredList = (ArrayList<Map<String, Object>>) studyGroupList.clone();
                break;
            case R.id.started:
                for (Map<String, Object> sg : studyGroupList) {
                    // closed && not finished
                    SimpleDateFormat format = new SimpleDateFormat("yyyy.mm.dd");
                    Date endDate = format.parse(String.valueOf(sg.get("endDate")));
                    Date currDate = new Date();
                    if (currDate.before(endDate) && (Boolean)sg.get("closed")) {
                        filteredList.add(sg);
                    }
                }
                break;
            case R.id.waiting:
                /*스터디그룹의 applicants에 나의 userID 있으면 추가하기*/
                for (Map<String, Object> sg : studyGroupList) {
                    ArrayList<Applicant>  applicantList = (ArrayList<Applicant>) sg.get("applicantList");
                    if (applicantList.contains(userID)) {
                        filteredList.add(sg);
                    }
                }
                break;
            case R.id.closing:
                for (Map<String, Object> sg : studyGroupList) {
                    if(!(Boolean)sg.get("closed")) {
                        filteredList.add(sg);
                    }
                }
                break;
            case R.id.finished:
                // 현재 날짜가 endDate를 지났다면 추가하기
                for (Map<String, Object> sg : studyGroupList) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy.mm.dd");
                    Date endDate = format.parse(String.valueOf(sg.get("endDate")));
                    Date currDate = new Date();
                    if (endDate.before(currDate)) {
                        filteredList.add(sg);
                    }
                }
                break;
        }

        setListView(filteredList);
    }

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
                            // 처음 실행하면 자동으로 [전체] 탭 선택됨 => 모든 studyGroupList 보여주기
                            setListView(studyGroupList);
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

    public void setListView(ArrayList<Map<String, Object>> list) {
        StudyGroupAdapter adapter = new StudyGroupAdapter(this, list);
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


        /** 길게 누르면 마감 버튼 or 신청 취소 or 삭제 버튼 **/
        myStudyRoomListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> studyGroup = (Map<String, Object>) adapter.getItem(position);

                /*현재 탭이 [마감 설정]이면 longClick 했을 때 마감여부 Dialog 보여줌*/
                RadioGroup myStudyRadioGroup = (RadioGroup)findViewById(R.id.myStudyRadioGroup);
                switch (myStudyRadioGroup.getCheckedRadioButtonId()) {
                    case R.id.waiting: // 대기 중
                        /**신청 취소 AlertDialog**/
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MyStudyRoomActivity.this)
                                .setTitle("신청을 취소하시겠습니까?")
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // 신청 취소 하면 해당 스터디그룹 applicantList에서 현재 사용자 빼기
                                        Toast.makeText(getApplicationContext(), "신청이 취소 되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        AlertDialog dialog1 = builder1.create();
                        dialog1.show();

                        break;
                    case R.id.closing: // 마감 설정
                        /**스터디 마감 AlertDialog**/
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(MyStudyRoomActivity.this)
                                .setTitle("스터디 모집을 마감하시겠습니까?")
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // 신청 취소 하면 해당 스터디그룹 applicantList에서 현재 사용자 빼기
                                    }
                                })
                                .setNegativeButton("아니오", null);
                        AlertDialog dialog2 = builder2.create();
                        dialog2.show();

                        break;
                }
                return true;
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