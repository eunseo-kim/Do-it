package com.example.study_with_me;

import android.content.Intent;
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

public class MenuBulletFragment extends Fragment /**implements View.OnClickListener*/{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_menu_bullet, container, false);
        /**
        TextView allBtn = root.findViewById(R.id.bulletMenuAllBtn);
        TextView notiBtn = root.findViewById(R.id.bulletMenuNotiBtn);
        TextView picBtn = root.findViewById(R.id.bulletMenuPicBtn);
        TextView fileBtn = root.findViewById(R.id.bulletMenuFileBtn);
        Button writeBtn = root.findViewById(R.id.bulletWriteBtn);

        allBtn.setOnClickListener(this);
        notiBtn.setOnClickListener(this);
        picBtn.setOnClickListener(this);
        fileBtn.setOnClickListener(this);
        writeBtn.setOnClickListener(this);
        */
        return root;
    }
    /**
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.bulletWriteBtn:
                intent = new Intent(getActivity(), BulletRegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
    */
}
