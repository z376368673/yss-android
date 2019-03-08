package com.android.baselib.anno.operate;

import android.text.TextUtils;

import com.android.baselib.log.ULog;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


/**
 * 事件函数代理
 *
 * @author PF-NAN
 * @date 2017/9/29
 */
public class MethodManagerHandler implements InvocationHandler {
    /**
     * 点击事件的时间间隔
     */
    private final static long TIME_SPAN = 200;
    /**
     * 需要控制时间的操作事件
     */
    private final static HashSet<String> TIME_SPAN_METHOD = new HashSet<>(2);
    /**
     * 代理对象
     */
    private WeakReference<Object> mObj;
    /**
     * 点击事件最后的操作时间
     */
    private static long mLastClickTime = 0;
    private final HashMap<String, Method> mMethods = new HashMap<>(1);

    static {
        TIME_SPAN_METHOD.add("onClick");
        TIME_SPAN_METHOD.add("onItemClick");
    }

    /**
     * 弱引用实现数据关联
     *
     * @param obj
     */
    MethodManagerHandler(Object obj) {
        this.mObj = new WeakReference<>(obj);
    }

    /**
     * 添加需要代理的方法到集合
     *
     * @param interMethodName 事件回调的方法名称,例如setOnClickListener方法的回调方法onClick()
     * @param method          对应的函数
     */
    void addMethod(String interMethodName, Method method) {
        mMethods.put(interMethodName, method);
    }

    /**
     * 获取被代理的对象
     *
     * @return 被代理的对象
     */
    Object getObj() {
        return mObj.get();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object obj = getObj();
        if (null != obj) {
            String methodName = method.getName();
            if ("toString".equals(methodName)) {
                return MethodManagerHandler.class.getSimpleName();
            }
            method = mMethods.get(methodName);
            if (method == null && mMethods.size() == 1) {
                for (Map.Entry<String, Method> entry : mMethods.entrySet()) {
                    if (TextUtils.isEmpty(entry.getKey())) {
                        method = entry.getValue();
                    }
                    break;
                }
            }
            if (method != null) {
                if (TIME_SPAN_METHOD.contains(methodName)) {
                    long timeSpan = System.currentTimeMillis() - mLastClickTime;
                    if (timeSpan < TIME_SPAN) {
                        ULog.e("=====事件操作时间 < " + TIME_SPAN + "毫秒");
                        return null;
                    } else {
                        mLastClickTime = System.currentTimeMillis();
                    }
                }
                try {
                    return method.invoke(obj, args);
                } catch (Throwable ex) {
                    String msg = "检查 " + obj.getClass().getCanonicalName() + " 中控件ID是否与 xml 中一致!!!";
                    ULog.e(msg, ex);
                }
            } else {
                ULog.e("函数 " + methodName + "没有被实现, (" + obj.getClass().getCanonicalName() + ")");
            }
        }
        return null;
    }
}
