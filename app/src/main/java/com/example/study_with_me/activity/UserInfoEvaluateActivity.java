package com.example.study_with_me.activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.study_with_me.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class UserInfoEvaluateActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private String userID;
    private ListView userEvaluateListView;
    private ArrayList<Map<String, Object>> userEvaluate = new ArrayList<>();
    private DatabaseReference userRef = databaseReference.child("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_evaluate_list);
        getSupportActionBar().setTitle("평가 목록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        userEvaluateListView = (ListView) findViewById(R.id.userEvaluateListView);

        getEvaluateList();
    }
    public void getEvaluateList() {
        userEvaluate.clear();
        userRef.child(userID).child("comments").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                Log.d("print>>", )
            }
        });
    }
}
