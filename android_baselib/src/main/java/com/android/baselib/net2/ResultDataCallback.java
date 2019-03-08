package com.android.baselib.net2;

import android.app.Activity;
import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.android.baselib.log.ULog;
import com.android.baselib.ui.UBaseActivity;

public abstract class ResultDataCallback implements ResultCallback {
    UBaseActivity act;
    Boolean isShow;
    public ResultDataCallback(Activity activity, boolean isShow){
        if (activity instanceof UBaseActivity){
            act = (UBaseActivity) activity;
        }
        this.isShow = isShow;
    }
    @Override
    public void onStart() {
        ULog.e("onStart");
        if (act!=null&&isShow){
            act.onStart("");
        }
    }
    @Override
    public void onProgress(int progress) {

    }
    @Override
    public void onError(String error) {
        ULog.e("onError");
    }

    @Override
    public void onResult(String json, String url,String msg) {
        ResultBean resultBean = JSON.parseObject(json,ResultBean.class);
        resultBean.json = json;
        onResult(resultBean,resultBean.msg);
    }
    public abstract void onResult(ResultBean bean, String msg);
    @Override
    public void onFail(String error) {
        ULog.e("onFail");
    }
    @Override
    public void onFinish() {
        ULog.e("onFinish");
        if (act!=null){
            act.onEnd("");
        }
    }
}
