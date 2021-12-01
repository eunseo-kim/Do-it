package com.example.study_with_me;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.study_with_me.activity.MainActivity;
import com.example.study_with_me.adapter.SchedulerAdapter;
import com.example.study_with_me.adapter.TeamEvaluationAdapter;
import com.example.study_with_me.model.CalendarDecorator;
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
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class MenuScheduleManagementFragment extends ListFragment implements View.OnClickListener, OnDateSelectedListener{

    private SwipeMenuListView scheduleListView;
    private Button addButton;
    private Button createButton;
    private ImageView deleteButton;
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
    private ArrayList<CalendarDay> decorateDates;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.schedule_management, container, false);

        // 상단 액션바 설정
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("일정 관리");
        actionBar.setDisplayHomeAsUpEnabled(true);


        /* Button Event Listener */
        scheduleEditText = root.findViewById(R.id.scheduleEditTextView);
        addButton = (Button)root.findViewById(R.id.addScheduleButton);
        createButton = (Button)root.findViewById(R.id.createScheduleButton);
        addButton.setOnClickListener(this);
        createButton.setOnClickListener(this);

        /* StudyGroup Info */
        activity = (MainActivity) getActivity();
        studyInfo = activity.getStudyInfo();
        studyGroupID = (String) studyInfo.get("studyGroupID");


        /* set Calendar View */
        scheduleListView = (SwipeMenuListView) root.findViewById(android.R.id.list);
        mCalendarView = (MaterialCalendarView) root.findViewById(R.id.calendarView);
        mCalendarView.setOnDateChangedListener(this);
        dateTextView = (TextView) root.findViewById(R.id.dateTextView);

        /* initialize Calendar View */
        calendarDay = CalendarDay.today(); // 실행 시 오늘 날짜로 시작
        mCalendarView.setSelectedDate(calendarDay);
        mCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                calendarDay = date;
                mCalendarView.setSelectedDate(date);
                calendarDate = String.format("%d/%d/%d", calendarDay.getYear(),
                        calendarDay.getMonth()+1, calendarDay.getDay());
                dateTextView.setText(calendarDate);
                decorateCalendar();
                setListView();
            }
        });
        calendarDate = String.format("%d/%d/%d", calendarDay.getYear(),
                CalendarDay.today().getMonth()+1, calendarDay.getDay());
        dateTextView.setText(calendarDate);
        decorateDates = new ArrayList<>();
        scheduleList = new ArrayList<>();
        decorateCalendar();

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
            decorateCalendar();
            setListView();
        }
    }


    /** Fragment 안에서 발생하는 클릭 이벤트를처리 **/
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.addScheduleButton:
                addButtonClicked();
                break;
            case R.id.createScheduleButton:
                createButtonClicked();
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

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(activity.getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(182, 182,182)));
                deleteItem.setWidth(168);
                deleteItem.setTitle("취소");
                deleteItem.setTitleSize(14);
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);

                SwipeMenuItem removeItem = new SwipeMenuItem(activity.getApplicationContext());
                removeItem.setBackground(new ColorDrawable(Color.rgb(253, 139,139)));
                removeItem.setWidth(168);
                removeItem.setTitle("삭제");
                removeItem.setTitleSize(14);
                removeItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(removeItem);
            }
        };
        scheduleListView.setMenuCreator(creator);

        scheduleListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (index == 1) {
                    scheduleList.remove(position);
                    studyGroupRef.child(studyGroupID).child("calendar").child(calendarDate).setValue(scheduleList);
                    mCalendarView.removeDecorators();
                    decorateCalendar();
                    schedulerAdapter.notifyDataSetChanged();
                }
                return false;
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
        dateTextView.setText(calendarDate);

        setListView();
    }

    private void decorateCalendar() {
        decorateDates.clear();
        final int year = calendarDay.getYear();
        final int month = calendarDay.getMonth()+1;
        studyGroupRef.child(studyGroupID).child("calendar")
                .child(String.valueOf(year))
                .child(String.valueOf(month))
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot Day : task.getResult().getChildren()) {
                    int day = Integer.parseInt(Day.getKey().toString());
                    CalendarDay cDay = new CalendarDay(year, month-1, day);
                    decorateDates.add(cDay);
                }
                mCalendarView.addDecorators(new CalendarDecorator(decorateDates));
            }
        });
    }
}
