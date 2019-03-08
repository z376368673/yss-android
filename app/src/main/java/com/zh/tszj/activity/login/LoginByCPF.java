package com.zh.tszj.activity.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.zh.tszj.R;
import com.zh.tszj.base.BaseActivity;

/**
 * CPF登陆页
 */
public class LoginByCPF extends BaseActivity {

    EditText et_phone_number;
    EditText et_pwd;
    //登陆
    Button btn_login;
    ImageView iv_close;
    @Override
    protected int onLayoutResID() {
        return R.layout.act_login_by_cpf;
    }

    @Override
    public void onClick(View view) {
       if (btn_login == view) {
            loginByPwd(et_phone_number.getText().toString(), et_pwd.getText().toString());
        }else  if (iv_close == view) {
          finish();
       }
    }

    private void loginByPwd(String s, String s1) {
    }

    private void toForgetPwd() {
    }


    private void toLoginByPhone() {
    }


    @Override
    protected void initEvent(@Nullable Bundle savedInstanceState) {
        super.initEvent(savedInstanceState);
        btn_login.setOnClickListener(this);
        iv_close.setOnClickListener(this);
    }
}
