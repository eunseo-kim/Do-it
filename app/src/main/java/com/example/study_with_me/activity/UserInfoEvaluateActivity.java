package com.example.study_with_me.activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.study_with_me.R;

public class UserInfoEvaluateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_evaluate_list);
        getSupportActionBar().setTitle("평가 목록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
