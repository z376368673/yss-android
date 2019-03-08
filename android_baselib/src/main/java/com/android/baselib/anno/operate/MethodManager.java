package com.android.baselib.anno.operate;

import android.text.TextUtils;
import android.view.View;

import com.android.baselib.anno.annotation.UMethodInject;
import com.android.baselib.anno.bean.ViewInfoObj;
import com.android.baselib.log.ULog;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * 代理模式处理所有需要注解实现的方法
 *
 * @author PF-NAN
 * @date 2017/9/29
 */
public class MethodManager {
    /**
     * 控件方法监听缓存
     * ViewInfoObj:控件详情,包含控件ID,控件父ID
     * Class<?>:方法类型
     * Object :Proxy.newProxyInstance
     */
    private final static NPFMap<ViewInfoObj, Class<?>, Object> mMethodOperateCache = new NPFMap<>();
    /**
     * 实例
     */
    private static volatile MethodManager mInstance = null;

    private MethodManager() {
    }

    /**
     * 获取实例
     *
     * @return MethodManager
     */
    public static MethodManager getInstance() {
        if (null == mInstance) {
            synchronized (MethodManager.class) {
                if (null == mInstance) {
                    mInstance = new MethodManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 处理需要注解实现的方法
     *
     * @param obj         需要查找控件的类
     * @param view        对应的控件
     * @param viewInfoObj 包含控件id和其父控件id的实体
     * @param method      对应的事件
     * @param annotation  标记的方法注解 UMethodInject
     */
    public void addMethod(Object obj, View view, ViewInfoObj viewInfoObj, Method method, UMethodInject annotation) {
        try {
            if (null != view) {
                method.setAccessible(true);
                boolean isAddMethod = false;//是否添加方法到缓存
                Class<?> methodType = annotation.type();
                String interMethodName = annotation.interMethodName();

                String methodName = annotation.methodName();
                if (TextUtils.isEmpty(methodName)) {
                    methodName = String.format("set%1$s", methodType.getSimpleName());
                }
                Object listener = mMethodOperateCache.get(viewInfoObj, methodType);
                /**
                 * 缓存中存在此方法
                 */
                if (null != listener) {
                    /**
                     * 获取代理对象
                     */
                    MethodManagerHandler methodManagerHandler = (MethodManagerHandler) Proxy.getInvocationHandler(listener);
                    /**
                     * 判断当前对象和代理对象的委托对象是否为同一个
                     */
                    isAddMethod = obj.equals(methodManagerHandler.getObj());
                    if (isAddMethod) {
                        /**
                         * 如果是添加当前事件到代理方法缓存中
                         */
                        methodManagerHandler.addMethod(interMethodName, method);
                    }
                }
                if (!isAddMethod) {
                    MethodManagerHandler methodManagerHandler = new MethodManagerHandler(obj);
                    methodManagerHandler.addMethod(interMethodName, method);
                    listener = Proxy.newProxyInstance(methodType.getClassLoader(), new Class[]{methodType}, methodManagerHandler);
                    mMethodOperateCache.put(viewInfoObj, methodType, listener);
                }
                Method setListenerMethod = view.getClass().getMethod(methodName, methodType);
                setListenerMethod.invoke(view, listener);
            }
        } catch (Throwable throwable) {
            ULog.e(throwable);
        }
    }
}