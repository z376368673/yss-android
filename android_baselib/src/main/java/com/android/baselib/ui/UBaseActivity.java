package com.android.baselib.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.android.baselib.R;
import com.android.baselib.log.ULog;
import com.android.baselib.scaner.tools.RxBarTool;
import com.android.baselib.ui.handler.UHandler;
import com.android.baselib.ui.inter.WeakHandlerInter;
import com.android.baselib.util.UAppManager;
import com.android.baselib.util.UBarUtil;

import java.lang.reflect.Field;

/**
 * 常规基类,控件绑定、事件回调、Handler回调、网络数据回调
 *
 * @author PF-NAN
 * @date 2018/11/8
 */
public abstract class UBaseActivity extends AppCompatActivity implements View.OnClickListener, WeakHandlerInter {
    /**
     * Handler
     */
    protected UHandler mHandler = new UHandler(this);
    private View mDefLoadingView;
    public UBaseActivity activity;
    public Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       RxBarTool.noTitle(this);

        //设置状态栏 貌似只有API 23 之后手机生效
 //       UBarUtil.setColorNoTranslucent(this, Color.parseColor("#000000"));
//        UBarUtil.setStatusBarLightMode(this, true);
        //   UBarUtil.setTranslucent(this);
        setContentView(onLayoutResID());
//        RxBarTool.setTransparentStatusBar(this);
        //Activity 管理
        UAppManager.getAppManager().addActivity(this);
        activity = this;
        context = this;
        initView(getIntent().getExtras());
        initEvent(getIntent().getExtras());
        initData(getIntent().getExtras());
    }

    /**
     * 初始化XML
     *
     * @return
     */
//    @IntRange(from = 1, to = Integer.MAX_VALUE)
    protected abstract int onLayoutResID();

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
                            Class<?> clazz = Class.forName(this.getPackageName() + ".R$id");
                            int viewResId = clazz.getField(fieldName).getInt(null);
                            field.set(this, this.findViewById(viewResId));
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
        setLoadingView();
        mDefLoadingView.setVisibility(View.VISIBLE);
    }

    @CallSuper
    public void onEnd(String url) {
        if (null != mDefLoadingView && mDefLoadingView.isShown()) {
            mDefLoadingView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置默认加载进度
     */
    private void setLoadingView() {
        if (null == mDefLoadingView) {
            int loadingViewResID = initLoadingView();
            mDefLoadingView = View.inflate(this, loadingViewResID == 0 ? R.layout.u_view_loading : loadingViewResID, null);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ViewGroup decorView = (ViewGroup) this.getWindow().getDecorView();
            decorView.addView(mDefLoadingView, params);
        }
    }

    @IntRange(from = 0, to = Integer.MAX_VALUE)
    protected int initLoadingView() {
        return 0;
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
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mLocalBroadcastReceiver = broadcastReceiver;
        IntentFilter intentFilter = new IntentFilter();
        if (null != actions && actions.length > 0) {
            for (String action : actions) {
                intentFilter.addAction(action);
            }
        }
        mLocalBroadcastManager.registerReceiver(mLocalBroadcastReceiver, intentFilter);
    }

    // 退出activity方法
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                back();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    // 可重写多态不同的退出方式
    protected void back() {
        finish();
    }

    protected void startTo(Class cls) {
        Intent intent = new Intent(activity, cls);
        startActivity(intent);
    }

    protected void startTo(Class cls, int requestCode) {
        Intent intent = new Intent(activity, cls);
        startActivityForResult(intent, requestCode);
    }

    protected void startTo(Class cls, int requestCode,String action) {
        Intent intent = new Intent(activity, cls);
        intent.setAction(action);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UAppManager.getAppManager().finishActivity(this);
        if (null != mLocalBroadcastManager && null != mLocalBroadcastReceiver) {
            mLocalBroadcastManager.unregisterReceiver(mLocalBroadcastReceiver);
        }
    }

}
