package com.example.study_with_me.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.study_with_me.R;
import com.example.study_with_me.activity.EvaluateMemberActivity;

import java.util.ArrayList;
import java.util.Map;

public class TeamEvaluationAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Map<String, String>> evalMemberList;

    public TeamEvaluationAdapter(Context context, ArrayList<Map<String, String>> evalMemberList) {
        this.context = context;
        this.evalMemberList = evalMemberList;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() { return evalMemberList.size(); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public Map<String, String> getItem(int position) { return evalMemberList.get(position); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.evaluate_member_item, null);
        ImageView memberImage = (ImageView) view.findViewById(R.id.evalMemberImage);
        TextView memberName = (TextView) view.findViewById(R.id.evalMemberName);
        Button evalBtn = (Button) view.findViewById(R.id.evalMemberBtn);

        memberImage.setImageResource(R.drawable.tmp_person_icon);
        //memberImage.setImageResource(evalMemberList.get(position).getMemberImage());
        String[] usernames = evalMemberList.get(position).values().toArray(new String[0]);
        memberName.setText(usernames[0]);

        evalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context c = view.getContext();
                Intent intent = new Intent(view.getContext(), EvaluateMemberActivity.class);
                intent.putExtra("userID", evalMemberList.get(position).keySet().toArray(new String[0])[0]);
                c.startActivity(intent);
            }
        });

        return view;
    }
}
