package com.example.study_with_me;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.example.study_with_me.activity.MainActivity;
import com.example.study_with_me.adapter.TeamEvaluationAdapter;
import com.example.study_with_me.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MenuEvaluateMemberFragment extends Fragment {
    ArrayList<String> memberList = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference userRef = databaseReference.child("users");
    private DatabaseReference studyGroupRef = databaseReference.child("studygroups");
    private FirebaseAuth firebaseAuth;

    private Map<String, Object> studyInfo = new HashMap<>();
    private ListView evalMemberList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.evaluate_member, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        /** 상단 액션바 설정 **/
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("팀원 평가");
        actionBar.setDisplayHomeAsUpEnabled(true);

        /** 팀원 평가 화면의 ListView **/
        evalMemberList = (ListView) root.findViewById(R.id.evalListView);

        getUser();

        return root;
    }

    /** 현재 스터디의 Map 형태의 스터디 멤버 얻기 **/
    private ArrayList<String> getStudyGroupMembers() {
        MainActivity activity = (MainActivity) getActivity();
        Map<String, Object> studyInfo = activity.getStudyInfo();
        Map<String, Object> mapMemberList = (Map<String, Object>) studyInfo.get("memberList");
        ArrayList<String> members = new ArrayList<>();

        for(Map.Entry<String, Object> entry : mapMemberList.entrySet()) {
            members.add(String.valueOf(entry.getValue()));
        }
        return members;
    }

    /** User 정보 얻은 후 리스트에 띄우기 **/
    private void getUser() {
        ArrayList<String> members = getStudyGroupMembers();

        for(int i = 0; i < members.size(); i++) {
            Log.d("id >> ", firebaseAuth.getCurrentUser().getUid() + ", " + members.get(i));
            if(firebaseAuth.getCurrentUser().getUid().equals(members.get(i)))
                continue;

            userRef.child(members.get(i)).child("username").get().addOnCompleteListener(
                    new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            String username = String.valueOf(task.getResult().getValue());
                            Log.d("name >>> ", username);
                            memberList.add(username);
                            setListView();
                        }
                    }
            );
        }
    }

    /** 팀원들을 나타낼 리스트 뷰 설정 **/
    private void setListView() {
        /** Adapter 설정 **/
        final TeamEvaluationAdapter evalAdapter = new TeamEvaluationAdapter(getActivity(), memberList);
        evalMemberList.setAdapter(evalAdapter);
    }
}