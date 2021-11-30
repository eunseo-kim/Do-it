package com.example.study_with_me;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.example.study_with_me.activity.AttendanceRegisterActivity;
import com.example.study_with_me.activity.BulletRegisterActivity;
import com.example.study_with_me.activity.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MenuBulletFragment extends Fragment implements View.OnClickListener{
    RadioButton bulletinNotice, bulletinAll;
    FloatingActionButton addButton;
    private final int ALL_BTN = 0, NOTI_BTN = 1, PIC_BTN = 2, FILE_BTN = 3;
    int selectedType = ALL_BTN;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.bullet, container, false);

        bulletinNotice = root.findViewById(R.id.bulletinNotice);
        bulletinAll = root.findViewById(R.id.bulletinAll);
        addButton = root.findViewById(R.id.addButton);

        bulletinNotice.setOnClickListener(this);
        bulletinAll.setOnClickListener(this);
        addButton.setOnClickListener(this);

        // 상단 액션바 설정
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("게시판");
        actionBar.setDisplayHomeAsUpEnabled(true);

        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bulletinAll: // listView 분류를 [전체]로
                Toast.makeText(getContext(), "전체 게시물 보기", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bulletinNotice:   // listView 분류를 [공지]로
                Toast.makeText(getContext(), "공지사항 보기", Toast.LENGTH_SHORT).show();
                break;
            case R.id.addButton: // 게시글 추가 버튼
                Intent intent = new Intent(view.getContext(), BulletRegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}