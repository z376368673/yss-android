package com.android.baselib.util;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.baselib.R;
import com.android.baselib.UBaseApplication;
import com.android.baselib.log.ULog;


/**
 * 高效率显示Toast的方法，不需要等待已经弹出的Toast消失，即可显示新的Toast
 *
 * @author nanPengFei
 */
public class UToastUtil {
    public static Toast mToast;
    @SuppressLint("StaticFieldLeak")
    private static TextView tv_toastMsg;

    /**
     * 快速连续弹Toast
     *
     * @param msg 需要显示的内容
     */
    public static void showToastLong(final String msg) {
        try {
            UBaseApplication.getHandler().post(new Runnable() {
                @SuppressLint("ShowToast")
                @Override
                public void run() {
                    if (null == mToast || null == tv_toastMsg) {
                        mToast = Toast.makeText(UBaseApplication.getApplication(), "", Toast.LENGTH_LONG);
                        @SuppressLint("InflateParams")
                        View view = LayoutInflater.from(UBaseApplication.getApplication()).inflate(R.layout.u_view_toast, null);
                        tv_toastMsg = view.findViewById(R.id.tv_toastMsg);
                        mToast.setView(view);
                        mToast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, UScreenUtil.dp2px(150));
                    }
                    tv_toastMsg.setText(msg);
                    mToast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            ULog.e(e);
        }

    }

    /**
     * 快速连续弹Toast
     *
     * @param msg 需要显示的内容
     */
    public static void showToastShort(final String msg) {
        try {
            UBaseApplication.getHandler().post(new Runnable() {
                @SuppressLint("ShowToast")
                @Override
                public void run() {
                    if (mToast == null) {
                        mToast = Toast.makeText(UBaseApplication.getApplication(), "", Toast.LENGTH_SHORT);
                        @SuppressLint("InflateParams") View view = LayoutInflater.from(UBaseApplication.getApplication()).inflate(R.layout.u_view_toast, null);
                        tv_toastMsg = view.findViewById(R.id.tv_toastMsg);
                        mToast.setView(view);
                        mToast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, UScreenUtil.dp2px(150));
                    }
                    tv_toastMsg.setText(msg);
                    mToast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            ULog.e(e);
        }

    }
}
