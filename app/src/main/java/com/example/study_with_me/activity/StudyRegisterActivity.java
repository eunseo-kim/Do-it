package com.example.study_with_me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;
import com.example.study_with_me.dialog.StudyRegisterDialog;
import com.example.study_with_me.model.StudyGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StudyRegisterActivity extends AppCompatActivity {
    /** 스터디 그룹 클래스에 저장할 데이터 **/
    private ArrayList<StudyGroup> studyGroups = new ArrayList<StudyGroup>();
    private String type;                    // 스터디 종류
    private int numOfMember = 0;            // 스터디 인원
    private Date startDate = new Date();    // 스터디 시작날짜 (오늘)
    private Date endDate;                   // 스터디 종료날짜
    private String studyName;               // 스터디 이름
    private String studyDescription;        // 스터디 설명

    public enum etcType {CATEGORY, PEOPLE, DATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_register);
        /** 상단 바 설정 **/
        getSupportActionBar().setTitle("스터디 등록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setStudyInfo();

        Button studyRegisterBtn = findViewById(R.id.studyRegisterBtn);
        studyRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid()) {
                    StudyGroup studyGroup = new StudyGroup(studyName, studyDescription, type, numOfMember, startDate, endDate);
                    studyGroups.add(studyGroup);
                    Intent intent = new Intent(getApplicationContext(), StudyRegisterCompleteActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                        Toast.makeText(getApplicationContext(), "selected", Toast.LENGTH_SHORT).show();
                        type = "Programming";
                        break;
                    case R.id.studyTypeEmployment:
                        type = "Employment";
                        break;
                    case R.id.studyTypeLanguage:
                        type = "Language";
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
                    case R.id.studyMemberNumOne:
                        numOfMember = 1;
                        break;
                    case R.id.studyMemberNumTwo:
                        numOfMember = 2;
                        break;
                    case R.id.studyMemberNumThree:
                        numOfMember = 3;
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
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                        endDate = cal.getTime();
                        break;
                    case R.id.studyTermTwoMonth:
                        cal.add(Calendar.DAY_OF_MONTH, 2);
                        endDate = cal.getTime();
                        break;
                    case R.id.studyTermSixMonth:
                        cal.add(Calendar.DAY_OF_MONTH, 6);
                        endDate = cal.getTime();
                        break;
                }
            }
        });
    }

    /** 스터디 정보 설정하는 함수 **/
    private void setStudyInfo() {
        /** 스터디 이름, 설명 **/
        studyName = findViewById(R.id.studyName).toString();
        studyDescription = findViewById(R.id.studyDescription).toString();

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
