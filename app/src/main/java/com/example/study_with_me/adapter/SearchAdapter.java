package com.example.study_with_me.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.study_with_me.R;
import com.example.study_with_me.model.StudyGroup;
import com.example.study_with_me.model.Studydata;

import java.util.ArrayList;
import java.util.Map;

public class SearchAdapter extends BaseAdapter implements Filterable {
    Context context;
    LayoutInflater layoutInflater;
    // Adapter에 추가된 데이터를 저장하기 위한 ArrList(원본 데이터 리스트)
    private ArrayList<Studydata> StudydataList = new ArrayList<Studydata>();
    private ArrayList<Map<String, StudyGroup>> studyList = new ArrayList<>();

    // 필터링된 결과 데이터를 저장하기 위한 ArrList. 최초에는전체 리스트 보유
    private ArrayList<Studydata> filterStudyList = StudydataList;

    Filter listFilter;

    public SearchAdapter(Context context, ArrayList<Map<String, StudyGroup>> studyList) {
        this.context = context;
        this.studyList = studyList;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return studyList.size();
    }

    @Override
    public Object getItem(int position) {
        return studyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.study_search_item, null);

        TextView recruitTextView = (TextView) view.findViewById(R.id.studyRecuit);
        TextView fieldTextView = (TextView) view.findViewById(R.id.studyField);
        TextView titleTextView = (TextView) view.findViewById(R.id.studyTitle);
        TextView dateTextView = (TextView) view.findViewById(R.id.studyRegisterDay);

        boolean closed = new Boolean(String.valueOf(studyList.get(position).get("closed")));
        String currentRecruit;
        if (closed) {
            currentRecruit = "모집마감";
        } else {
            currentRecruit = "모집중";
        }
        recruitTextView.setText(currentRecruit);
        fieldTextView.setText(String.valueOf(studyList.get(position).get("type")));
        titleTextView.setText(String.valueOf(studyList.get(position).get("name")));
        dateTextView.setText(String.valueOf(studyList.get(position).get("endDate")));

        return view;
    }

    @Override
    public Filter getFilter() {
        if(listFilter == null) {
            listFilter = new ListFilter();
        }
        return listFilter;
    }

    private class ListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if(constraint == null || constraint.length() == 0) {
                results.values = StudydataList;
                results.count = StudydataList.size();
            } else {
                ArrayList<Studydata> itemList = new ArrayList<Studydata>();

                for(Studydata item : StudydataList) {
                    if(item.getStudyTitle().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        itemList.add(item);
                    }
                }
                results.values = itemList;
                results.count = itemList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filterStudyList = (ArrayList<Studydata>) results.values;

            if(results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}