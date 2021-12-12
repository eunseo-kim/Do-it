package com.example.study_with_me.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.MenuAuthorizeAttendanceFragment;
import com.example.study_with_me.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class AttendanceRegisterActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference userRef = databaseReference.child("users");
    private FirebaseAuth firebaseAuth;
    private String userID;
    private String studyGroupID;

    private Map<String, Object> attendInfo;
    private Button registerButton;
    private TextView gps, place;
    private TextView range;
    private EditText hourEditText, minuteEditText;
    private ToggleButton timeToggleButton;
    private int hour, minute;
    private boolean isPM;
    public static final String PREFS_NAME = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_register);

        // 상단 메뉴바
        getSupportActionBar().setTitle("출석 등록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        gps = findViewById(R.id.gps);
        place = findViewById(R.id.place);
        range = findViewById(R.id.range);
        hourEditText = findViewById(R.id.hourEditText);
        minuteEditText = findViewById(R.id.minuteEditText);
        timeToggleButton = findViewById(R.id.timeToggleButton);
        registerButton = findViewById(R.id.registerButton);

        Intent intent = getIntent();
        attendInfo = (Map<String, Object>) intent.getSerializableExtra("attendInfo");
        studyGroupID = intent.getStringExtra("studyGroupID");
        Log.d("attendInfo", attendInfo.toString());

        // SharedPreferences에서 가장 최근 입력된 시, 분 복원
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String hourText = settings.getString("hourText", "");
        hourEditText.setText(hourText);
        String minuteText = settings.getString("minuteText", "");
        minuteEditText.setText(minuteText);

        // 시, 분 입력 변경 감지
        handleTextChange();

        // 주소 검색창 클릭
        EditText searchView = (EditText) findViewById(R.id.map_search);
        searchView.setFocusable(false);
        searchView.setClickable(true);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MapSearchActivity.class);
                intent.putExtra("studyGroupID", studyGroupID);
                view.getContext().startActivity(intent);
            }
        });

        // 등록 버튼 클릭
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    registerAttendance();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        // am, pm timeToggleButton 클릭
        isPM = false;
        timeToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) isPM = true;
                else isPM = false;
            }
        });

        /* Initialize TextView */
        gps.setText(attendInfo.get("x") + ", " + attendInfo.get("y"));
        place.setText(attendInfo.get("place") + "");
        range.setText(attendInfo.get("range") + "");
        try {
            int HOUR = Integer.parseInt(attendInfo.get("hour").toString());
            hourEditText.setText(String.valueOf(HOUR % 12));
            if (HOUR > 12) {    // toggleButton Clicked(check PM)
                timeToggleButton.setChecked(true);
            }
            minuteEditText.setText(attendInfo.get("minute") + "");
        } catch (Exception e) {}
    }

    private void handleTextChange() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        hourEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String hourText = hourEditText.getText().toString();
                editor.putString("hourText", hourText);
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        minuteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String minuteText = minuteEditText.getText().toString();
                editor.putString("minuteText", minuteText);
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

    }


    /** update database (attendance) **/
    private void registerAttendance() throws ParseException {
        if (hourEditText.getText().length() == 0 || minuteEditText.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "시간을 설정해주세요", Toast.LENGTH_SHORT).show();
        } else {
            // 모든 입력이 완료된 상태
            hour = Integer.parseInt(hourEditText.getText().toString());
            minute = Integer.parseInt(minuteEditText.getText().toString());
            if (isPM) hour += 12;

            userRef.child(userID).child("attendance").child(studyGroupID).child("isSet").setValue(true);
            userRef.child(userID).child("attendance").child(studyGroupID).child("place").setValue(attendInfo.get("place"));
            userRef.child(userID).child("attendance").child(studyGroupID).child("x").setValue(attendInfo.get("x"));
            userRef.child(userID).child("attendance").child(studyGroupID).child("y").setValue(attendInfo.get("y"));
            userRef.child(userID).child("attendance").child(studyGroupID).child("range").setValue(range.getText().toString());
            userRef.child(userID).child("attendance").child(studyGroupID).child("hour").setValue(String.format("%02d", hour));
            userRef.child(userID).child("attendance").child(studyGroupID).child("minute").setValue(String.format("%02d", minute));

            finish();
        }
    }
}
