package com.android.baselib.util;

import android.content.Context;

/**
 * 描述：像素工具类
 */
public class UCommonUtils {
    public static int pic_corner=5;//图片圆角

    public static int zd_visible_height=3000;

    public static int dp2px(Context context, int dpVaule) {
        float density = context.getResources().getDisplayMetrics().density;
        int px = (int) (dpVaule * density + 0.5f);
        return px;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }



}
