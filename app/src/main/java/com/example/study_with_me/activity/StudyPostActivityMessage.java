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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
                // studyGroup => StudyGroup 객체
                // 현재 가입하려는 studyGroup의 id -> studyGroup.getStudyGroupID()
                // 로그인된 사용자 id -> userID
                Map<String, Object> studyGroup = (Map<String, Object>) studyGroupInfo;
                Log.d("aa", studyGroup.toString());
                String studyGroupID = (String) studyGroup.get("studyGroupID");

                ArrayList<Applicant> applicantList = (ArrayList<Applicant>)studyGroup.get("applicantList");
                if (applicantList != null) {
                    for (int i = 0; i < applicantList.size(); i++) {
                        // - applicantList의 i번째 인덱스의 현재 userID 가져오는 법?
                        Map<String, Object> map = (Map<String, Object>) applicantList.get(i);
                        if (map.get("userID").equals(userID)){
                            Toast.makeText(getApplicationContext(), "이미 가입된 스터디입니다.", Toast.LENGTH_SHORT).show();
                            break;
                        }

                        /*
                         * 만약 존재하면 토스트 메시지 보여주고 break;
                         * Toast.makeText(getApplicationContext(), "이미 가입된 스터디입니다.", Toast.LENGTH_SHORT).show();
                         * */
                        else if (!map.get("userID").equals(userID)){
                            Toast.makeText(getApplicationContext(), "신청되었습니다.", Toast.LENGTH_SHORT).show();

                        }
                    }

                    /*만약 다 돌았는데 신청자 리스트에 없는 새로운 사용자이면
                     * 가입되었습니다. => Toast 메시지 보여주고
                     * applicantList에 new Applicant(~~~) 추가한 다음에
                     * db에 studyGroup 갱신 시켜주기*/
                }
            }
        });
    }
}