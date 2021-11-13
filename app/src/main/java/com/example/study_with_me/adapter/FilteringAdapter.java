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
import com.example.study_with_me.model.Studydata;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FilteringAdapter extends BaseAdapter implements Filterable {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrList(원본 데이터 리스트)
    private ArrayList<Studydata> StudydataList = new ArrayList<Studydata>();
    // 필터링된 결과 데이터를 저장하기 위한 ArrList. 최초에는전체 리스트 보유
    private ArrayList<Studydata> filterStudyList = StudydataList;

    Filter listFilter;

    public FilteringAdapter() {

    }
    @Override
    public int getCount() {
        return filterStudyList.size();
    }

    @Override
    public Object getItem(int position) {
        return StudydataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.study_search_item , parent, false);
        }

        // 화면에 표시될 View로부터 위젯에 대한 참조 획득
        TextView recuitTextView = (TextView) convertView.findViewById(R.id.studyRegisterDay);
        TextView fieldTextView = (TextView) convertView.findViewById(R.id.studyField);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.studyTitle);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.studyRegisterDay);

        // Dataset(filteredItemdList) 에서 positiond에 위치한 데이터 참조 획득
        Studydata studydata = filterStudyList.get(position);

        recuitTextView.setText(studydata.getStudyTitle());
        fieldTextView.setText(studydata.getStudyField());
        titleTextView.setText(studydata.getStudyTitle());
        dateTextView.setText(studydata.getStudydate());

        return convertView;
    }

    public void addItem(String recuit, String field, String title, String date) {
        Studydata item = new Studydata();

        item.setRecuit(recuit);
        item.setTitle(title);
        item.setField(field);
        item.setDate(date);

        StudydataList.add(item);
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