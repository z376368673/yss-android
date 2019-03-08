package com.android.baselib.anno.inter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 支持Activity、Fragment、View、Holder绑定.
 *
 * @author PF-NAN
 * @date 2017/9/26
 */
public interface LayoutInjectInter {
    /**
     * 注入Activity
     *
     * @param activity Activity
     */
    void inject(Activity activity);

    /**
     * android.app.Fragment注入
     *
     * @param fragment  android.app.Fragment
     * @param inflater  LayoutInflater
     * @param container ViewGroup
     * @return View
     */
    View inject(android.app.Fragment fragment, LayoutInflater inflater, ViewGroup container);

    /**
     * android.support.v4.app.Fragment注入
     *
     * @param fragment  android.support.v4.app.Fragment
     * @param inflater  LayoutInflater
     * @param container ViewGroup
     * @return View
     */
    View inject(android.support.v4.app.Fragment fragment, LayoutInflater inflater, ViewGroup container);


    /**
     * 注入View
     *
     * @param view View
     */
    void inject(View view);

    /**
     * 注入 Holder
     *
     * @param object Holder
     * @param view   View
     */
    void inject(Object object, View view);

}
