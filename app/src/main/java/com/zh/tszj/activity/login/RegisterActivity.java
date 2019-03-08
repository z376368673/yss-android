//package com.zh.tszj.activity.login;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.android.baselib.view.UNavigationBar;
//import com.zh.tszj.R;
//
//public class RegisterActivity extends LoginActivity {
//    UNavigationBar navigationBars;
//    EditText et_phone_number;
//    EditText et_yzm_code;
//    //获取验证码
//    TextView btn_yzm;
//    //注册
//    TextView btn_register;
//    //登陆
//    LinearLayout login;
//
//    @Override
//    protected int onLayoutResID() {
//        return R.layout.act_register_layout;
//    }
//
//    @Override
//    public void onClick(View view) {
//         if (btn_yzm==view){
//            getYzmCodeByreg();
//        }else if (btn_register==view){
//             register(et_phone_number.getText().toString(),et_yzm_code.getText().toString());
//        }else if (login==view){
//             toLoginByPhone();
//        }
//    }
//
//    private void toLoginByPhone() {
//    }
//
//    @Override
//    protected void initView(@Nullable Bundle savedInstanceState) {
//        super.initView(savedInstanceState);
////        navigationBars = findViewById(R.id.navigationBars);
////        navigationBars.setTitle("注册");
//    }
//
//    @Override
//    protected void initEvent(@Nullable Bundle savedInstanceState) {
//        super.initEvent(savedInstanceState);
//        btn_yzm.setOnClickListener(this);
//        btn_register.setOnClickListener(this);
//        login.setOnClickListener(this);
//    }
//
//}
