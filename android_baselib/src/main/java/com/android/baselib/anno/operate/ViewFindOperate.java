package com.android.baselib.anno.operate;

import android.app.Activity;
import android.view.View;

/**
 * 控件查找操作
 *
 * @author PF-NAN
 * @date 2017/9/26
 */
public class ViewFindOperate {
    private View mView;
    private Activity mActivity;

    /**
     * View下操作控件
     *
     * @param view 控件所依附的View
     */
    public ViewFindOperate(View view) {
        this.mView = view;
    }

    /**
     * Activity下操作控件
     *
     * @param activity 控件所依附的Activity
     */
    public ViewFindOperate(Activity activity) {
        this.mActivity = activity;
    }


    /**
     * 查找控件
     *
     * @param viewId 控件ID
     * @return View
     */
    public View findViewById(int viewId) {
        View view = null;
        if (null != mView) {
            view = mView.findViewById(viewId);
        } else if (null != mActivity) {
            view = mActivity.findViewById(viewId);
        }
        return view;
    }

    /**
     * 查找控件
     *
     * @param viewId  控件ID
     * @param pViewId 父控件ID
     * @return View
     */
    public View findViewById(int viewId, int pViewId) {
        View pView = null;
        if (pViewId > 0) {
            pView = this.findViewById(pViewId);
        }
        View view = null;
        if (pView != null) {
            view = pView.findViewById(viewId);
        } else {
            view = this.findViewById(viewId);
        }
        return view;

    }


}
