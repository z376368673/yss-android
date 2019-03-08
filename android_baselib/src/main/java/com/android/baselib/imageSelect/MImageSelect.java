package com.android.baselib.imageSelect;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 提供给外界相册的调用的工具类
 *
 * @author PF-NAN
 * @date 2018/11/13
 */
public class MImageSelect {

    private WeakReference<Activity> mWeakActivity;

    private static MImageSelect mInstance;

    private MImageSelect() {
    }

    public static MImageSelect getInstance() {
        if (null == mInstance) {
            synchronized (MImageSelect.class) {
                if (null == mInstance) {
                    mInstance = new MImageSelect();
                }
            }
        }
        return mInstance;
    }

    /**
     * 图片选择的结果
     */
    public static final String SELECT_RESULT = "SELECT_RESULT";

    /**
     * 打开相册，选择图片,可多选,不限数量。
     *
     * @param activity
     * @param requestCode
     */
    public void openGallery(Activity activity, int requestCode) {
        openGallery(activity, requestCode, false, 0);
    }

    /**
     * 打开相册，选择图片,可多选,不限数量。
     *
     * @param activity
     * @param requestCode
     * @param selected    接收从外面传进来的已选择的图片列表。当用户原来已经有选择过图片，现在重新打开 选择器，允许用户把先前选过的图片传进来，并把这些图片默认为选中状态。
     */
    public void openGallery(Activity activity, int requestCode, ArrayList<String> selected) {
        openGallery(activity, requestCode, false, 0, selected);
    }

    /**
     * 打开相册，选择图片,可多选,限制最大的选择数量。
     *
     * @param activity
     * @param requestCode
     * @param isSingle       是否单选
     * @param maxSelectCount 图片的最大选择数量，小于等于0时，不限数量，isSingle为false时才有用。
     */
    public void openGallery(Activity activity, int requestCode, boolean isSingle, int maxSelectCount) {
        openGallery(activity, requestCode, isSingle, maxSelectCount, null);
    }

    /**
     * 打开相册，选择图片,可多选,限制最大的选择数量。
     *
     * @param activity
     * @param requestCode
     * @param isSingle       是否单选
     * @param maxSelectCount 图片的最大选择数量，小于等于0时，不限数量，isSingle为false时才有用。
     * @param selected       接收从外面传进来的已选择的图片列表。当用户原来已经有选择过图片，现在重新打开 选择器，允许用户把先前选过的图片传进来，并把这些图片默认为选中状态。
     */
    public void openGallery(Activity activity, int requestCode, boolean isSingle, int maxSelectCount, ArrayList<String> selected) {
        mWeakActivity = new WeakReference<>(activity);
        MImageSelectorActivity.openActivity(mWeakActivity.get(), requestCode, isSingle, maxSelectCount, selected);
    }
}
