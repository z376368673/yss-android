package com.android.baselib.anno;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.baselib.anno.annotation.UFragmentInject;
import com.android.baselib.anno.annotation.ULayoutInject;
import com.android.baselib.anno.annotation.UMethodInject;
import com.android.baselib.anno.annotation.UViewInject;
import com.android.baselib.anno.bean.ViewInfoObj;
import com.android.baselib.anno.inter.LayoutInjectInter;
import com.android.baselib.anno.operate.MethodManager;
import com.android.baselib.anno.operate.ViewFindOperate;
import com.android.baselib.log.ULog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;


/**
 * 注解入口,需要在Activity的onCreate(),或Fragment的onCreateView()中调用.
 * 主要操作两件事情:
 * 1、将注入的layout布局id设置给类
 * 2、获取类中所有属性，并获取其注解的id值，并设置给该属性
 *
 * @author PF-NAN
 * @date 2017/9/26
 */
public class UNPFInject implements LayoutInjectInter {

    private static final HashSet<Class<?>> mInjectClass = new HashSet<>();

    /**
     * 初始化需要支持的对象
     */
    static {
        mInjectClass.add(Object.class);
        mInjectClass.add(Activity.class);
        mInjectClass.add(Fragment.class);
        try {
            mInjectClass.add(Class.forName("android.support.v4.app.Fragment"));
            mInjectClass.add(Class.forName("android.support.v4.app.FragmentActivity"));
        } catch (ClassNotFoundException e) {
            ULog.e(e);
        }
    }

    /**
     * 实例
     */
    private static volatile UNPFInject mInstance = null;

    private UNPFInject() {
    }

    /**
     * 获取类实例
     *
     * @return UNPFInject
     */
    public static UNPFInject getInstance() {
        if (null == mInstance) {
            synchronized (UNPFInject.class) {
                if (null == mInstance) {
                    mInstance = new UNPFInject();
                }
            }
        }
        return mInstance;
    }

    /**
     * Activity绑定
     *
     * @param activity Activity
     */
    @Override
    public void inject(Activity activity) {
        Class<?> classz = activity.getClass();
        activityLayoutInject(classz, activity);
        injectVAndM(activity, classz, new ViewFindOperate(activity));
    }

    /**
     * android.app.Fragment绑定
     *
     * @param fragment  android.app.Fragment
     * @param inflater  LayoutInflater
     * @param container ViewGroup
     * @return View
     */
    @Override
    public View inject(Fragment fragment, LayoutInflater inflater, ViewGroup container) {
        Class<?> classz = fragment.getClass();
        View view = fragmentLayoutInject(classz, inflater, container);
        injectVAndM(fragment, classz, new ViewFindOperate(view));
        return view;
    }

    /**
     * android.support.v4.app.Fragment绑定
     *
     * @param fragment  android.support.v4.app.Fragment
     * @param inflater  LayoutInflater
     * @param container ViewGroup
     * @return View
     */
    @Override
    public View inject(android.support.v4.app.Fragment fragment, LayoutInflater inflater, ViewGroup container) {
        Class<?> classz = fragment.getClass();
        View view = fragmentLayoutInject(classz, inflater, container);
        injectVAndM(fragment, classz, new ViewFindOperate(view));
        return view;
    }

    /**
     * View绑定
     *
     * @param view View
     */
    @Override
    public void inject(View view) {
        Class<? extends View> classz = view.getClass();
        injectVAndM(view, classz, new ViewFindOperate(view));
    }

    /**
     * Holder、自定义控件绑定
     *
     * @param object View依附类
     * @param view   View
     */
    @Override
    public void inject(Object object, View view) {
        Class<?> classz = object.getClass();
        injectVAndM(object, classz, new ViewFindOperate(view));
    }


    /**
     * 获取Activity中LayoutId
     *
     * @param classz   类字节码对象
     * @param activity Activity
     */
    private void activityLayoutInject(Class<?> classz, Activity activity) {
        try {
            ULayoutInject layoutInject = getLayoutInject(classz);
            if (null != layoutInject) {
                int layoutId = layoutInject.value();
                if (layoutId > 0) {
                    Method setContentView = classz.getMethod("setContentView", int.class);
                    setContentView.invoke(activity, layoutId);
                }
            }
        } catch (Throwable e) {
            ULog.e(e);
        }
    }

    /**
     * 获取Framgnet中LayoutId
     *
     * @param classz    类字节码对象
     * @param inflater  LayoutInflater
     * @param container ViewGroup
     * @return View
     */
    private View fragmentLayoutInject(Class<?> classz, LayoutInflater inflater, ViewGroup container) {
        View view = null;
        try {
            ULayoutInject layoutInject = getLayoutInject(classz);
            if (null != layoutInject) {
                int layoutId = layoutInject.value();
                if (layoutId > 0) {
                    view = inflater.inflate(layoutId, container, false);
                }
            }
        } catch (Throwable e) {
            ULog.e(e);
        }
        return view;
    }

    /**
     * 获取LayoutInject
     *
     * @param currentClass 类字节码对象
     * @return ULayoutInject
     */
    private ULayoutInject getLayoutInject(Class<?> currentClass) {
        if (null == currentClass || mInjectClass.contains(currentClass)) {
            ULog.e("当前类不存在");
            return null;
        }
        ULayoutInject annotation = currentClass.getAnnotation(ULayoutInject.class);
        if (null == annotation) {
            /**
             * 如果当前Class不存在注解,则获取其父类注解
             */
            annotation = getLayoutInject(currentClass.getSuperclass());
        }
        return annotation;
    }


    /**
     * 获取类中所有的属性进行解析
     *
     * @param obj         需要查找控件的类
     * @param aClass      需要查找控件的类字节码对象
     * @param findOperate 控件查询处理类
     */
    private void injectVAndM(Object obj, Class<?> aClass, ViewFindOperate findOperate) {
        if (null == obj || mInjectClass.contains(aClass)) {
            return;
        }
        injectVAndM(obj, aClass.getSuperclass(), findOperate);
        /**
         * 1、拿到所有的属性
         */
        Field[] fields = aClass.getDeclaredFields();
        if (null != fields && fields.length > 0) {
            /**
             * 2、针对属性进行过滤
             */
            for (Field field : fields) {
                Class<?> fieldType = field.getType();
                /**
                 * 不解析static、final、基本类型、集合
                 */
                boolean isContinue = Modifier.isFinal(fieldType.getModifiers()) || Modifier.isStatic(fieldType.getModifiers()) || fieldType.isPrimitive() || fieldType.isArray();
                if (isContinue) {
                    continue;
                }

                /**
                 * 3、获取属性上注解(View)
                 */
                UViewInject viewAnno = field.getAnnotation(UViewInject.class);
                try {
                    if (null != viewAnno) {
                        View view = findOperate.findViewById(viewAnno.value(), viewAnno.parentId());
                        if (null != view) {
                            field.setAccessible(true);
                            field.set(obj, view);
                        } else {
                            String msg = " 检查 " + obj.getClass().getCanonicalName() + " 中控件ID是否与 xml 中一致!!!";
                            ULog.e(msg);
                        }
                    }
                } catch (IllegalAccessException e) {
                    String msg = "检查 " + obj.getClass().getCanonicalName() + " 中控件ID是否与 xml 中一致!!!";
                    ULog.e(msg, e);
                }

                /**
                 * 4、获取属性上注解(Fragment)
                 */
                UFragmentInject fragmentAnno = field.getAnnotation(UFragmentInject.class);
                try {
                    if (null != fragmentAnno) {
                        if (obj instanceof FragmentActivity) {
                            android.support.v4.app.FragmentManager fragmentManager = ((FragmentActivity) obj).getSupportFragmentManager();
                            findV4Fragment(obj, field, fragmentAnno, fragmentManager);
                        } else if (obj instanceof android.support.v4.app.Fragment) {
                            android.support.v4.app.FragmentManager fragmentManager = ((android.support.v4.app.Fragment) obj).getFragmentManager();
                            findV4Fragment(obj, field, fragmentAnno, fragmentManager);
                        } else if (obj instanceof Activity) {
                            FragmentManager fragmentManager = ((Activity) obj).getFragmentManager();
                            findAppFragment(obj, field, fragmentAnno, fragmentManager);

                        } else if (obj instanceof Fragment) {
                            FragmentManager fragmentManager = ((Fragment) obj).getFragmentManager();
                            findAppFragment(obj, field, fragmentAnno, fragmentManager);
                        }
                    }
                } catch (IllegalAccessException e) {
                    String msg = "检查 " + obj.getClass().getCanonicalName() + " 中@FragmentInject映射ID是否与 xml 中一致!!!";
                    ULog.e(msg, e);
                }


            }
        }

        /**
         * 解析类中需要注解操作的方法
         */
        Method[] methods = aClass.getDeclaredMethods();
        if (null != methods && methods.length > 0) {
            for (Method method : methods) {
                /**
                 * 不需要解析静态事件
                 */
                boolean isContinue = Modifier.isStatic(method.getModifiers());
                if (isContinue) {
                    continue;
                }
                /**
                 * 获取方法注解值
                 */
                UMethodInject methodAnno = method.getAnnotation(UMethodInject.class);
                if (null != methodAnno) {
                    int[] values = methodAnno.value();
                    int[] parentIds = methodAnno.parentId();
                    int parentIdLeng = null == parentIds ? 0 : parentIds.length;
                    for (int i = 0; i < values.length; i++) {
                        int value = values[i];
                        int parentId = parentIdLeng > i ? parentIds[i] : 0;
                        if (value > 0) {
                            /**
                             * 包含控件id和其父控件id的实体
                             */
                            ViewInfoObj viewInfoObj = new ViewInfoObj(value, parentId);
                            /**
                             * 获取控件
                             */
                            View view = findOperate.findViewById(value, parentId);
                            /**
                             * 方法管理操作
                             */
                            MethodManager.getInstance().addMethod(obj, view, viewInfoObj, method, methodAnno);
                        }
                    }

                }
            }
        }
    }

    /**
     * android.app.Fragment设置
     *
     * @param obj             Object
     * @param field           Field
     * @param fragmentAnno    UFragmentInject
     * @param fragmentManager android.app.FragmentManager
     * @throws IllegalAccessException IllegalAccessException
     */
    private void findAppFragment(Object obj, Field field, UFragmentInject fragmentAnno, FragmentManager fragmentManager) throws IllegalAccessException {
        Fragment fragment = fragmentManager.findFragmentById(fragmentAnno.value());
        field.setAccessible(true);
        field.set(obj, fragment);
    }

    /**
     * android.support.v4.app.Fragment设置
     *
     * @param obj             Object
     * @param field           Field
     * @param fragmentAnno    UFragmentInject
     * @param fragmentManager android.support.v4.app.FragmentManager
     * @throws IllegalAccessException IllegalAccessException
     */
    private void findV4Fragment(Object obj, Field field, UFragmentInject fragmentAnno, android.support.v4.app.FragmentManager fragmentManager) throws IllegalAccessException {
        android.support.v4.app.Fragment fragment = fragmentManager.findFragmentById(fragmentAnno.value());
        field.setAccessible(true);
        field.set(obj, fragment);
    }
}