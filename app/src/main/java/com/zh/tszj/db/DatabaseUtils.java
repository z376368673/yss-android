package com.zh.tszj.db;

import android.content.Context;

public class DatabaseUtils {
    private static  MyOpenHelper mHelper;
    public static final  String DATA = "data.db";
    public static final  String CACHE = "cache.db";
    private DatabaseUtils(){
    }

    /**
     * 一般来说这里的initHelper放到application中去初始化
     * 当然也可以在项目运行阶段初始化
     */
    public static void initHelper(Context context, String name){
        if(mHelper == null){
            mHelper = new MyOpenHelper(context,name);
        }
    }
    public static MyOpenHelper getHelper(){
        if(mHelper == null){
            new RuntimeException("MyOpenHelper is null,No init it");
        }
        return mHelper;
    }
}

