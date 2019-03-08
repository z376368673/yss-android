package com.zh.tszj.activity.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.baselib.view.UNavigationBar;
import com.zh.tszj.R;
import com.zh.tszj.base.BaseActivity;

/**
 * 手机号码登陆页
 */
public class ForgetPwd extends BaseActivity {
    UNavigationBar uNavigationBar;
    EditText et_phone_number;
    EditText et_yzm_code;
    //获取验证码
    TextView btn_yzm;
    EditText et_pwd;
    EditText et_again_pwd;
    //登陆
    Button btn_complete;

    @Override
    protected int onLayoutResID() {
        return R.layout.act_forget_pwd;
    }

    @Override
    public void onClick(View view) {
        if (btn_yzm == view) {
            getYzmCodeBylogin();
        } else if (btn_complete == view) {
            forgetPwd(et_phone_number.getText().toString(),
                    et_yzm_code.getText().toString(),
                    et_pwd.getText().toString(),
                    et_again_pwd.getText().toString());
        }
    }

    private void forgetPwd(String s, String s1, String s2, String s3) {
    }

    private void getYzmCodeBylogin() {

    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        uNavigationBar.setTitle("找回密码");
        uNavigationBar.setLeftText("取消");
        uNavigationBar.setLeftTextOnClick(v -> finish());
    }

    @Override
    protected void initEvent(@Nullable Bundle savedInstanceState) {
        super.initEvent(savedInstanceState);
        btn_yzm.setOnClickListener(this);
        btn_complete.setOnClickListener(this);

    }
}
