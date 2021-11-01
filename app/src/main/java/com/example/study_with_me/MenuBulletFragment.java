package com.example.study_with_me;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.study_with_me.activity.BulletRegisterActivity;

public class MenuBulletFragment extends Fragment implements View.OnClickListener{
    TextView allBtn;
    TextView notiBtn;
    TextView picBtn;
    TextView fileBtn;
    TextView nothingText;
    Button writeBtn;
    private final int ALL_BTN = 0, NOTI_BTN = 1, PIC_BTN = 2, FILE_BTN = 3;
    int selectedType = ALL_BTN;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.bullet, container, false);

        allBtn = root.findViewById(R.id.bulletMenuAllBtn);
        notiBtn = root.findViewById(R.id.bulletMenuNotiBtn);
        picBtn = root.findViewById(R.id.bulletMenuPicBtn);
        fileBtn = root.findViewById(R.id.bulletMenuFileBtn);
        nothingText = root.findViewById(R.id.bulletNothingText);
        writeBtn = root.findViewById(R.id.bulletWriteBtn);

        allBtn.setOnClickListener(this);
        notiBtn.setOnClickListener(this);
        picBtn.setOnClickListener(this);
        fileBtn.setOnClickListener(this);
        writeBtn.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.bulletWriteBtn:
                intent = new Intent(getActivity(), BulletRegisterActivity.class);
                intent.putExtra("type", selectedType);
                startActivity(intent);
                break;
            case R.id.bulletMenuAllBtn:
                allBtn.setTextColor(Color.BLACK);
                notiBtn.setTextColor(Color.GRAY);
                picBtn.setTextColor(Color.GRAY);
                fileBtn.setTextColor(Color.GRAY);
                nothingText.setText("등록된 글이 없습니다.");
                writeBtn.setText("글 쓰기");
                selectedType = ALL_BTN;
                break;
            case R.id.bulletMenuNotiBtn:
                notiBtn.setTextColor(Color.BLACK);
                allBtn.setTextColor(Color.GRAY);
                picBtn.setTextColor(Color.GRAY);
                fileBtn.setTextColor(Color.GRAY);
                nothingText.setText("중요한 소식을 공지로 전해보세요");
                writeBtn.setText("글 쓰기");
                selectedType = NOTI_BTN;
                break;
            case R.id.bulletMenuPicBtn:
                picBtn.setTextColor(Color.BLACK);
                allBtn.setTextColor(Color.GRAY);
                notiBtn.setTextColor(Color.GRAY);
                fileBtn.setTextColor(Color.GRAY);
                nothingText.setText("멤버들과 사진을 공유해보세요");
                writeBtn.setText("사진 올리기");
                selectedType = PIC_BTN;
                break;
            case R.id.bulletMenuFileBtn:
                fileBtn.setTextColor(Color.BLACK);
                allBtn.setTextColor(Color.GRAY);
                picBtn.setTextColor(Color.GRAY);
                notiBtn.setTextColor(Color.GRAY);
                nothingText.setText("멤버들과 파일을 공유해보세요");
                writeBtn.setText("파일 올리기");
                selectedType = FILE_BTN;
                break;
        }
    }
}
