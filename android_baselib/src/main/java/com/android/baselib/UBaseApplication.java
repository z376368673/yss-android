package com.android.baselib;

import android.app.Application;
import android.os.Handler;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * @author: PF-NAN
 * @date: 2018/3/13.
 */
public class UBaseApplication extends Application {
    private static Handler mHandler;
    private static UBaseApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        mApplication = this;
        initBugly();
    }

    /**
     * 版本更新和bug收集
     */
    private  void initBugly(){
        String appid = "564d85567e";
        // CrashReport.initCrashReport(getApplicationContext(), appid, false);
        Bugly.init(getApplicationContext(), appid, false);
    }
    /**
     * 获取Handler
     * @return Handler
     */
    public static Handler getHandler() {
        return mHandler;
    }

    /**
     * 获取上下文
     *
     * @return
     */
    public static UBaseApplication getApplication() {
        return mApplication;
    }
}