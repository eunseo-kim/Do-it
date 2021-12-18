package com.example.study_with_me.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import android.widget.Toast;

import com.example.study_with_me.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FindPasswordActivity extends AppCompatActivity {
    EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_password);

        emailEditText = (EditText) findViewById(R.id.email);
    }

        public void sendPasswordResetEmail(View view) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String email = emailEditText.getText().toString();
            Log.d("user EMAIL:", email);

            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(FindPasswordActivity.this, "비밀번호 재설정 메일을 보냈습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


}