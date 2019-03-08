package com.android.baselib.anno.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Layout绑定
 *
 * @author PF-NAN
 * @date 2017/9/26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ULayoutInject {
    /**
     * 布局id
     *
     * @return Layout Id
     */
    int value();
}
