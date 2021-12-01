package com.example.study_with_me.model;

public class Place {
    public String id;           // 장소 ID
    public String place_name;    // 장소명, 업체명
    public String category_name;   // 카테고리 이름
    public String category_group_code;    // 중요 카테고리만 그룹핑한 카테고리 그룹 코드
    public String category_group_name;   // 중요 카테고리만 그룹핑한 카테고리 그룹명
    public String phone;       // 전화번호
    public String address_name;   // 전체 지번 주소
    public String road_address_name;    // 전체 도로명 주소
    public String x;         // X 좌표값 혹은 longitude
    public String y;            // Y 좌표값 혹은 latitude
    public String place_url;    // 장소 상세페이지 URL
    public String distanc;      // 중심좌표까지의 거리. 단, x,y 파라미터를 준 경우에만 존재. 단위는 meter
}
