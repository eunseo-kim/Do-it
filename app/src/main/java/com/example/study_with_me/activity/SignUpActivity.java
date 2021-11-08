package com.example.study_with_me.activity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;
import com.example.study_with_me.activity.LoginActivity;
import com.example.study_with_me.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    // 비밀번호 유효성 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{6,16}$");
    private EditText signUpPassword, signUpConfirmPassword, signUpEmail, signUpName;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseAuth firebaseAuth;
    private String userID; // 사용자 구분하는 키


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        signUpPassword = (EditText)findViewById(R.id.signUpPassword);
        signUpConfirmPassword = (EditText)findViewById(R.id.signUpConfirmPassword);
        signUpEmail = (EditText)findViewById(R.id.signUpEmail);
        signUpName = (EditText)findViewById(R.id.signUpName);

    }

    public void signUp(View view) {
         String pwd = signUpPassword.getText().toString().trim();
         String checkpwd = signUpConfirmPassword.getText().toString().trim();
         String email = signUpEmail.getText().toString().trim();
         String name = signUpName.getText().toString().trim();

        if(isValidEmail(email) && isValidPasswd(pwd, checkpwd) && isValidName(name)) {
            createUser(email, pwd, name);
        }
    }

    // 이메일 유효성 검사
    private boolean isValidEmail(String email) {
        if (email.isEmpty()) {
            // 이메일 공백
            Toast.makeText(getApplicationContext(), "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            Toast.makeText(getApplicationContext(), "이메일 형식을 확인해주세요", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 판단
    private boolean isValidPasswd(String pwd, String checkpwd) {
        if (pwd.isEmpty()) {
            // 비밀번호 공백
            Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(pwd).matches()) {
            // 비밀번호 형식 불일치
            Toast.makeText(getApplicationContext(), "비밀번호 형식을 확인해주세요", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!pwd.equals(checkpwd)) {
            // 비밀번호와 확인 비밀번호가 다름
            Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidName(String name) {
        if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void createUser(String email, String password, String name) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // 회원가입 성공 →  데이터베이스에 사용자 추가
                        addDatabase(email, password, name);

                        Log.d(TAG, "createUserWithEmail:success");
                        Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        // 회원가입 실패
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        );
    }

    // 데이터베이스에 사용자 [이메일/비번/닉네임] 추가
    // 데이터베이스에서 사용자 식별 키는 userID(현재 user의 getUid())
    public void addDatabase(String email, String password, String name) {
        UserModel usermodel = new UserModel(email, password, name);
        Log.d("tag", "userID : " + userID);
        databaseReference.child("users").child(userID).setValue(usermodel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"저장을 완료했습니다", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"저장을 실패했습니다", Toast.LENGTH_LONG).show();
            }
        });
    }
}
