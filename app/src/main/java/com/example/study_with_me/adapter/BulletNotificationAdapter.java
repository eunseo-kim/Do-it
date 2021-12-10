package com.example.study_with_me.adapter;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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

import com.example.study_with_me.R;
import com.example.study_with_me.RealPathUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firestore.v1.StructuredQuery;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

    public void setItemImage(int position, Uri uri) {

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

        String filePath = bulletinList.get(position).get("fileUri").toString();

        String text = bulletinList.get(position).get("text").toString();
        textView.setText(text);

        /* set imageView */
        imageView = view.findViewById(R.id.bulletImageView);

        String imagePath = bulletinList.get(position).get("imageUri").toString();
        String contentPath = RealPathUtil.getPath(context, Uri.parse(imagePath));
        if(contentPath == null)
            Log.d("isNull", "yes");
        setImageView();

        Log.d("imagePath", imagePath);
        Log.d("return ", "return");

        return view;
    }

    private void setImageView() {
        Picasso.get()
                .load("https://firebasestorage.googleapis.com/v0/b/studywithme-b93ee.appspot.com/o/images%2F1638305437393.jpeg?alt=media&token=9368cfb5-c9aa-4160-b698-dc0d344eab36")
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(context.getApplicationContext(), "이미지", Toast.LENGTH_SHORT).show();
                        Log.d("이미지", "...");
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }

    private void setImageUri() {
        StorageReference ref
                = storageReference
                .child("images/" + registerTime + ".jpeg");

        ref.getDownloadUrl()
            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri downloadUrl) {
                    //이미지 로드 성공시
                    Toast.makeText(context.getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
                    if (downloadUrl != null) {
                        Picasso.get().load(downloadUrl.toString()).into(imageView);
                    }
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

    public static String getDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private void getRealPathFromURI(Uri contentURI) {
       String[] projection = new String[]{
               MediaStore.Images.Media._ID,
               MediaStore.Images.Media.DISPLAY_NAME,
               MediaStore.Images.Media.DATE_TAKEN
       };

       String selection = "${MediaStore.Images.Media.DATE_TAKEN} >= ?";
       String[] selectionArgs = new String[] {
               String.valueOf(registerTime)
       };

       String sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC";

       Cursor cursor = context.getContentResolver().query(
               MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
               projection,
               selection,
               selectionArgs,
               sortOrder
       );

        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        int dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);
        int displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);

        while(cursor.moveToNext()) {
            long id = cursor.getLong(idColumn);
            Uri uri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    String.valueOf(id)
            );
            Log.d("uri >>> ", uri.toString());
       }
    }

}
