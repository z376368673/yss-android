package com.android.baselib.net2;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class ResultBean {
    /**
     * 泛型转化类
     */
    public String data;
    /**
     * 提示信息
     */
    public String msg;
    public String json;
    /**
     *  1：成功  其他都是错误
      */
    public int state;

    public <T extends Object>T getObjData(Class<T> tClass){
        return JSON.parseObject(data,tClass);
    }
    public <T extends Object>List<T> getListData(Class<T> tClass){
        return JSON.parseArray(data,tClass);
    }


}
