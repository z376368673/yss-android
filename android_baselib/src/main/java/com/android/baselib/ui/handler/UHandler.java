package com.android.baselib.ui.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.android.baselib.ui.inter.WeakHandlerInter;

import java.lang.ref.WeakReference;

/**
 * 防止内存泄漏Handler处理
 *
 * @author PF-NAN
 * @date 2018/11/2
 */
public class UHandler extends Handler {

    private WeakReference<WeakHandlerInter> mWeakHandlerInter;

    public UHandler(WeakHandlerInter weakHandlerInter) {
        super(Looper.getMainLooper());
        mWeakHandlerInter = new WeakReference<>(weakHandlerInter);
    }

    @Override
    public void handleMessage(Message message) {
        super.handleMessage(message);
        if (null != mWeakHandlerInter && null != mWeakHandlerInter.get()) {
            mWeakHandlerInter.get().handleMessage(message);
        }
    }
}
