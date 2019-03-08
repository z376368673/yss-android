package com.android.baselib.ui;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.baselib.log.ULog;
import com.android.baselib.ui.handler.UHandler;
import com.android.baselib.ui.inter.WeakHandlerInter;

import java.lang.reflect.Field;

/**
 * 常规基类,控件绑定、事件回调、Handler回调、网络数据回调
 * 与UBaseActivity一起使用
 *
 * @author PF-NAN
 * @date 2018/11/8
 */
public abstract class UBaseFragment extends Fragment implements View.OnClickListener, WeakHandlerInter{
    /**
     * 对应的Activity
     */
    protected UBaseActivity mActivity;
    /**
     * Handler
     */
    protected UHandler mHandler = new UHandler(this);
    protected View mRootView;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            if (null == mRootView) {
                mRootView = View.inflate(mActivity, onLayoutResID(), null);
            }
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ULog.eT("UBaseFragment", e);
        }
        return mRootView;
    }

    /**
     * 初始化XML
     *
     * @return
     */
    protected abstract int onLayoutResID();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof UBaseActivity) {
            mActivity = (UBaseActivity) getActivity();
        }
    }

    private boolean isViewPrepare = false;
    private boolean isFirstVisible = true;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!isViewPrepare) {
            isViewPrepare = true;
            if (getUserVisibleHint()) {
                if (isFirstVisible) {
                    onFirstVisible();
                    isFirstVisible = false;
                } else {
                    onVisible();
                }
            }
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isViewPrepare && isVisibleToUser) {
            if (isFirstVisible) {
                onFirstVisible();//第一次，且用户可见
                isFirstVisible = false;
            } else {
                onVisible(); //可见，但不是第一次
            }
        }
    }

    /**
     * 不是第一次进入fragment
     */
    protected void onVisible() {

    }

    /**
     * 第一次进入fragment
     */
    protected void onFirstVisible() {
        Bundle savedInstanceState = getArguments();
        initView(savedInstanceState);
        initEvent(savedInstanceState);
        initData(savedInstanceState);
    }

    /**
     * 控件初始化
     *
     * @param savedInstanceState
     */
    @CallSuper
    protected void initView(@Nullable Bundle savedInstanceState) {
        initViewResID();
    }

    /**
     * 初始化View资源
     */
    private void initViewResID() {
        Field[] fields = this.getClass().getDeclaredFields();
        if (null != fields && fields.length > 0) {
            for (Field field : fields) {
                try {
                    if (null != field) {
                        Class<?> fieldType = field.getType();
                        if (View.class.isAssignableFrom(fieldType)) {
                            field.setAccessible(true);
                            String fieldName = field.getName();
                            Class<?> clazz = Class.forName(mActivity.getPackageName() + ".R$id");
                            int viewResId = clazz.getField(fieldName).getInt(null);
                            field.set(this, mRootView.findViewById(viewResId));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ULog.e(e);
                }
            }
        }
    }

    /**
     * 事件初始化
     *
     * @param savedInstanceState
     */
    protected void initEvent(@Nullable Bundle savedInstanceState) {
    }

    /**
     * 数据 初始化
     *
     * @param savedInstanceState
     */
    protected void initData(@Nullable Bundle savedInstanceState) {
    }

    @CallSuper
    public void onStart(String url) {
        if (null != mActivity) {
            mActivity.onStart(url);
        }
    }

    @CallSuper
    public void onEnd(String url) {
        if (null != mActivity) {
            mActivity.onEnd(url);
        }
    }

    /*广播管理器*/
    protected LocalBroadcastManager mLocalBroadcastManager;
    /*广播接收器*/
    protected BroadcastReceiver mLocalBroadcastReceiver;

    /**
     * 注册广播
     *
     * @param broadcastReceiver
     * @param actions
     */
    @CallSuper
    protected void initReceiver(BroadcastReceiver broadcastReceiver, String... actions) {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(mActivity);
        mLocalBroadcastReceiver = broadcastReceiver;
        IntentFilter intentFilter = new IntentFilter();
        if (null != actions && actions.length > 0) {
            for (String action : actions) {
                intentFilter.addAction(action);
            }
        }
        mLocalBroadcastManager.registerReceiver(mLocalBroadcastReceiver, intentFilter);
    }


    protected void startTo(Class cls){
        Intent intent = new Intent(getContext(),cls);
        startActivity(intent);
    }


    protected void startTo(Class cls,int requestCode){
        Intent intent = new Intent(getContext(),cls);
        getActivity().startActivityFromFragment(this,intent,requestCode);
    }
    protected void startTo(Class cls,int requestCode,String action){
        Intent intent = new Intent(getContext(),cls);
        intent.setAction(action);
        getActivity().startActivityFromFragment(this,intent,requestCode);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mLocalBroadcastManager && null != mLocalBroadcastReceiver) {
            mLocalBroadcastManager.unregisterReceiver(mLocalBroadcastReceiver);
        }
    }
}
