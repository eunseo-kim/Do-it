package com.example.study_with_me;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.study_with_me.activity.MainActivity;
import com.example.study_with_me.adapter.TeamEvaluationAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MenuEvaluateMemberFragment extends Fragment {
    ArrayList<Map<String, String>> memberList = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference userRef = databaseReference.child("users");
    private DatabaseReference studyGroupRef = databaseReference.child("studygroups");
    private FirebaseAuth firebaseAuth;

    private TeamEvaluationAdapter evalAdapter;
    private ListView evalMemberList;
    private MainActivity activity;
    private Map<String, Object> studyInfo;
    private String uid;
    private boolean isFirstVisit = true;

    public MenuEvaluateMemberFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.evaluate_member, container, false);
        isFirstVisit = false;
        firebaseAuth = FirebaseAuth.getInstance();
        activity = (MainActivity) getActivity();
        studyInfo = activity.getStudyInfo();

        /** 상단 액션바 설정 **/
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("팀원 평가");
        actionBar.setDisplayHomeAsUpEnabled(true);

        root.setFocusableInTouchMode(true);
        root.requestFocus();
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    removeView();
                    activity.onBackPressed();
                    return true;
                }
                removeView();

                return false;
            }
        });


        /** 팀원 평가 화면의 ListView **/
        evalMemberList = (ListView) root.findViewById(R.id.evalListView);
        return root;
    }

    /** 덮은 화면 제거 **/
    private void removeView() {
        ConstraintLayout cl = (ConstraintLayout) getActivity().findViewById(R.id.coverLayout);
        if(cl != null)
            ((ViewManager) cl.getParent()).removeView(cl);
    }

    /** 스터디 기간이 끝났는지 확인 **/
    private int checkStudyTerm() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        Date curDate = new Date();
        Date endDate;

        try {
            endDate = format.parse(String.valueOf(studyInfo.get("endDate")));
            return curDate.compareTo(endDate);
        } catch(ParseException e) { throw new RuntimeException(e); }
    }

    /** 스터디 기간이 끝나지 않은 경우 화면 덮기 **/
    private void coverDisplay() {
        if(checkStudyTerm() < 0) {
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ConstraintLayout cl = (ConstraintLayout)inflater.inflate(R.layout.cover_display, null);
            cl.setBackgroundColor(Color.parseColor("#99000000"));

            ConstraintLayout.LayoutParams paramll = new ConstraintLayout.LayoutParams
                    (ConstraintLayout.LayoutParams.FILL_PARENT,ConstraintLayout.LayoutParams.FILL_PARENT);
            getActivity().addContentView(cl, paramll);

            TextView textView = (TextView) getActivity().findViewById(R.id.coverTextView);
            textView.setText("팀원 평가 기간이 아닙니다.");

            cl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeView();
                    getActivity().onBackPressed();
                }
            });
        }
    }

    /** 현재 스터디의 스터디 멤버(userID) 얻기 **/
    private ArrayList<String> getStudyGroupMembers() {
        Map<String, Object> mapMemberList = (Map<String, Object>) studyInfo.get("memberList");
        ArrayList<String> members = new ArrayList<>();

        coverDisplay();

        for(Map.Entry<String, Object> entry : mapMemberList.entrySet()) {
            members.add(String.valueOf(entry.getValue()));
        }

        return members;
    }

    /** User 정보 얻은 후 리스트에 띄우기 **/
    public void getUser() {
        ArrayList<String> members = getStudyGroupMembers();
        for(int i = 0; i < members.size(); i++) {
            if(firebaseAuth.getCurrentUser().getUid().equals(members.get(i)))
                continue;
            uid = members.get(i);
            userRef.child(members.get(i)).child("username").get().addOnCompleteListener(
                    new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            memberList.clear();
                            Map<String, String> userInfo = new HashMap<>();
                            String username = String.valueOf(task.getResult().getValue());
                            userInfo.put(uid, username);
                            memberList.add(userInfo);
                            setListView();
                        }
                    }
            );
        }
        setListView();
    }

    /** 팀원들을 나타낼 리스트 뷰 설정 **/
    private void setListView() {
        /** Adapter 설정 **/
        String studyId = String.valueOf(activity.getStudyInfo().get("studyGroupID"));
        evalAdapter = new TeamEvaluationAdapter(getActivity(), memberList, studyId);
        evalMemberList.setAdapter(evalAdapter);
    }

    /** ListView 갱신 **/
    @Override
    public void onResume() {
        super.onResume();
        getUser();
    }
}