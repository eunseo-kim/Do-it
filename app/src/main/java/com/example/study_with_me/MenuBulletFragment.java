package com.example.study_with_me;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import com.example.study_with_me.activity.BulletRegisterActivity;
import com.example.study_with_me.activity.MainActivity;
import com.example.study_with_me.adapter.BulletNotificationAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Map;

public class MenuBulletFragment extends Fragment implements View.OnClickListener{
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference userRef = databaseReference.child("users");
    private DatabaseReference studyGroupRef = databaseReference.child("studygroups");
    private FirebaseAuth firebaseAuth;
    private String userID;

    private MainActivity activity;
    private Map<String, Object> studyInfo;
    private String studyGroupID;
    private ArrayList<Map<String, Object>> bulletinList;
    private ArrayList<Map<String, Object>> filteredList;

    private RadioButton bulletinNotice, bulletinAll;
    private FloatingActionButton addButton;
    private ListView listView;
    private boolean filterNotice = false;

    private final int ALL_BTN = 0, NOTI_BTN = 1, PIC_BTN = 2, FILE_BTN = 3;
    int selectedType = ALL_BTN;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.bullet, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        bulletinNotice = root.findViewById(R.id.bulletinNotice);
        bulletinAll = root.findViewById(R.id.bulletinAll);
        addButton = root.findViewById(R.id.addButton);
        listView = (ListView)root.findViewById(android.R.id.list);

        bulletinNotice.setOnClickListener(this);
        bulletinAll.setOnClickListener(this);
        addButton.setOnClickListener(this);

        // 상단 액션바 설정
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("게시판");
        actionBar.setDisplayHomeAsUpEnabled(true);

        /* StudyGroup Info */
        activity = (MainActivity) getActivity();
        studyInfo = activity.getStudyInfo();
        studyGroupID = studyInfo.get("studyGroupID").toString();

        bulletinList = new ArrayList<>();
        filteredList = new ArrayList<>();

        /*listView의 url 클릭 시 url 실행*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(bulletinList.get(i))));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.d("click", "onItemClick");
                if(intent.resolveActivity(requireContext().getPackageManager()) != null) {
                    Toast.makeText(getActivity().getApplicationContext(), "링크", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }
        });
        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bulletinAll: // listView 분류를 [전체]로
                getNonFilteredList();
                break;
            case R.id.bulletinNotice:   // listView 분류를 [공지]로
                getFilteredList();
                break;
            case R.id.addButton: // 게시글 추가 버튼
                Intent intent = new Intent(view.getContext(), BulletRegisterActivity.class);
                intent.putExtra("studyGroupID", studyGroupID);
                startActivity(intent);
                break;
        }
    }

    private void getBulletinList() {
        bulletinList.clear();
        studyGroupRef.child(studyGroupID)
                .child("bulletinBoard")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    Map<String, Object> bulletinItem = (Map<String, Object>) snapshot.getValue();
                    Log.d("bulletinItem", bulletinItem.toString());
                    bulletinList.add(0, bulletinItem);
                }
                setListView(bulletinList);
            }
        });
    }

    private void getFilteredList() {
        filteredList.clear();
        for(Map<String, Object> item : bulletinList) {
            Log.d("item:", item.toString());
            if ((Boolean)item.get("notice")) {
                filteredList.add(item);
            }
        }
        setListView(filteredList);
    }

    private void getNonFilteredList() {
        setListView(bulletinList);
    }


    private void setListView(ArrayList list) {
        final BulletNotificationAdapter bulletNotificationAdapter = new BulletNotificationAdapter(getActivity().getApplicationContext(), list);
        listView.setAdapter(bulletNotificationAdapter);
    }

    /** ListView 갱신 **/
    @Override
    public void onResume() {
        super.onResume();
        getBulletinList();
    }
}