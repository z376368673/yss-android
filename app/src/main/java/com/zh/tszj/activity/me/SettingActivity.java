package com.zh.tszj.activity.me;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.baselib.util.UToastUtil;
import com.android.baselib.view.UNavigationBar;
import com.zh.tszj.R;
import com.zh.tszj.activity.login.LoginMain;
import com.zh.tszj.base.BaseActivity;
import com.zh.tszj.config.CacheData;

public class SettingActivity extends BaseActivity {

    UNavigationBar uNavigationBar;

    @Override
    protected int onLayoutResID() {
        return R.layout.act_me_setting;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        uNavigationBar.setTitle("设置");
        uNavigationBar.setBack(this);
    }

    /**
     * 账号与安全
     * @param view
     */
    public void accountSet(View view){
        if (TextUtils.isEmpty(CacheData.getToken())){
            startTo(LoginMain.class);
        }else
            startTo(AcoutSecureActivity.class);
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
