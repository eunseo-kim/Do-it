package com.example.study_with_me.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;

public class BulletRegisterActivity extends AppCompatActivity {
    private ImageView imageIcon, fileIcon, uploadImage;
    private Button postButton;
    private CheckBox noticeButton;
    int SELECT_PICTURE = 200; // activity result code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bullet_register);

        // 상단 메뉴바 숨김
        getSupportActionBar().hide();

        imageIcon = findViewById(R.id.imageIcon);
        fileIcon = findViewById(R.id.fileIcon);
        postButton = findViewById(R.id.postButton);
        noticeButton = findViewById(R.id.noticeButton);
        uploadImage = findViewById(R.id.uploadImage);

        imageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });
    }

    private void imageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    uploadImage.setImageURI(selectedImageUri);
                }
            }
        }
    }

}
