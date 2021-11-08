package com.example.study_with_me.activity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.study_with_me.R;
import com.example.study_with_me.adapter.ApplicantAdapter;
import com.example.study_with_me.model.Applicant;
import com.example.study_with_me.model.StudyGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// https://github.com/baoyongzhang/SwipeMenuListView 여기 링크 참고해서 만들었습니다!

public class AlarmActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference studyGroupRef = databaseReference.child("studygroups");
    private DatabaseReference userRef = databaseReference.child("users");
    private FirebaseAuth firebaseAuth;
    private String userID, username;
    private ArrayList<Applicant> applicants = new ArrayList<>();
    private ArrayList<String> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);
        // 상단 메뉴바
        getSupportActionBar().setTitle("알림");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            userID = firebaseAuth.getCurrentUser().getUid();
        }

        setUserDataChangedListener();
        setApplicants();
    }

    /* StudyApplicant applicants 알림 화면에 띄워주기 */



    /* DB에서 전체 스터디 그룹 돌면서 => 내가 방장인 스터디그룹의 모든 Applicant 리스트 */
    public void setApplicants() {
        // DB에서 현재 사용자가 방장인 스터디 그룹 가져오기
        studyGroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot studyGroupSnapshot : snapshot.getChildren()) {
                    // studyGroupSnapshot.child("leader")과 userID 비교
                    // 만약 같으면? [applicant list + 스터디 이름] 가져오기
                    if (userID.equals(studyGroupSnapshot.child("leader").getValue(String.class))) {
                        for (DataSnapshot applicantSnapshot : studyGroupSnapshot.child("applicantList").getChildren()) {
                            String studyGroupTitle = applicantSnapshot.child("studyGroupTitle").getValue(String.class);
                            String registerTime = applicantSnapshot.child("registerTime").getValue(String.class);
                            String userID = applicantSnapshot.child("userID").getValue(String.class);
                            applicants.add(new Applicant(userID, registerTime, studyGroupTitle));
                        }
                    }
                }
                setListView();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        });
    }

    private void setUserDataChangedListener() {
        userRef.child(userID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.add(snapshot.child("username").getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    /** userID로 username 가져오기 **/
    public void setUserNameByID(String userID) {
        userRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username = snapshot.child(userID).child("username").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void setListView() {
        SwipeMenuListView swipeMenuListView = (SwipeMenuListView) findViewById(R.id.alarmListView);
        ApplicantAdapter adapter = new ApplicantAdapter(this, applicants, userList);
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
                switch (index) {
                    case 0:
                        Log.d(TAG, "onMenuItemClick: click item");
                        break;
                    case 1:
                        Log.d(TAG, "onMenuItemClick: click item");
                        break;
                }
                // false : close the menu; true : not close the menu
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