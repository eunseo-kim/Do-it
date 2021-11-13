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
    private ArrayList<Map<String, Object>> studyList = new ArrayList<>();

    // 필터링된 결과 데이터를 저장하기 위한 ArrList. 최초에는전체 리스트 보유
    private ArrayList<Map<String, Object>> filterStudyList;

    Filter listFilter;

    public SearchAdapter(Context context, ArrayList<Map<String, Object>> studyList) {
        this.context = context;
        this.studyList = studyList;
        this.layoutInflater = LayoutInflater.from(this.context);
        this.filterStudyList = this.studyList;
    }

    public SearchAdapter() {
    }

    @Override
    public int getCount() {
        return filterStudyList.size();
    }

    @Override
    public Object getItem(int position) {
        return filterStudyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.study_search_item , parent, false);
        }

        TextView recruitTextView = (TextView) convertView.findViewById(R.id.studyRecuit);
        TextView fieldTextView = (TextView) convertView.findViewById(R.id.studyField);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.studyTitle);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.studyRegisterDay);

        if(filterStudyList.size() > position) {
            Map<String, Object> studyGroupMap = filterStudyList.get(position);

            boolean closed = new Boolean(String.valueOf(filterStudyList.get(position).get("closed")));
            String currentRecruit;
            if (closed) {
                currentRecruit = "모집마감";
            } else {
                currentRecruit = "모집중";
            }
            recruitTextView.setText(currentRecruit);
            fieldTextView.setText(String.valueOf(studyGroupMap.get("type")));
            titleTextView.setText(String.valueOf(studyGroupMap.get("name")));
            dateTextView.setText(String.valueOf(studyGroupMap.get("endDate")));
        }
        return convertView;
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
                results.values = studyList;
                results.count = studyList.size();
            } else {
                ArrayList<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();

                for(Map<String, Object> item: studyList) {
                    if(String.valueOf(item.get("name")).toUpperCase().contains(constraint.toString().toUpperCase())) {
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
            filterStudyList = (ArrayList<Map<String, Object>>) results.values;
            if(results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}