package com.example.study_with_me.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study_with_me.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class BulletRegisterActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference userRef = databaseReference.child("users");
    private DatabaseReference studyGroupRef = databaseReference.child("studygroups");
    private FirebaseAuth firebaseAuth;
    private String userID;
    private String studyGroupID;

    private ImageView imageIcon, fileIcon, imageView;
    private TextView fileView;
    private Button postButton;
    private CheckBox noticeButton;
    private EditText editText;
    private Uri imagePath;
    private boolean isNotice;   // 공지글인지 여부
    
    private Bitmap bitmap;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference fileRef;
    StorageReference imageRef;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bullet_register);
        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        Intent intent = getIntent();
        studyGroupID = intent.getStringExtra("studyGroupID");

        // initialise views
        imageIcon = findViewById(R.id.imageIcon);
        fileIcon = findViewById(R.id.fileIcon);
        postButton = findViewById(R.id.postButton);
        noticeButton = findViewById(R.id.noticeButton);
        imageView = findViewById(R.id.imageView);
        fileView = findViewById(R.id.fileView);
        editText = findViewById(R.id.editText);

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        imageRef = storageReference.child("images/");
        fileRef = storageReference.child("files/");

        // upload image button clicked
        imageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage(); // select image from mobile gallery
            }
        });

        // post button clicked
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPost();
            }
        });

        // 공지글 checkBox Click event
        isNotice = false;
        noticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNotice = ((CheckBox)view).isChecked();
            }
        });
    }


    // upload Post
    private void uploadPost() {
        if (bitmap==null && editText.getText().length()==0) {
            Toast.makeText(BulletRegisterActivity.this,
                    "게시물을 작성해주세요",
                    Toast.LENGTH_SHORT).show();
        } else {
            // firebase Storage에 파일 등록
            uploadImage();

            // realtime firebase에 [이미지 uri, 파일 uri, 작성자, 작성 시각, 게시글, 공지여부] 추가
            Map<String, Object> bulletinMap = new HashMap<>();
            bulletinMap.put("text", editText.getText().toString()); // 게시글
            bulletinMap.put("notice", isNotice);    // 공지인지
            String imageUri = imagePath.toString();
            bulletinMap.put("imageUri", imageUri); // imageUri
            bulletinMap.put("writer", userID);      // 작성자 userID
            bulletinMap.put("fileUri", "");      // fileUri (임의로 빈 문자열 전달 => !!수정해야 돼요!!)
            bulletinMap.put("registerTime", System.currentTimeMillis());
            Log.d("bulletinMap", bulletinMap.toString());

            studyGroupRef.child(studyGroupID).child("bulletinBoard").push().setValue(bulletinMap);

            finish();
        }
    }


    // Select Image method
    private void SelectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            imagePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                imagePath);
                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
    private void uploadImage()
    {
        if (imagePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(imagePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast.makeText(BulletRegisterActivity.this,
                                                    "Image Uploaded",
                                                    Toast.LENGTH_SHORT).show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(BulletRegisterActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }



}
