package com.zh.tszj.bean;


import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

public class AdvertBean implements Serializable {

    /**
     * id : 2CB1FD8044B24FF3816B9319D7BC24F3
     * create_date : 2019-02-19 16:00:43
     * sort : 1
     * link_url :
     * img_url : //carousel/1550564871110.jpg
     * update_date : 2019-02-19 16:00:43
     * del_flag : 1
     */
    @DatabaseField(columnName = "id", id = true)
    public String id;
    public String create_date;
    public int sort;
    public String link_url;
    public String img_url;
    public String update_date;
    public int del_flag;
    public int type;
}
