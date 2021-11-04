package com.example.study_with_me.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;

public class StudyRegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_register);
        getSupportActionBar().setTitle("스터디 등록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // 기타 버튼 누르면 id 비교해서 각각 팝업창 뜨도록
    public void ectButtonClicked(View view) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        switch (view.getId()) {
            case (R.id.ectCategoryButton):
                dialog.setContentView(R.layout.ect_category_pop_up);
                dialog.show();
                break;
            case (R.id.ectPeopleCountButton):
                dialog.setContentView(R.layout.ect_people_pop_up);
                dialog.show();
                break;
            case (R.id.ectDateButton):
                dialog.setContentView(R.layout.ect_date_pop_up);
                dialog.show();
                break;
        }
    }
}
