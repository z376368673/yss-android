package com.android.baselib.ui.inter;

import android.os.Message;

/**
 * 主要处理基类中创建Handler导致内存泄露
 *
 * @author PF-NAN
 * @date 2018/11/2
 */
public interface WeakHandlerInter {
    void handleMessage(Message message);
}
