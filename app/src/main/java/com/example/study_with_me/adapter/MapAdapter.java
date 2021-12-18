package com.example.study_with_me.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.study_with_me.R;
import com.example.study_with_me.model.MapItem;
import java.util.ArrayList;

public class MapAdapter extends BaseAdapter {
    private ArrayList<MapItem> mapList = new ArrayList<>();
    Context context;
    LayoutInflater layoutInflater;


    public MapAdapter(Context context, ArrayList<MapItem> mapList) {
        this.context = context;
        this.mapList = mapList;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return mapList.size();
    }

    @Override
    public Object getItem(int position) {
        return mapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.attendance_map_search_item, null);
        TextView mapName = (TextView) view.findViewById(R.id.mapName);
        TextView mapRoad = (TextView) view.findViewById(R.id.mapRoad);

        mapName.setText(mapList.get(position).place_name);
        mapRoad.setText(mapList.get(position).road_address_name);

        return view;
    }
}