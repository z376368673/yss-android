package com.zh.tszj.bean;


import java.io.Serializable;

public class BannerItem implements Serializable {
    public int pic;
    public int id;
    public String title;
    public String time;

    public BannerItem(){};

    public BannerItem(String title, int pic){
        this.pic=pic;
        this.title=title;
    }
    public BannerItem(int id, String title, int pic){
        this.id=id;
        this.pic=pic;
        this.title=title;
    }

    public BannerItem(String title, String time){
        this.title=title;
        this.time=time;
    }

}
