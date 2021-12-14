package com.example.study_with_me.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.study_with_me.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BulletRegisterActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference userRef = databaseReference.child("users");
    private DatabaseReference studyGroupRef = databaseReference.child("studygroups");
    private FirebaseAuth firebaseAuth;
    private String userID;
    private String userName;
    private String studyGroupID;
    private Button postButton;
    private CheckBox noticeButton;
    private EditText editText;
    private boolean isNotice;   // 공지글인지 여부
    private long registerTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bullet_register);
        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        Intent intent = getIntent();
        studyGroupID = intent.getStringExtra("studyGroupID");

        // initialise views
        postButton = findViewById(R.id.postButton);
        noticeButton = findViewById(R.id.noticeButton);
        editText = findViewById(R.id.editText);

        // get current user's userName
        userRef.child(userID)
                .child("username")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                userName = task.getResult().getValue(String.class);
            }
        });

        // post button clicked
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPost();
            }
        });

        // 공지글 checkBox Click event
        isNotice = false;
        noticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNotice = ((CheckBox)view).isChecked();
            }
        });
    }


    // upload Post
    private void uploadPost() {
        if (editText.getText().length()==0) {
            Toast.makeText(BulletRegisterActivity.this,
                    "게시물을 작성해주세요",
                    Toast.LENGTH_SHORT).show();
        } else {
            Map<String, Object> bulletinMap = new HashMap<>();
            bulletinMap.put("text", editText.getText().toString()); // 게시글
            bulletinMap.put("notice", isNotice);    // 공지인지
            bulletinMap.put("writerID", userID);      // 작성자 userID
            bulletinMap.put("writerName", userName);      // 작성자 userID
            registerTime = System.currentTimeMillis();
            bulletinMap.put("registerTime", registerTime);
            Log.d("bulletinMap", bulletinMap.toString());

            studyGroupRef.child(studyGroupID).child("bulletinBoard")
                    .child(String.valueOf(registerTime)).setValue(bulletinMap);

            finish();
        }
    }
}
