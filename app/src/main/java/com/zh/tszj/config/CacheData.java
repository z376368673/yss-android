package com.zh.tszj.config;

import com.alibaba.fastjson.JSON;
import com.android.baselib.util.USpUtil;
import com.zh.tszj.bean.UserInfo;
import com.zh.tszj.db.DatabaseUtils;

import java.util.List;

public class CacheData {

    public static void setTonken(String token) {
        USpUtil uSpUtil = USpUtil.getInstance("user.db");
        uSpUtil.put("token", token);
    }

    public static String getToken() {
        USpUtil uSpUtil = USpUtil.getInstance("user.db");
        return uSpUtil.getString("token");
    }

    public static void saveUser(UserInfo userInfo){
        DatabaseUtils.getHelper().save(userInfo,true);
    }
    public static UserInfo getUser(){
        try{
            List<UserInfo> list =  DatabaseUtils.getHelper().queryAll(UserInfo.class);
            if (list!=null&&list.size()>0){
                return list.get(0);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return new UserInfo();
    }
}
