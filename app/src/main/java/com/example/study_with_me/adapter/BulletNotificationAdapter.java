package com.example.study_with_me.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.study_with_me.R;
import com.example.study_with_me.model.GlideApp;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.bumptech.glide.annotation.GlideModule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class BulletNotificationAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Map<String, Object>> bulletinList;
    private Bitmap bitmap;
    private Uri imageUri;
    TextView userName, date, textView;
    Button fileView;
    ImageView imageView;
    long registerTime;

    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference fileRef;
    StorageReference imageRef;


    public BulletNotificationAdapter(Context context, ArrayList<Map<String, Object>> bulletinList) {
        this.context = context;
        this.bulletinList = bulletinList;
        this.layoutInflater = LayoutInflater.from(this.context);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        imageRef = storageReference.child("images/");
    }

    @Override
    public int getCount() {
        return bulletinList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Map<String, Object> getItem(int position) {
        return bulletinList.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.bullet_item, null);

        userName = (TextView)view.findViewById(R.id.userName);
        date = (TextView)view.findViewById(R.id.date);
        textView = (TextView)view.findViewById(R.id.textView);
        fileView = view.findViewById(R.id.fileView);

        /* set registerTime(milliSecond to Date) */
        registerTime = Long.parseLong(bulletinList.get(position).get("registerTime").toString());
        String dateString = getDate(registerTime, "yyyy/MM/dd HH:mm:ss");
        date.setText(dateString);

        /* set writer userName */
        String writerName = bulletinList.get(position).get("writerName").toString();
        userName.setText(writerName);

        /* set imageView */
        imageView = view.findViewById(R.id.imageView);
        String imagePath = bulletinList.get(position).get("imageUri").toString();
        Log.d("imagePath", imagePath);

        if (!imagePath.equals("") || imagePath != null) {
            setImageUri();
        }

        String filePath = bulletinList.get(position).get("fileUri").toString();

        String text = bulletinList.get(position).get("text").toString();
        textView.setText(text);
        return view;
    }

    private void setImageUri() {
        StorageReference ref
                = storageReference
                .child("images/" + registerTime + ".jpeg");

        ref.getDownloadUrl()
            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //이미지 로드 성공시
                    Toast.makeText(context.getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
                    Log.d("success", "성공");
                    Log.d("uri", uri.toString());

                    GlideApp.with(imageView.getContext())
                            .load(uri)
                            .into(imageView);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    //이미지 로드 실패시
                    Log.d("failure", "실패");
                    Toast.makeText(context.getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                }
            });
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

}
