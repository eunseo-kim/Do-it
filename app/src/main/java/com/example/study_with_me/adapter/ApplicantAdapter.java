package com.example.study_with_me.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.study_with_me.R;
import com.example.study_with_me.model.Applicant;
import com.example.study_with_me.model.StudyGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ApplicantAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Applicant> applicants;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private String username;

    public ApplicantAdapter(Context context, ArrayList<Applicant> studyGroups) {
        this.context = context;
        this.applicants = studyGroups;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() { return applicants.size(); }

    @Override
    public Applicant getItem(int position) { return applicants.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.alarm_item, null);

        TextView studyTitle = (TextView) view.findViewById(R.id.studyTitle);
        TextView alarmMemberName = (TextView) view.findViewById(R.id.alarmMemberName);
        TextView alarmRegisterTime = (TextView) view.findViewById(R.id.alarmRegisterTime);

        databaseReference.child(applicants.get(position).getUserID())
                .addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username = snapshot.child("username").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        });

        studyTitle.setText(applicants.get(position).getStudyGroupTitle());
        alarmMemberName.setText(username);
        alarmRegisterTime.setText(applicants.get(position).getRegisterTime());

        return view;
    }
}
