package com.android.baselib.anno.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控件绑定
 *
 * @author PF-NAN
 * @date 2017/9/26
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UViewInject {
    /**
     * 控件ID
     *
     * @return 控件ID
     */
    int value();

    /**
     * 父控件ID,默认0
     *
     * @return 父控件ID
     */
    int parentId() default 0;
}
