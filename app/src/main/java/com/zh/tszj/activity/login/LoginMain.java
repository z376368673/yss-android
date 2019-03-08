package com.zh.tszj.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.baselib.net2.HttpClient;
import com.android.baselib.net2.ResultBean;
import com.android.baselib.net2.ResultDataCallback;
import com.android.baselib.util.URegexUtil;
import com.android.baselib.util.UScreenUtil;
import com.android.baselib.util.UToastUtil;
import com.android.baselib.util.UToolsUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zh.tszj.R;
import com.zh.tszj.activity.MainActivity;
import com.zh.tszj.alipay.AlipayUtils;
import com.zh.tszj.alipay.AuthResult;
import com.zh.tszj.alipay.PayResult;
import com.zh.tszj.api.API;
import com.zh.tszj.base.BaseActivity;
import com.zh.tszj.config.CacheData;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 手机号码登陆页
 */
public class LoginMain extends BaseActivity {
//    RelativeLayout rel_layout;
    ImageView iv_gif;

    EditText et_phone_number;
    //获取验证码
    TextView btn_yzm;
    EditText et_yzm_code;
    //密码登陆
    EditText et_pwd;
    ImageView iv_yanjing;

    ImageView iv_left;
    ImageView iv_right;

    LinearLayout layout_left;
    LinearLayout layout_right;

    LinearLayout layout_yzm;
    LinearLayout layout_pwd;

    //密码登陆
    TextView btn_pwd_log;
    //忘记密码
    TextView btn_forget_pwd;

    LinearLayout layout_zhifu;
    LinearLayout layout_cpf;

    //登陆
    Button btn_login;

    TypeEnum typeEnum = TypeEnum.LOGIN_CODE;

    //验证码登陆 密码登陆 注册
    private enum TypeEnum {
        LOGIN_CODE,
        LOGIN_PWD,
        REGISTER,
    }
    AlipayUtils alipayUtils;
    @Override
    protected int onLayoutResID() {
        return R.layout.act_login_main;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//        //750*400
//        int weith = UScreenUtil.getScreenWidth();
//        //x : y = 750 : 400
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_gif.getLayoutParams();
//        params.height = weith*400/750;
//        iv_gif.setLayoutParams(params);
        Glide.with(this).load(R.mipmap.iv_login_top).apply(options).into(iv_gif);

        alipayUtils = new AlipayUtils(activity);
        alipayUtils.setResultCallback(new AlipayUtils.AlipayResultCallback() {
            @Override
            public void payResultSuccess(PayResult payResult) {}
            @Override
            public void payResultFail(PayResult payResult) { }

            @Override
            public void alipayAuthSuccess(AuthResult authResult) {
                startAliPayCall(authResult.getAuthCode());
            }
            @Override
            public void alipayAuthFail(AuthResult authResult) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (btn_yzm == view) {
            getYzmCode(et_phone_number.getText().toString());
        } else if (btn_pwd_log == view) {
            //切换登陆方式
            if (typeEnum == TypeEnum.LOGIN_CODE) {
                pwdAndCode(TypeEnum.LOGIN_PWD);
            } else {
                pwdAndCode(TypeEnum.LOGIN_CODE);
            }
        } else if (btn_login == view) {
            loginForYamAndPwd(typeEnum);
        } else if (layout_left == view) {
            pwdAndCode(TypeEnum.LOGIN_CODE);
        } else if (layout_right == view) {
            pwdAndCode(TypeEnum.REGISTER);
        } else if (layout_zhifu == view) {
            startAliPay();
//            UToastUtil.showToastLong("支付宝登陆正在开发中....");
        } else if (layout_cpf == view) {
            startTo(LoginByCPF.class, Activity.DEFAULT_KEYS_DIALER);
        } else if (btn_forget_pwd == view) {
            startTo(ForgetPwd.class, Activity.DEFAULT_KEYS_DIALER);
        }
    }

    /**
     * 支付宝
     */
    private void startAliPay() {
        Call<ResponseBody> call = HttpClient.getApi(API.class).alipayPage();
        HttpClient.enqueue(call, new ResultDataCallback(this, true) {
            @Override
            public void onResult(ResultBean bean, String error) {
                JSONObject jsonObj = JSON.parseObject(bean.data);
                String privateKey = jsonObj.getString("privateKey");
                String appId = jsonObj.getString("appId");

                alipayUtils.setAppID(appId);
                alipayUtils.setRsaPrivate(privateKey);
                alipayUtils.authV2();
//                alipayUtils.payV2();
            }
//
//            @Override
//            public void onResult(ResultBean bean, String error) {
//                if (bean.state == 1) {
//
//                }
//            }

            @Override
            public void onFail(String error) {
                super.onFail(error);
            }
        });
    }

    /**
     * 支付宝
     */
    private void startAliPayCall(String authcode) {
        Call<ResponseBody> call = HttpClient.getApi(API.class).alipayLogin(authcode);
        HttpClient.enqueue(call, new ResultDataCallback(this, true) {
            @Override
            public void onResult(ResultBean bean, String error) {
                Log.e("ResultBean", "onResult: "+bean.json);
                loginSuccessful(bean);
            }

            @Override
            public void onFail(String error) {
                super.onFail(error);
            }
        });
    }


    /**
     * 登陆按钮事件
     *
     * @param typeEnum
     */
    private void loginForYamAndPwd(TypeEnum typeEnum) {
        switch (typeEnum) {
            case LOGIN_CODE:
                loginByYzm(et_phone_number.getText().toString(), et_yzm_code.getText().toString());
                break;
            case LOGIN_PWD:
                loginByPwd(et_phone_number.getText().toString(), et_pwd.getText().toString());
                break;
            case REGISTER:
                regist(et_phone_number.getText().toString(), et_yzm_code.getText().toString(), et_pwd.getText().toString());
                break;
        }
    }

    /**
     * 注册
     *
     * @param number 手机号
     * @param code   验证码
     * @param pwd    密码
     */
    private void regist(String number, String code, String pwd) {
        if (TextUtils.isEmpty(number) || number.length() != 11) {
            UToastUtil.showToastShort("请输入11位手机号码");
            return;
        } else if (TextUtils.isEmpty(code) || code.length() < 4) {
            UToastUtil.showToastShort("请输入至少4位验证码");
            return;
        } else if (TextUtils.isEmpty(pwd) || pwd.length() < 6) {
            UToastUtil.showToastShort("请输入至少6位密码");
            return;
        }
        Call<ResponseBody> call = HttpClient.getApi(API.class).userRegister(number, code, pwd);
        HttpClient.enqueue(call, new ResultDataCallback(this, true) {
            @Override
            public void onResult(ResultBean bean, String error) {
                loginSuccessful(bean);
            }

            @Override
            public void onFail(String error) {
                super.onFail(error);
            }
        });
    }

    /**
     * 获取验证码
     */
    private void getYzmCode(String phone) {
        if (TextUtils.isEmpty(phone) || phone.length() != 11) {
            UToastUtil.showToastShort("请输入11位手机号码");
            return;
        }
        Call<ResponseBody> call = HttpClient.getApi(API.class).sendSms(phone);
        HttpClient.enqueue(call, new ResultDataCallback(this, true) {
            @Override
            public void onResult(ResultBean bean, String error) {
                countDowmTime();
            }

            @Override
            public void onFail(String error) {
                super.onFail(error);
            }

            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
    }

    /**
     * 验证码登陆
     *
     * @param number
     * @param code
     */
    private void loginByYzm(String number, String code) {
        onStart("");
        if (TextUtils.isEmpty(number) || number.length() != 11) {
            UToastUtil.showToastShort("请输入11位手机号码");
            return;
        } else if (TextUtils.isEmpty(code) || code.length() < 4) {
            UToastUtil.showToastShort("请输入至少4位验证码");
            return;
        }
        Call<ResponseBody> call = HttpClient.getApi(API.class).loginByYzm(number, code);
        HttpClient.enqueue(call, new ResultDataCallback(this, true) {
            @Override
            public void onResult(ResultBean bean, String error) {
                loginSuccessful(bean);
            }

            @Override
            public void onFail(String error) {
                super.onFail(error);
            }
            @Override
            public void onFinish() {
                super.onFinish();
                onEnd("");
            }
        });
    }

    /**
     * 登陆成功 处理
     *
     * @param bean
     */
    private void loginSuccessful(ResultBean bean) {
        if (bean.state == 1) {
            Log.e("Login", bean.data);
            CacheData.setTonken(bean.data);
            startTo(MainActivity.class);
            finish();
        } else {
            UToastUtil.showToastShort(bean.msg);
        }
    }

    /**
     * 密码登陆
     *
     * @param number
     * @param code
     */
    protected void loginByPwd(String number, String code) {

        if (TextUtils.isEmpty(number) || number.length() != 11) {
            UToastUtil.showToastShort("请输入11位手机号码");
            return;
        } else if (TextUtils.isEmpty(code) || code.length() < 6) {
            UToastUtil.showToastShort("请输入至少6位密码");
            return;
        }
        Call<ResponseBody> call = HttpClient.getApi(API.class).loginByPwd(number, code);
        // Call<ResponseBody> call = HttpClient.getApi(API.class).loginByPwd(number, code);
        HttpClient.enqueue(call, new ResultDataCallback(this, true) {
            @Override
            public void onResult(ResultBean bean, String error) {
                loginSuccessful(bean);
            }

            @Override
            public void onFail(String error) {
                super.onFail(error);
            }
        });
    }

    /**
     * 切换验证码登陆和密码登陆
     *
     * @param typeEnum
     */
    private void pwdAndCode(TypeEnum typeEnum) {
        this.typeEnum = typeEnum;
        switch (typeEnum) {
            case LOGIN_CODE:
                btn_pwd_log.setVisibility(View.VISIBLE);
                btn_forget_pwd.setVisibility(View.VISIBLE);
                layout_pwd.setVisibility(View.GONE);
                layout_yzm.setVisibility(View.VISIBLE);
                iv_left.setVisibility(View.VISIBLE);
                iv_right.setVisibility(View.INVISIBLE);
                btn_pwd_log.setText("密码登陆");
                break;
            case LOGIN_PWD:
                btn_pwd_log.setVisibility(View.VISIBLE);
                btn_forget_pwd.setVisibility(View.INVISIBLE);
                layout_pwd.setVisibility(View.VISIBLE);
                layout_yzm.setVisibility(View.GONE);
                btn_pwd_log.setText("验证码登陆");
                break;
            case REGISTER:
                btn_pwd_log.setVisibility(View.GONE);
                btn_forget_pwd.setVisibility(View.GONE);
                layout_pwd.setVisibility(View.VISIBLE);
                layout_yzm.setVisibility(View.VISIBLE);
                iv_left.setVisibility(View.INVISIBLE);
                iv_right.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void initEvent(@Nullable Bundle savedInstanceState) {
        super.initEvent(savedInstanceState);
        btn_pwd_log.setOnClickListener(this);
        btn_forget_pwd.setOnClickListener(this);
        btn_yzm.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        iv_yanjing.setOnClickListener(this);
        layout_left.setOnClickListener(this);
        layout_right.setOnClickListener(this);

        layout_zhifu.setOnClickListener(this);
        layout_cpf.setOnClickListener(this);

        iv_yanjing.setOnTouchListener((v, event) -> {
            if (et_pwd.getText().length() <= 1) return false;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance()); //密码可见
                    et_pwd.setSelection(et_pwd.getText().length());
                    break;
                case MotionEvent.ACTION_UP:
                    et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());//密码不可见
                    et_pwd.setSelection(et_pwd.getText().length());
                    break;
                case MotionEvent.ACTION_CANCEL:
                    et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());//密码不可见
                    et_pwd.setSelection(et_pwd.getText().length());
                    break;
            }
            return false;
        });
    }

    //忘记密码
    protected void toForgetPwd() {
//        startTo(ForgetPwd.class);
//        api = WXAPIFactory.createWXAPI(this, appId, false);// 获得IWXAPI实例
    }

    //去登陆
    protected void toLoginByPwd() {
        startTo(LoginByCPF.class);
        finish();
    }

    //去登陆
    protected void toLoginByPhone() {
        startTo(LoginMain.class);
        finish();
    }

    private void countDowmTime() {
        btn_yzm.setEnabled(false);
        CountDownTimer countDownTimer = new CountDownTimer(1000 * 60, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //如果除1000 则计算的时候显示 就有问题 /990 是考虑到了时差问题
                btn_yzm.setText(millisUntilFinished / 990 + " 秒");
            }

            @Override
            public void onFinish() {
                btn_yzm.setText("获取验证码");
                btn_yzm.setEnabled(true);
            }
        };
        countDownTimer.start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) return;
        //注册完成要自动登陆
        if (requestCode == 10) {

        }
    }

}
