package com.example.study_with_me.activity;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditUserInfoActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference userRef = databaseReference.child("users");

    private FirebaseAuth firebaseAuth;

    private EditText emailEditText;
    private EditText passwordEditText1;
    private EditText passwordEditText2;
    private String userID;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_information);

        getSupportActionBar().setTitle("회원 정보 수정");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            userID = user.getUid();
            email = user.getEmail();
        }

        emailEditText = (EditText)findViewById(R.id.emailEditText);
        emailEditText.setText(email); /* 초기값으로 원래 이메일 주소 */
    }

    /* 입력한 이메일 주소로 이메일 업데이트하기 */
    public void updateEmail(View view) {
        String newEmail = emailEditText.getText().toString();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        user.updateEmail(newEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditUserInfoActivity.this, "이메일을 변경했습니다.", Toast.LENGTH_SHORT).show();
                            userRef.child(userID).child("email").setValue(newEmail);
                        }
                    }
                });
    }

    /* 내 메일로 비밀번호 재설정 이메일 보내기 */
    public void sendPasswordResetEmail(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();
        Log.d("user EMAIL:", email);

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditUserInfoActivity.this, "비밀번호 재설정 메일을 보냈습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
