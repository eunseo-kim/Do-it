package com.example.study_with_me;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.ListFragment;

import com.example.study_with_me.activity.AlarmActivity;
import com.example.study_with_me.activity.AttendanceRegisterActivity;
import com.example.study_with_me.activity.MainActivity;
import com.example.study_with_me.activity.StudySearchActivity;

public class MenuAuthorizeAttendanceFragment extends ListFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.attendance_authorize, container, false);

        // 상단 액션바 설정
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("출석 인증");
        actionBar.setDisplayHomeAsUpEnabled(true);

        // 멤버 출석 현황 리스트
        ListView listView = (ListView)root.findViewById(android.R.id.list);

        // 멤버 출석 현황 ListView의 header에 title 추가
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.member_attendance_title, null);
        listView.addHeaderView(headerView);

        // ListView에 적용할 adapter을 생성하고 적용(임의로 memberName 리스트를 생성하여 리스트뷰에 전달해봄)
        String[] list = {"김은서", "박정용", "고다혜", "김진욱"};
        MainActivity activity = (MainActivity) getActivity();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.member_attendance_item, R.id.memberName, list); //어댑터를 리스트 뷰에 적용
        setListAdapter(adapter);


        // [등록하기] 버튼 클릭하면 AttendanceRegisterActivity로 전환
        Button registerButton = (Button)root.findViewById(R.id.attendRegisterButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AttendanceRegisterActivity.class);
                // Intent intent = new Intent(view.getContext(), AlarmActivity.class); // 임시로 알림창 되는지 확인해봄
                view.getContext().startActivity(intent);
            }
        });

        // [edit] 버튼 클릭 시 AttendanceRegisterActivity로 전환
        ImageView editButton = (ImageView)root.findViewById(R.id.AttendEditButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AttendanceRegisterActivity.class);
                // Intent intent = new Intent(view.getContext(), StudySearchActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        return root;
    }

}