package com.example.study_with_me;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.study_with_me.activity.MainActivity;
import com.example.study_with_me.adapter.SchedulerAdapter;
import com.example.study_with_me.adapter.TeamEvaluationAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class MenuScheduleManagementFragment extends ListFragment implements View.OnClickListener, OnDateSelectedListener{
    public class CalendarDecorator implements DayViewDecorator {
        private CalendarDay date;

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return day.equals(date);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
    }

    private ListView scheduleListView;
    private Button addButton, createButton;
    private TextView scheduleEditText;
    private MaterialCalendarView mCalendarView;
    private TextView dateTextView;
    private ArrayList<String> scheduleList;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference userRef = databaseReference.child("users");
    private DatabaseReference studyGroupRef = databaseReference.child("studygroups");
    private FirebaseAuth firebaseAuth;
    private String userID;
    private MainActivity activity;
    private Map<String, Object> studyInfo;
    private String studyGroupID;
    private CalendarDay calendarDay;
    private String calendarDate; // "year/month/day" 형태의 문자열
    private CalendarDay decorateDay;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.schedule_management, container, false);

        // 상단 액션바 설정
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("일정 관리");
        actionBar.setDisplayHomeAsUpEnabled(true);

        scheduleEditText = root.findViewById(R.id.scheduleEditTextView);
        addButton = (Button)root.findViewById(R.id.addScheduleButton);
        createButton = (Button)root.findViewById(R.id.createScheduleButton);
        addButton.setOnClickListener(this);
        createButton.setOnClickListener(this);

        scheduleListView = (ListView) root.findViewById(android.R.id.list);
        mCalendarView = (MaterialCalendarView) root.findViewById(R.id.calendarView);
        mCalendarView.setOnDateChangedListener(this);
        dateTextView = (TextView) root.findViewById(R.id.dateTextView);
        mCalendarView.setSelectedDate(CalendarDay.today()); // 실행 시 오늘 날짜로 시작
        calendarDate = String.format("%d/%d/%d", CalendarDay.today().getYear(),
                CalendarDay.today().getMonth()+1, CalendarDay.today().getDay());

        activity = (MainActivity) getActivity();
        studyInfo = activity.getStudyInfo();
        studyGroupID = (String) studyInfo.get("studyGroupID");
        scheduleList = new ArrayList<>();

        setListView();
        return root;
    }


    public void addButtonClicked() {
        addButton.setVisibility(View.GONE);
        scheduleEditText.setVisibility(View.VISIBLE);
        createButton.setVisibility(View.VISIBLE);
    }

    public void createButtonClicked() {
        addButton.setVisibility(View.VISIBLE);
        scheduleEditText.setVisibility(View.GONE);
        createButton.setVisibility(View.GONE);

        /** scheduleEditText에 내용이 있으면 [Calendar - year - month - day - schedule] 추가하기 **/
        if (scheduleEditText.getText().length() == 0) {
            Toast.makeText(getContext(), "일정을 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            final String schedule = scheduleEditText.getText().toString();
            studyGroupRef.child(studyGroupID).child("calendar").child(calendarDate).push().setValue(schedule);
            setListView();
        }
    }

    public void deleteButtonClicked() {

    }

    /** Fragment 안에서 발생하는 모든 클릭 이벤트를 처리 **/
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.addScheduleButton:
                addButtonClicked();
                break;
            case R.id.createScheduleButton:
                createButtonClicked();
                break;
            case R.id.deleteButton:
                deleteButtonClicked();
                break;
        }
    }

    private void setListView() {
        final SchedulerAdapter schedulerAdapter = new SchedulerAdapter(getActivity(), scheduleList);

        scheduleList.clear();
        studyGroupRef.child(studyGroupID).child("calendar").child(calendarDate)
            .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot schedule : task.getResult().getChildren()) {
                    scheduleList.add(schedule.getValue(String.class));
                }
                scheduleListView.setAdapter(schedulerAdapter);
            }
        });
    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        calendarDay = date;
        final int year = calendarDay.getYear();
        final int month = calendarDay.getMonth()+1;
        final int day = calendarDay.getDay();
        calendarDate = String.format("%d/%d/%d", year, month, day);

        setListView();
    }

    private void decorateCalendar() {
        final int year = calendarDay.getYear();
        final int month = calendarDay.getMonth()+1;
    }
}
