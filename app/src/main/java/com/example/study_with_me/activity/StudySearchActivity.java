package com.example.study_with_me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.study_with_me.R;
import com.example.study_with_me.adapter.SearchAdapter;
import com.example.study_with_me.model.StudyGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class StudySearchActivity extends AppCompatActivity {
    private String userID;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference studyGroupRef = databaseReference.child("studygroups");
    private ListView studySearchListView;

    private ArrayList<Map<String, Object>> studyList = new ArrayList<>(); // 전체 스터디 리스트
    private ArrayList<Map<String, Object>> filteredStudyList = new ArrayList<>(); // 필터링된 스터디 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_search_main);

        studySearchListView = (ListView) findViewById(R.id.studySearchListView);

        if(firebaseAuth.getCurrentUser() != null){
            userID = firebaseAuth.getCurrentUser().getUid();
        }
        // 상단 메뉴바
        getSupportActionBar().setTitle("스터디 검색");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /** floatingActionButton 누르면 스터디 생성 화면 **/
        floatingButtonClickedListener();
        
        /** 펼치기 버튼(expandButton) 클릭 시 필터링 검색 창 펼침 **/
        expandButtonClickedListener();

        /** DB의 studygroups의 변화가 생겼을 때 감지하는 listener **/
        setStudyGroupsChangedListener();

        editTextChangedListener();
    }
    /** 액션바 오버라이딩 **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /** 스터디 검색화면 리스트 뷰 처리 **/
    private void setListView(ArrayList<Map<String, Object>> list) {
        SearchAdapter adapter = new SearchAdapter(this, list);
        studySearchListView.setAdapter(adapter);
        studySearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Map<String, Object> item = (Map<String, Object>) adapter.getItem(position);
                Intent intent = new Intent(StudySearchActivity.this, StudyPostActivityMessage.class);
                intent.putExtra("studyGroup", (Serializable) item);
                startActivity(intent);
            }
        });
    }

    /** DB에 있는 모든 스터디 정보 가져오는 함수 **/
    private void collectAllStudyGroups(Map<String, Object> studygroups) {
        for(Map.Entry<String, Object> entry : studygroups.entrySet()) {
            Map singleStudyGroup = (Map) entry.getValue();
            studyList.add(singleStudyGroup);
        }
    }

    /** DB의 studygroups에 무언가 추가되거나 변경될 시 처리하는 함수 **/
    private void setStudyGroupsChangedListener() {
        studyGroupRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue() != null) {
                            collectAllStudyGroups((Map<String, Object>) snapshot.getValue());
                            setListView(studyList);
                            filteredStudyList = (ArrayList<Map<String, Object>>) studyList.clone();

                            // 스터디 목록 없으면 addMessage("스터디를 등록해주세요")출력하도록
                            if(studyList.size() != 0) {
                                findViewById(R.id.addMessage).setVisibility(View.INVISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                }
        );
    }

    /** floatingButton 처리 함수 **/
    private void floatingButtonClickedListener() {
                FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.floatingActionButton);
                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StudyRegisterActivity.class);
                startActivity(intent);
            }
        });
    }

//  EditText에 키보드 입력이 있으면 필터링되는 함수
    private void editTextChangedListener() {
        EditText editText = (EditText)findViewById(R.id.editText) ;
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String filterText = edit.toString();
                if(filterText.length() > 0) {
                    studySearchListView.setFilterText(filterText);
                } else {
                    studySearchListView.clearTextFilter();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
        });
    }

    /** 펼치기 버튼 처리 함수 **/
    private void expandButtonClickedListener() {
        ImageView expandButton = (ImageView)findViewById(R.id.expandButton);
        LinearLayout filteringScreen = (LinearLayout)findViewById(R.id.filteringScreen);
        expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filteringScreen.getVisibility() == View.VISIBLE) {
                    filteringScreen.setVisibility(View.GONE);
                } else {
                    filteringScreen.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    
    /** 상단 바 마이페이지, 알림 버튼 **/
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.alarmBell:
                Intent intent1 = new Intent(this, AlarmActivity.class);
                startActivity(intent1);
                return true;
            case R.id.myPage:
                Intent intent2 = new Intent(this, MyPageActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** 분류 필터링 **/
    public void onClick(View v) {
        filteredStudyList.clear();
        Log.d("studyList >>> ", studyList.get(0).toString());
        Boolean b = (Boolean) studyList.get(0).get("closed");

        switch (v.getId()) {
            case R.id.all:
                Toast.makeText(getApplicationContext(),"전체 스터디", Toast.LENGTH_SHORT).show();
                filteredStudyList = (ArrayList<Map<String, Object>>) studyList.clone();
                break;
            case R.id.programming:
                Toast.makeText(getApplicationContext(), "프로그래밍만 분류", Toast.LENGTH_SHORT).show();
                for (Map<String, Object> sg : studyList) {
                    if(String.valueOf(sg.get("type")).equals("프로그래밍")) {
                        filteredStudyList.add(sg);
                    }
                }
                break;
            case R.id.employ:
                Toast.makeText(getApplicationContext(), "취업만 분류", Toast.LENGTH_SHORT).show();
                for (Map<String, Object> sg : studyList) {
                    if(String.valueOf(sg.get("type")).equals("취업")) {
                        filteredStudyList.add(sg);
                    }
                }
                break;
            case R.id.language:
                Toast.makeText(getApplicationContext(), "어학 분류", Toast.LENGTH_SHORT).show();
                for (Map<String, Object> sg : studyList) {
                    if(String.valueOf(sg.get("type")).equals("어학")) {
                        filteredStudyList.add(sg);
                    }
                }
                break;
            case R.id.ect:
                Toast.makeText(getApplicationContext(), "기타만 분류", Toast.LENGTH_SHORT).show();
                for (Map<String, Object> sg : studyList) {
                    if (!String.valueOf(sg.get("type")).equals("프로그래밍")  && !String.valueOf(sg.get("type")).equals("취업") && !String.valueOf(sg.get("type")).equals("어학")) {
                        filteredStudyList.add(sg);
                    }
                }
                break;
        }
        setListView(filteredStudyList);
    }

    // 각각 글에 맞는 글이 매치되어야 됨!
    public void studyAreaClicked(View view) {
        Intent intent = new Intent(this, StudyPostActivityMessage.class);
        startActivity(intent);
    }

    /**인원수 필터링**/
    public void filterCount(View view) {
        ArrayList<Map<String, Object>> filterCountList = new ArrayList<Map<String, Object>>();
        switch (view.getId()) {
            case R.id.two:
                for (Map<String, Object> sg : filteredStudyList) {
                    if (Integer.valueOf(String.valueOf(sg.get("member"))) == 2) {
                        filterCountList.add(sg);
                    }
                }
                setListView(filterCountList);
                break;
            case R.id.three:
                for (Map<String, Object> sg : filteredStudyList) {
                    if (Integer.valueOf(String.valueOf(sg.get("member"))) == 3) {
                        filterCountList.add(sg);
                    }
                }
                setListView(filterCountList);
                break;
            case R.id.four:
                for (Map<String, Object> sg : filteredStudyList) {
                    if (Integer.valueOf(String.valueOf(sg.get("member"))) == 4) {
                        filterCountList.add(sg);
                    }
                }
                setListView(filterCountList);
                break;
            case R.id.moreFour:
                for (Map<String, Object> sg : filteredStudyList) {
                    if (Integer.valueOf(String.valueOf(sg.get("member"))) > 4) {
                        filterCountList.add(sg);
                    }
                }
                setListView(filterCountList);
                break;
        }
    }

    /**스터디 기간 필터링**/
    public void filerDate(View view) {
        String startDate;
        String endDate;
        int month;
        ArrayList<Map<String, Object>> filterCountList = new ArrayList<Map<String, Object>>();
        switch(view.getId()) {
            case R.id.oneMonth:
                for(Map<String, Object> sg: filteredStudyList) {
                    startDate = String.valueOf(sg.get("startDate"));
                    endDate = String.valueOf(sg.get("endDate"));

                    if(Integer.parseInt(endDate.substring(2,4)) > Integer.parseInt(startDate.substring(2,4))) {
                        month = 12 - Integer.parseInt(startDate.substring(5,7)) + Integer.parseInt(endDate.substring(5,7));
                        if(month == 1) {
                            Log.d("기간", Integer.toString(month));
                            filterCountList.add(sg);
                        }
                    } else {
                        month = Integer.parseInt(endDate.substring(5,7)) - Integer.parseInt(startDate.substring(5,7));
                        if(month == 1) {
                            filterCountList.add(sg);
                        }
                    }
                }
                setListView(filterCountList);
                break;
            case R.id.twoMonth:
                for(Map<String, Object> sg: filteredStudyList) {
                    startDate = String.valueOf(sg.get("startDate"));
                    endDate = String.valueOf(sg.get("endDate"));

                    if(Integer.parseInt(endDate.substring(2,4)) > Integer.parseInt(startDate.substring(2,4))) {
                        month = 12 - Integer.parseInt(startDate.substring(5,7)) + Integer.parseInt(endDate.substring(5,7));
                        if(month == 2) {
                            Log.d("기간", Integer.toString(month));
                            filterCountList.add(sg);
                        }
                    } else {
                        month = Integer.parseInt(endDate.substring(5,7)) - Integer.parseInt(startDate.substring(5,7));
                        if(month == 2) {
                            filterCountList.add(sg);
                        }
                    }
                }
                setListView(filterCountList);
                break;
            case R.id.sixMonth:
                for(Map<String, Object> sg: filteredStudyList) {
                    startDate = String.valueOf(sg.get("startDate"));
                    endDate = String.valueOf(sg.get("endDate"));

                    if(Integer.parseInt(endDate.substring(2,4)) > Integer.parseInt(startDate.substring(2,4))) {
                        month = 12 - Integer.parseInt(startDate.substring(5,7)) + Integer.parseInt(endDate.substring(5,7));
                        if(month == 6) {
                            Log.d("기간", Integer.toString(month));
                            filterCountList.add(sg);
                        }
                    } else {
                        month = Integer.parseInt(endDate.substring(5,7)) - Integer.parseInt(startDate.substring(5,7));
                        if(month == 6) {
                            filterCountList.add(sg);
                        }
                    }
                }
                setListView(filterCountList);
                break;
            case R.id.moreSixMonth:
                for(Map<String, Object> sg: filteredStudyList) {
                    startDate = String.valueOf(sg.get("startDate"));
                    endDate = String.valueOf(sg.get("endDate"));

                    if(Integer.parseInt(endDate.substring(2,4)) > Integer.parseInt(startDate.substring(2,4))) {
                        month = 12 - Integer.parseInt(startDate.substring(5,7)) + Integer.parseInt(endDate.substring(5,7));
                        if(month > 6) {
                            Log.d("기간", Integer.toString(month));
                            filterCountList.add(sg);
                        }
                    } else {
                        month = Integer.parseInt(endDate.substring(5,7)) - Integer.parseInt(startDate.substring(5,7));
                        if(month > 6) {
                            filterCountList.add(sg);
                        }
                    }
                }
                setListView(filterCountList);
                break;
        }
    }

}
