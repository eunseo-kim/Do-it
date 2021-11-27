package com.example.study_with_me;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.ListFragment;

import com.example.study_with_me.activity.AlarmActivity;
import com.example.study_with_me.activity.AttendanceRegisterActivity;
import com.example.study_with_me.activity.MainActivity;
import com.example.study_with_me.activity.StudySearchActivity;
import com.example.study_with_me.adapter.AttendanceAdapter;
import com.example.study_with_me.adapter.SchedulerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuAuthorizeAttendanceFragment extends ListFragment {
    private MainActivity activity;
    private ListView listView;
    private Map<String, Object> studyInfo;
    private String studyGroupID;
    private ArrayList<Map<String, Object>> memberList = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference userRef = databaseReference.child("users");
    private String memberName;
    private TextView attendTime, attendGPS, attendPlace, attendRange;
    private Map<String, Object> attendInfo;
    private Button attendEditButton;
    private FirebaseAuth firebaseAuth;
    private String userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.attendance_authorize, container, false);

        // 상단 액션바 설정
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("출석 인증");
        actionBar.setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        activity = (MainActivity) getActivity();
        studyInfo = activity.getStudyInfo();
        studyGroupID = (String) studyInfo.get("studyGroupID");

        attendTime = root.findViewById(R.id.attendTime);
        attendGPS = root.findViewById(R.id.attendGPS);
        attendPlace = root.findViewById(R.id.attendPlace);
        attendRange = root.findViewById(R.id.attendRange);
        attendEditButton = root.findViewById(R.id.AttendEditButton);

        listView = (ListView)root.findViewById(android.R.id.list);
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.member_attendance_title, null);
        listView.addHeaderView(headerView);

        setMyAttendance();
        getMemberList();

        attendEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AttendanceRegisterActivity.class);
                intent.putExtra("attendInfo", (Serializable)attendInfo);
                view.getContext().startActivity(intent);
            }
        });

        return root;
    }

    private void setMyAttendance() {
        userRef.child(userID).child("attendance").child(studyGroupID)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                attendInfo = (Map<String, Object>) task.getResult().getValue();
                String time = (String) attendInfo.get("time");
                String gps = (String) attendInfo.get("gps");
                String place = (String) attendInfo.get("place");
                String range = (String) attendInfo.get("range");
                attendTime.setText(time);
                attendGPS.setText(gps);
                attendPlace.setText(place);
                attendRange.setText(range);
            }
        });
    }

    private void getMemberList() {
        memberList.clear();
        Map<String, String> membersMap = (Map<String, String>) studyInfo.get("memberList");
        for (Map.Entry<String, String> entry : membersMap.entrySet()) {
            final String memberID = entry.getValue();
            userRef.child(memberID).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    memberName = (String) task.getResult().getValue();
                }
            });

            Map<String, Object> member = new HashMap<>();

            userRef.child(memberID).child("attendance").child(studyGroupID)
                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    Boolean isSet = (Boolean) task.getResult().child("isSet").getValue();
                    Boolean attend = (Boolean) task.getResult().child("attend").getValue();
                    String gps = (String) task.getResult().child("gps").getValue();
                    String place = (String) task.getResult().child("place").getValue();
                    String range = (String) task.getResult().child("range").getValue();
                    String time = (String) task.getResult().child("time").getValue();
                    member.put("memberName", memberName);
                    member.put("isSet", isSet);
                    member.put("attend", attend);
                    member.put("gps", gps);
                    member.put("place", place);
                    member.put("range", range);
                    member.put("time", time);
                    memberList.add(member);
                    Log.d("memberList", memberList.toString());
                    setListView();
                }
            });
        }
    }

    private void setListView() {
        final AttendanceAdapter attendanceAdapter = new AttendanceAdapter(getActivity(), memberList);
        listView.setAdapter(attendanceAdapter);
    }
}