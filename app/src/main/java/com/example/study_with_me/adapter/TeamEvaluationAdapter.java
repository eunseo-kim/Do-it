package com.example.study_with_me.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.study_with_me.R;
import com.example.study_with_me.activity.EvaluateMemberActivity;
import com.example.study_with_me.model.MemberSampledata;

import java.util.ArrayList;

public class TeamEvaluationAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<MemberSampledata> evalMemberList;

    public TeamEvaluationAdapter(Context context, ArrayList<MemberSampledata> evalMemberList) {
        this.context = context;
        this.evalMemberList = evalMemberList;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() { return evalMemberList.size(); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public MemberSampledata getItem(int position) { return evalMemberList.get(position); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.evaluate_member_item, null);

        ImageView memberImage = (ImageView) view.findViewById(R.id.evalMemberImage);
        TextView memberName = (TextView) view.findViewById(R.id.evalMemberName);
        Button evalBtn = (Button) view.findViewById(R.id.evalMemberBtn);

        memberImage.setImageResource(evalMemberList.get(position).getMemberImage());
        memberName.setText(evalMemberList.get(position).getName());
        evalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context c = view.getContext();
                Intent intent = new Intent(view.getContext(), EvaluateMemberActivity.class);
                c.startActivity(intent);
            }
        });

        return view;
    }
}
