package com.zh.tszj.bean;


import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

public class ShopClass implements Serializable {

//    {
//        "id": 1,
//            "parent_id": "-1",
//            "cat_name": "茶道",
//            "is_show": 1,
//            "cat_sort": null,
//            "create_date": "2019-02-25 17:12:29",
//            "cat_pic": null
//    }
    @DatabaseField(columnName = "id", id = true)
    public  int id = -1 ;
    public  int parent_id = -1;
    public  String cat_name ="推荐";
    public  int is_show = 1;
    public  int cat_sort ;
    public  String create_date ;
    public  String cat_pic ;

    public ShopClass(int id, String cat_name, int is_show) {
        this.id = id;
        this.cat_name = cat_name;
        this.is_show = is_show;
    }

    public ShopClass() {
    }
}
