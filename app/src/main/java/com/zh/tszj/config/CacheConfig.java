package com.zh.tszj.config;

import com.android.baselib.util.USpUtil;

public class CacheConfig {

    /**
     * 是否第一次进入
     */
    public static final String IS_FIRST = "is first";
    public static boolean isFirst(){
      return   USpUtil.getInstance().getBoolean(IS_FIRST,false);
    }
    public static void isFirstToNo(){
        USpUtil.getInstance().put(IS_FIRST,true);
    }

}
