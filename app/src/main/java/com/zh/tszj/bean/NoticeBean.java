package com.zh.tszj.bean;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

public class NoticeBean implements Serializable {
    /**
     * id : 111
     * title : 111
     * create_date : 2019-02-27 15:52:34
     * author : 111
     * type : 1
     * content : 111
     */
    @DatabaseField(columnName = "id", id = true)
    public String id;
    public String title;
    public String create_date;
    public String author;
    public int type;
    public String content;
}
