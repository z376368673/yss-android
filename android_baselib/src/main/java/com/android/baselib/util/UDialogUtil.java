package com.android.baselib.util;


import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

public class UDialogUtil {
    /**
     * Dialog显示工具
     *
     * @param view
     * @param style
     * @param anim
     * @param gravity
     * @param isTouchCancel
     * @return
     */
    public static Dialog getDialog(View view, int style, int anim, int gravity, boolean isTouchCancel) {
        Dialog dialog = new Dialog(view.getContext(), style);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        if (null != window) {
            window.setWindowAnimations(anim);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.x = 0;
            layoutParams.gravity = gravity;
            if (gravity == Gravity.BOTTOM) {
                layoutParams.width = UScreenUtil.getScreenWidth();
                layoutParams.height = LayoutParams.WRAP_CONTENT;
                window.getDecorView().setPadding(0, 0, 0, 0);
                window.setAttributes(layoutParams);
            }
            dialog.onWindowAttributesChanged(layoutParams);
        }
        dialog.setCanceledOnTouchOutside(isTouchCancel);
        dialog.setCancelable(isTouchCancel);
        return dialog;
    }

}
