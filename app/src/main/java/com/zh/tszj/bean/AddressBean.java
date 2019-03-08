package com.zh.tszj.bean;

import java.io.Serializable;

/**
 * 地址信息
 */
public class AddressBean implements Serializable {

    /**
     * id : CA79FD7E1D594A5DB0175B86C077F6FC
     * user_id : 01C626337A8249BA9B0F662F3E6D007F
     * user_name : 小白
     * user_phone : 18291831184
     * areaId_path : null
     * area_id : null
     * user_address : 哈佛加入我额
     * is_default : 2
     * del_flag : 1
     * province : 陕西省
     * city : 西安市
     * county : 新城区
     * token : null
     * create_date : 2019-03-05 13:25:12
     */

    public String id;
    public String user_id;
    public String user_name;
    public String user_phone;
    public Object areaId_path;
    public Object area_id;
    public String user_address;
    public int is_default;
    public int del_flag;
    public String province;
    public String city;
    public String county = "";
    public Object token;
    public String create_date;

}
