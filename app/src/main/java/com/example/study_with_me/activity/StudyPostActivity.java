package com.example.study_with_me.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.study_with_me.R;

public class StudyPostActivity extends AppCompatActivity {
    Dialog dialog01;    // custom dialog


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog01 = new Dialog(StudyPostActivity.this);
        dialog01.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog01.setContentView(R.layout.apply_message);

        findViewById(R.id.applyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog01();
            }
        });
    }

    public void showDialog01() {
        dialog01.show();

        Button noButton = dialog01.findViewById(R.id.noButton);
        noButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog01.dismiss();
            }
        });
        dialog01.findViewById(R.id.yesButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}