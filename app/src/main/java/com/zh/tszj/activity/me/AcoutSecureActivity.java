package com.zh.tszj.activity.me;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.baselib.util.UToastUtil;
import com.android.baselib.view.UNavigationBar;
import com.zh.tszj.R;
import com.zh.tszj.base.BaseActivity;

public class AcoutSecureActivity extends BaseActivity {

    UNavigationBar uNavigationBar;
    LinearLayout layout_modify;
    LinearLayout layout_phone;
    TextView tv_phone;

    @Override
    protected int onLayoutResID() {
        return R.layout.act_me_act_secure;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        uNavigationBar.setTitle("账号与安全");
        uNavigationBar.setBack(this);

    }

    /**
     * 账号与安全
     * @param view
     */
    public void accountSet(View view){

    }
    /**
     * 清除缓存
     * @param view
     */
    public void clearCache(View view){
        onStart("");
        mHandler.sendEmptyMessageDelayed(1,1000);
    }

    @Override
    public void handleMessage(Message message) {
        super.handleMessage(message);
        onEnd("");
        UToastUtil.showToastShort("清除成功");
    }

    /**
     * 关于曰十三
     * @param view
     */
    public void aboutOur(View view){

    }

    @Override
    public void onClick(View v) {

    }
}
