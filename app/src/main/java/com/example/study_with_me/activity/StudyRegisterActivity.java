package com.example.study_with_me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;
import com.example.study_with_me.dialog.StudyRegisterDialog;
import com.example.study_with_me.model.StudyGroup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/** 스터디 등록 액티비티 **/
public class StudyRegisterActivity extends AppCompatActivity {
    /** 스터디 그룹 클래스에 저장할 데이터 **/
    public static ArrayList<StudyGroup> studyGroups = new ArrayList<StudyGroup>();

    private String type;                    // 스터디 종류
    private int numOfMember = 0;            // 스터디 인원
    private Date startDate = new Date();    // 스터디 시작날짜 (오늘)
    private Date endDate;                   // 스터디 종료날짜
    private String studyName;               // 스터디 이름
    private String studyDescription;        // 스터디 설명
    private String userID;
    private String studyGroupID;            // userID + 현재 시각
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference studyGroupRef = databaseReference.child("studygroups");
    private DatabaseReference userRef = databaseReference.child("users");
    FirebaseAuth firebaseAuth;

    public enum etcType {CATEGORY, PEOPLE, DATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_register);

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        /** 스터디 이름, 설명 **/
        EditText name = findViewById(R.id.studyName);
        EditText description = findViewById(R.id.studyDescription);

        /** 상단 바 설정 **/
        getSupportActionBar().setTitle("스터디 등록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /** 스터디 정보 설정 **/
        setStudyInfo();

        Button studyRegisterBtn = findViewById(R.id.studyRegisterBtn);

        /** 스터디 등록 버튼 이벤트 처리 **/
        studyRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studyName = name.getText().toString();
                studyDescription = description.getText().toString();
                if(isValid()) {
                    writeStudyGroup();

                    Intent intent = new Intent(getApplicationContext(), StudyRegisterCompleteActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /** DB에 스터디 정보를 등록하는 함수 **/
    private void writeStudyGroup() {
        long time= System.currentTimeMillis();
        studyGroupID = userID+String.valueOf(time);
        StudyGroup studyGroup = new StudyGroup(studyGroupID, userID, studyName, studyDescription, type, numOfMember, startDate, endDate);
        studyGroupRef.child(studyGroupID).push();
        studyGroupRef.child(studyGroupID).setValue(studyGroup)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "스터디 등록을 완료했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "스터디 등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        addStudyGroupList();
    }

    /** DB의 users-userID-studyGroupList에 생성한 studyGroupID 저장하기 **/
    public void addStudyGroupList() {
        ArrayList<String> studyGroupList = new ArrayList<>();
        studyGroupList.add(studyGroupID);
        userRef.child(userID).child("studyGroupIDList").push().setValue(studyGroupID);
    }

    /** 상단 바 뒤로가기 버튼 처리 **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** 정보가 모두 입력됐는지 확인하는 함수 **/
    private boolean isValid() {
        if(type == null) return false;
        if(numOfMember == 0) return false;
        if(endDate == null) return false;
        if(studyName == null) return false;
        if(studyDescription == null) return false;

        return true;
    }

    /** 기타 버튼 누르면 id 비교해서 각각 팝업창 뜨도록 **/
    public void ectButtonClicked(View view) {
        StudyRegisterDialog dialog;
        switch (view.getId()) {
            case (R.id.ectCategoryButton):
                dialog = new StudyRegisterDialog(this, etcType.CATEGORY);
                dialog.setDialogListener(new StudyRegisterDialog.StudyRegisterDialogListener() {
                    @Override
                    public void onPositiveClicked(String content) {
                        type = content;
                    }
                    @Override
                    public void onNegativeClicked() { }
                });
                dialog.show();
                break;
            case (R.id.ectPeopleCountButton):
                dialog = new StudyRegisterDialog(this, etcType.PEOPLE);
                dialog.setDialogListener(new StudyRegisterDialog.StudyRegisterDialogListener() {
                    @Override
                    public void onPositiveClicked(String content) {
                        try {
                            numOfMember = Integer.valueOf(content);
                        } catch(Exception e) {
                            Toast.makeText(getApplicationContext(), "숫자를 입력해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onNegativeClicked() { }
                });
                dialog.show();
                break;
            case (R.id.ectDateButton):
                dialog = new StudyRegisterDialog(this, etcType.DATE);
                dialog.setDialogListener(new StudyRegisterDialog.StudyRegisterDialogListener() {
                    @Override
                    public void onPositiveClicked(String content) {
                        try {
                            endDate = new SimpleDateFormat("yyyy/MM/dd").parse(content);
                        } catch(Exception e) {
                            Toast.makeText(getApplicationContext(), "형식에 맞춰 입력해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onNegativeClicked() { }
                });
                dialog.show();
                break;
        }

    }

    /** 스터디 분야를 설정하는 함수 **/
    private void setStudyType(RadioGroup studyType) {
        studyType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch(checkedId) {
                    case R.id.studyTypeProgramming:
                        type = "프로그래밍";
                        break;
                    case R.id.studyTypeEmployment:
                        type = "취업";
                        break;
                    case R.id.studyTypeLanguage:
                        type = "어학";
                        break;
                }
            }
        });
    }

    /** 스터디 인원을 설정하는 함수 **/
    private void setStudyNumbOfMember(RadioGroup studyMember) {
        studyMember.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch(checkedId) {
                    case R.id.studyMemberNumTwo:
                        numOfMember = 2;
                        break;
                    case R.id.studyMemberNumThree:
                        numOfMember = 3;
                        break;
                    case R.id.studyMemberNumFour:
                        numOfMember = 4;
                        break;
                }
            }
        });
    }

    /** 스터디 기간을 설정하는 함수 **/
    private void setStudyTerm(RadioGroup studyTerm) {
        studyTerm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                Calendar cal = Calendar.getInstance();
                switch (checkedId) {
                    case R.id.studyTermOneMonth:
                        cal.add(Calendar.MONTH, 1);
                        endDate = cal.getTime();
                        break;
                    case R.id.studyTermTwoMonth:
                        cal.add(Calendar.MONTH, 2);
                        endDate = cal.getTime();
                        break;
                    case R.id.studyTermSixMonth:
                        cal.add(Calendar.MONTH, 6);
                        endDate = cal.getTime();
                        break;
                }
            }
        });
    }

    /** 스터디 정보 설정하는 함수 **/
    private void setStudyInfo() {
        /** 스터디 분야 **/
        RadioGroup studyType = findViewById(R.id.studyType);
        setStudyType(studyType);

        /** 스터디 인원 **/
        RadioGroup studyMember = findViewById(R.id.studyMemberNum);
        setStudyNumbOfMember(studyMember);

        /** 스터디 기간 **/
        RadioGroup studyTerm = findViewById(R.id.studyTerm);
        setStudyTerm(studyTerm);
    }
}
