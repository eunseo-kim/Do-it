package com.example.study_with_me.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.study_with_me.R;
import com.example.study_with_me.activity.EvaluateMemberActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Map;

public class TeamEvaluationAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference reference = firebaseDatabase.getReference();
    DatabaseReference studyRef;
    DatabaseReference userRef;

    ArrayList<Map<String, String>> evalMemberList;
    ArrayList<String> cmpList = new ArrayList<>();
    String studyId;
    String username;

    public TeamEvaluationAdapter(Context context, ArrayList<Map<String, String>> evalMemberList, String studyId) {
        this.context = context;
        this.evalMemberList = evalMemberList;
        this.layoutInflater = LayoutInflater.from(this.context);
        this.studyId = studyId;
        studyRef = reference.child("studygroups");
        userRef = reference.child("users");

        userRef.child(firebaseAuth.getCurrentUser().getUid()).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                username = task.getResult().getValue().toString();
            }
        });
    }

    @Override
    public int getCount() { return evalMemberList.size(); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public Map<String, String> getItem(int position) { return evalMemberList.get(position); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null)
            view = layoutInflater.inflate(R.layout.evaluate_member_item, null);

        ImageView memberImage = (ImageView) view.findViewById(R.id.evalMemberImage);
        TextView memberName = (TextView) view.findViewById(R.id.evalMemberName);
        Button evalBtn = (Button) view.findViewById(R.id.evalMemberBtn);

        memberImage.setImageResource(R.drawable.tmp_person_icon);
        String[] usernames = evalMemberList.get(position).values().toArray(new String[0]);
        memberName.setText(usernames[0]);

        evalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context c = view.getContext();
                Intent intent = new Intent(view.getContext(), EvaluateMemberActivity.class);
                intent.putExtra("userID", evalMemberList.get(position).keySet().toArray(new String[0])[0]);
                intent.putExtra("username", username);
                intent.putExtra("studyID", studyId);
                c.startActivity(intent);
            }
        });
        String evalMemberId = evalMemberList.get(position).keySet().toArray(new String[0])[0];
        Log.d("eval ID >>>>>>> ", evalMemberId);
        setCmpList(evalMemberId, view, evalBtn);

        return view;
    }

    private void setCmpList(String evalMemberId, View view, Button evalBtn) {
        studyRef.child(studyId).child("evalMembers").child(evalMemberId).child("evaluating").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                cmpList = (ArrayList<String>) task.getResult().getValue();

                if(cmpList != null && cmpList.contains(firebaseAuth.getCurrentUser().getUid())) {
                     view.setClickable(false);
                     evalBtn.setClickable(false);
                     evalBtn.setText("평가 완료");
                     evalBtn.setBackgroundColor(Color.GRAY);
                }
            }
        });
    }
}
