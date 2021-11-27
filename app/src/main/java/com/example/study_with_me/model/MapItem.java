package com.example.study_with_me.model;

import java.io.Serializable;

//지도 검색 시 띄워줄 item 클래스
public class MapItem implements Serializable {
    public String place_name;
    public String road_address_name;
    public String x;
    public String y;

    public MapItem(String name, String road, String x, String y) {
        this.place_name = name;
        this.road_address_name = road;
        this.x = x;
        this.y = y;
    }
}