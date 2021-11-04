package com.example.study_with_me;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.activity.LoginActivity;
import com.example.study_with_me.activity.MyPageActivity;
import com.example.study_with_me.activity.StudySearchActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    // 비밀번호 유효성 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
    private EditText signUpID;
    private EditText signUpPassword;
    private EditText signUpConfirmPassword;
    private EditText signUpEmail;
    private EditText signUpPhone;
    FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        getSupportActionBar().setTitle("회원가입");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        signUpID = (EditText)findViewById(R.id.signUpID);
        signUpPassword = (EditText)findViewById(R.id.signUpPassword);
        signUpConfirmPassword = (EditText)findViewById(R.id.signUpConfirmPassword);
        signUpEmail = (EditText)findViewById(R.id.signUpEmail);
        signUpPhone = (EditText)findViewById(R.id.signUpPhone);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void signUp(View view) {
        final String id = signUpID.getText().toString().trim();
        final String pwd = signUpPassword.getText().toString().trim();
        final String checkpwd = signUpConfirmPassword.getText().toString().trim();
        final String email = signUpEmail.getText().toString().trim();
        final String phone = signUpPhone.getText().toString().trim();

        if(isValidEmail(email) && isValidPasswd(pwd, checkpwd)) {
            createUser(email, pwd);
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

    private void createUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // 회원가입 성공
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        // 회원가입 실패
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}
