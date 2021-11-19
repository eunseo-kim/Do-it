package com.example.study_with_me.activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.study_with_me.R;
import com.example.study_with_me.adapter.ApplicantAdapter;
import com.example.study_with_me.model.Applicant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class AlarmActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference studyGroupRef = databaseReference.child("studygroups");
    private DatabaseReference userRef = databaseReference.child("users");
    private FirebaseAuth firebaseAuth;
    private ApplicantAdapter adapter;

    private String userID;
    private int joinCount;
    private ArrayList<Applicant> applicants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);

        getSupportActionBar().setTitle("알림");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            userID = firebaseAuth.getCurrentUser().getUid();
        }

        setApplicants();
    }

    private void setJoinCount(String uid) {
        userRef.child(uid).child("joinCount").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                joinCount = Integer.parseInt(task.getResult().getValue().toString());
            }
        });
    }

    private void setApplicants() {
        /** DB에서 현재 사용자가 방장인 스터디 그룹 가져오기 **/
        studyGroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                applicants.clear(); /* 이거 추가하니까 중복 제거됐어요 */
                for (DataSnapshot studyGroupSnapshot : snapshot.getChildren()) {
                    // studyGroupSnapshot.child("leader")과 userID 비교
                    // 만약 같으면? [applicant list + 스터디 이름] 가져오기
                    if (userID.equals(studyGroupSnapshot.child("leader").getValue(String.class))) {
                        Map<String, Object> studyGroup = (Map<String, Object>) studyGroupSnapshot.getValue();

                        if (studyGroup.get("applicantList") != null) {
                            Map<String, Object> applicantMap = (Map<String, Object>)studyGroup.get("applicantList");
                            for (Map.Entry entry : applicantMap.entrySet()) {
                                Map<String, Object> applicant = (Map<String, Object>) entry.getValue();
                                String studyGroupTitle = String.valueOf(applicant.get("studyGroupTitle"));
                                String registerTime = (String) applicant.get("registerTime");
                                String id = (String) applicant.get("userID");
                                String username = applicant.get("userName").toString();
                                String studyGroupID = (String) applicant.get("studyGroupID");
                                applicants.add(new Applicant(id, username, registerTime, studyGroupID, studyGroupTitle));
                            }
                        }
                    }
                }
                Log.d("check applicants size :",String.valueOf(applicants.size()));
                setListView();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void setListView() {
        SwipeMenuListView swipeMenuListView = (SwipeMenuListView) findViewById(R.id.alarmListView);
        adapter = new ApplicantAdapter(this, applicants);
        swipeMenuListView.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0x33)));
                // set item width
                openItem.setWidth(168);
                // set item title
                openItem.setTitle("수락");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0x33, 0xC9,
                        0xCE)));
                // set item width
                deleteItem.setWidth(168);
                // set a icon
                deleteItem.setTitle("거절");
                // add to menu
                deleteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);
            }
        };
        swipeMenuListView.setMenuCreator(creator);

        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Log.d("onMenuItemClick", "onMenuItemClick");
                Applicant applicant = applicants.get(position);
                String appStudyGroupID = applicant.getStudyGroupID();
                String appUserID = applicant.getUserID();
                setJoinCount(appUserID);

                switch (index) {
                    case 0:
                        databaseReference.child("studygroups")
                                .child(appStudyGroupID).child("memberList")
                                .push().setValue(appUserID);

                        databaseReference.child("users")
                                .child(appUserID).child("studyGroupIDList")
                                .push().setValue(appStudyGroupID);

                        databaseReference.child("users")
                                .child(appUserID).child("joinCount")
                                .push().setValue(joinCount+1);
                    default:
                        Query query = databaseReference.child("studygroups")
                                .child(appStudyGroupID).child("applicantList")
                                .orderByChild("userID").equalTo(appUserID);

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot groupSnapShot: snapshot.getChildren()) {
                                    groupSnapShot.getRef().removeValue();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });
                        break;
                }
                applicants.remove(position);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    // 액션바 오버라이딩
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