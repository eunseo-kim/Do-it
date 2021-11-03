package com.example.study_with_me;

import android.os.Bundle;
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
import com.example.study_with_me.model.MemberSampledata;

import java.util.ArrayList;


public class MenuEvaluateMemberFragment extends Fragment {
    ArrayList<MemberSampledata> memberList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.evaluate_member, container, false);

        // 상단 액션바 설정
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("팀원 평가");
        actionBar.setDisplayHomeAsUpEnabled(true);

        // 팀원 정보 초기화 (가짜 DB)
        this.InitializeMemberData();

        // 팀원 평가 화면의 ListView
        ListView listView = (ListView) root.findViewById(R.id.evalListView);
        // Adapter 설정
        final TeamEvaluationAdapter evalAdapter = new TeamEvaluationAdapter(getActivity(), memberList);
        listView.setAdapter(evalAdapter);

        return root;
    }

    public void InitializeMemberData() {
        memberList = new ArrayList<MemberSampledata>();
        memberList.add(new MemberSampledata(R.drawable.tmp_person_icon, "Park Jeong Yong"));
        memberList.add(new MemberSampledata(R.drawable.tmp_person_icon, "Park Jeong Yong2"));
        memberList.add(new MemberSampledata(R.drawable.tmp_person_icon, "Park Jeong Yong3"));
    }
}