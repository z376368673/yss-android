package com.android.baselib.anno.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 函数绑定
 *
 * @author PF-NAN
 * @date 2017/9/28
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UMethodInject {
    /**
     * 控件id集合
     *
     * @return id集合
     */
    int[] value();

    /**
     * 父控件id集合
     *
     * @return 父控件id集合
     */
    int[] parentId() default 0;

    /**
     * 方法类型，默认点击事件
     *
     * @return 事件类型
     */
    Class<?> type() default View.OnClickListener.class;

    /**
     * 设置控件绑定的事件名称:例如setOnClickListener
     *
     * @return 事件名称
     */
    String methodName() default "";

    /**
     * 如果type()的接口类型提供多个回调, 需要使用此参数指定回调方法名.
     *
     * @return 事件多个回调中指定的事件回调
     */
    String interMethodName() default "";
}
