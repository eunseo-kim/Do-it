package com.example.study_with_me.adapter;

import android.content.Context;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.study_with_me.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class UserCommentAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference reference = firebaseDatabase.getReference();
    DatabaseReference userRef;

    ArrayList<Map<String, Map>> userCommentList;
    ArrayList<String> userID = new ArrayList<>();
    ArrayList<String> userComment = new ArrayList<>();

    TextView userName;
    TextView comment;

    public UserCommentAdapter(Context context, ArrayList<Map<String, Map>> userCommentList) {
        this.context = context;
        this.userCommentList = userCommentList;
        this.layoutInflater = LayoutInflater.from(this.context);
        userRef = reference.child("users");


    }

    @Override
    public int getCount() { return userCommentList.size(); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public Map<String, Map> getItem(int position) { return userCommentList.get(position); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.user_info_evaluate_list_item, null);

        userName = view.findViewById(R.id.commentUserName);
        comment = view.findViewById(R.id.comment);

        Map<String, Map> userCommentMap = userCommentList.get(position);
        for(Map.Entry<String, Map> commentInfo : userCommentMap.entrySet()) {
            for(Object user : commentInfo.getValue().entrySet()) {
                userName.setText(user.toString().split("=")[0]);
                comment.setText(user.toString().split("=")[1]);
            }
        }

        return view;
    }
}
