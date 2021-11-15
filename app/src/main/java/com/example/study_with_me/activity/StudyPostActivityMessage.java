package com.example.study_with_me.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.study_with_me.R;
import com.example.study_with_me.model.Applicant;
import com.example.study_with_me.model.StudyGroup;
import com.example.study_with_me.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StudyPostActivityMessage extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference userRef = databaseReference.child("users");
    DatabaseReference studyGroupRef = databaseReference.child("studygroups");
    Map<String, Object> studyGroupInfo = new HashMap<>();
    private FirebaseAuth firebaseAuth;
    private String userID;
    private String username;
    private Map<String, Object> applicantMap;

    Dialog dialog01;    // custom dialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_post);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            userID = firebaseAuth.getCurrentUser().getUid();
        }

        /** intent 값 받기 (study group에 대한 map 형태의 정보) **/
        studyGroupInfo = (Map) getIntent().getSerializableExtra("studyGroup");
        setUserRefListener(studyGroupInfo);

        dialog01 = new Dialog(StudyPostActivityMessage.this);
        dialog01.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog01.setContentView(R.layout.apply_message);

        Button applyButton = (Button) findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog01();
            }
        });
    }

    private void setUserRefListener(Map<String, Object> studyGroup) {
        userRef.child(String.valueOf(studyGroup.get("leader"))).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.getValue().toString();
                setView(studyGroup, username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void setView(Map<String, Object> studyGroup, String writerName) {
        TextView writer = findViewById(R.id.studySearchWriter);
        TextView title = findViewById(R.id.studySearchTitle);
        TextView type = findViewById(R.id.studySearchType);
        TextView numOfMember = findViewById(R.id.studySearchMember);
        TextView endDate = findViewById(R.id.studySearchEndDate);
        TextView description = findViewById(R.id.studySearchDescription);

        writer.setText(writerName);
        title.setText(String.valueOf(studyGroup.get("name")));
        type.setText(String.valueOf(studyGroup.get("type")));
        numOfMember.setText(String.valueOf(studyGroup.get("member")));
        endDate.setText(String.valueOf(studyGroup.get("endDate")) + " 까지");
        description.setText(String.valueOf(studyGroup.get("description")));
    }

    public void showDialog01() {
        dialog01.show();

        Button noButton = dialog01.findViewById(R.id.noButton);
        noButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        dialog01.findViewById(R.id.yesButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean duplicate = false; // 중복된 신청자인지
                Map<String, Object> studyGroup = (Map<String, Object>) studyGroupInfo;
                Log.d("aa", studyGroup.toString());
                String studyGroupID = (String) studyGroup.get("studyGroupID");

                if (studyGroup.get("applicantList") != null) {
                    applicantMap = (Map<String, Object>)studyGroup.get("applicantList");
                    for (Map.Entry entry : applicantMap.entrySet()) {
                        Map<String, Object> map = (Map<String, Object>) entry.getValue();
                        if (map.get("userID").equals(userID)) {
                            Toast.makeText(getApplicationContext(), "이미 가입된 스터디입니다.", Toast.LENGTH_SHORT).show();
                            duplicate = true;
                            break;
                        }
                    }
                } else {
                    applicantMap = new HashMap<>();
                }

                if(!duplicate) {
                    userRef.child(userID).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            username = String.valueOf(task.getResult().getValue());
                            long now = System.currentTimeMillis();
                            Date date = new Date(now);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd hh:mm");
                            String registerTime = dateFormat.format(date);

                            Applicant newApplicant = new Applicant(userID, username, registerTime,
                                    studyGroupID, String.valueOf(studyGroup.get("name")));
                            studyGroupRef.child(studyGroupID).child("applicantList").push().setValue(newApplicant);
                            Toast.makeText(getApplicationContext(), "신청되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }
}