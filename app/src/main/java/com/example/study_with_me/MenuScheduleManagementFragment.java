package com.example.study_with_me;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.study_with_me.activity.MainActivity;

import java.util.Calendar;

public class MenuScheduleManagementFragment extends ListFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.schedule_management, container, false);

        //editText 키보드 가림 문제 해결X
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // 상단 액션바 설정
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("일정 관리");
        actionBar.setDisplayHomeAsUpEnabled(true);

        //  ListView adapter
        ListView listView = (ListView)root.findViewById(android.R.id.list);
        String[] list = {"오후 10시에 회의", "UI 완성해오기"}; // test data 2개
        MainActivity activity = (MainActivity) getActivity();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.schedule_item, R.id.scheduleTextView, list); //어댑터를 리스트 뷰에 적용
        setListAdapter(adapter);


        // [일정을 추가해보세요] 누르면 [editText + 등록하기 버튼] 표시
        Button addButton = (Button)root.findViewById(R.id.addScheduleButton);
        TextView scheduleEditText = root.findViewById(R.id.scheduleEditTextView);
        Button createButton = (Button)root.findViewById(R.id.createScheduleButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addButton.setVisibility(View.GONE);
                scheduleEditText.setVisibility(View.VISIBLE);
                createButton.setVisibility(View.VISIBLE);
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addButton.setVisibility(View.VISIBLE);
                scheduleEditText.setVisibility(View.GONE);
                createButton.setVisibility(View.GONE);
            }
        });


        // 클릭한 날짜 텍스트로 title 바꾸기
        CalendarView mCalendarView = (CalendarView) root.findViewById(R.id.calendarView);
        TextView calendarDate = (TextView) root.findViewById(R.id.calendarDate);
        // 오늘 날짜로 초기화
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH) + 1;
        int d = calendar.get(Calendar.DATE);

        String date =  y + "/" + m + "/" + d;
        calendarDate.setText(date);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() // 날짜 선택 이벤트
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                String date = year + "/" + (month + 1) + "/" + dayOfMonth;
                calendarDate.setText(date); // 선택한 날짜로 설정
            }
        });

        return root;
    }
}