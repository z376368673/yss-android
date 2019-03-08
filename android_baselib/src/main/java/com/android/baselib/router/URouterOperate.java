package com.android.baselib.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.android.baselib.log.ULog;

import java.lang.ref.WeakReference;

/**
 * 相关路由操作
 *
 * @author PF-NAN
 * @date 2018/11/7
 */
public class URouterOperate {

    private static URouterOperate mInstance = null;

    private URouterOperate() {
    }

    public static URouterOperate getInstance() {
        if (null == mInstance) {
            synchronized (URouterOperate.class) {
                if (null == mInstance) {
                    mInstance = new URouterOperate();
                }
            }
        }
        return mInstance;
    }

    /**
     * 通过路径获取Class
     *
     * @param classPath
     * @return
     */
    public Class getClass(@NonNull String classPath) {
        Class clazz = null;
        try {
            clazz = Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            ULog.e(e);
        }
        return clazz;
    }

    private WeakReference<Activity> mWeakActivity;
    private WeakReference<Context> mWeakContext;

    /**
     * Activity跳转
     *
     * @param bundle
     * @param activityPath
     * @param context
     */
    public void startActivity(@NonNull Bundle bundle, @NonNull String activityPath, @NonNull Context context) {
        try {
            mWeakContext = new WeakReference<>(context);
            Intent intent = new Intent();
            intent.setClassName(mWeakContext.get(), activityPath);
            intent.putExtras(bundle);
            mWeakContext.get().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ULog.e(e);
        }
    }

    /**
     * Activity跳转
     *
     * @param activityPath
     * @param context
     */
    public void startActivity(@NonNull String activityPath, @NonNull Context context) {
        try {
            mWeakContext = new WeakReference<>(context);
            Intent intent = new Intent();
            intent.setClassName(mWeakContext.get(), activityPath);
            mWeakContext.get().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ULog.e(e);
        }
    }

    /**
     * Activity跳转
     *
     * @param bundle
     * @param activityPath
     * @param activity
     * @param requestCode
     */
    public void startActivityForResult(@NonNull Bundle bundle, @NonNull String activityPath, @NonNull Activity activity, @IntRange(from = 1000, to = Integer.MAX_VALUE) int requestCode) {
        try {
            mWeakActivity = new WeakReference<>(activity);
            Intent intent = new Intent();
            intent.setClassName(mWeakActivity.get(), activityPath);
            intent.putExtras(bundle);
            mWeakActivity.get().startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
            ULog.e(e);
        }
    }

    /**
     * Activity跳转
     *
     * @param activityPath
     * @param activity
     * @param requestCode
     */
    public void startActivityForResult(@NonNull String activityPath, @NonNull Activity activity, @IntRange(from = 1000, to = Integer.MAX_VALUE) int requestCode) {
        try {
            mWeakActivity = new WeakReference<>(activity);
            Intent intent = new Intent();
            intent.setClassName(mWeakActivity.get(), activityPath);
            mWeakActivity.get().startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
            ULog.e(e);
        }
    }
}
