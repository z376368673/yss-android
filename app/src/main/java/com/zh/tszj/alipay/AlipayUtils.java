package com.zh.tszj.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.zh.tszj.alipay.util.OrderInfoUtil2_0;

import java.util.Map;

/**
 *  重要说明:
 *
 *  这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
 *  真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
 *  防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
 */

public class AlipayUtils {
    private Activity activity;
    /** 支付宝支付业务：入参app_id */
   private static  String APPID = "";
    //private static  String APPID = "2019030263424540";

    /** 支付宝账户登录授权业务：入参pid值 */
//    private static  String PID = "";
    private static  String PID = "2088431595535806";
    /** 支付宝账户登录授权业务：入参target_id值 */
    // private static  String TARGET_ID = "";
     private static  String TARGET_ID = "18291831184";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
      private static  String RSA2_PRIVATE = "";

//    private static  String RSA2_PRIVATE = "MIIEwAIBADANBgkqhkiG9w0BAQEFAASCBKowggSmAgEAAoIBAQDF+RGqaY1SnvdjZ9K54H8w6X918AyK17121Bg5kWMdzgfE0QJ5W+5Dpg7xmGndiAEwP642LbZqcMT/GbRc/Dsx8WUp3Lr3lx9eL31eSV4w5Q1AMbilVL9QSGpKU+8Nmz1iQnogFBgM9DEX1onE9LDwgewvPoHYKU+B+u/QM2aELVt25OEfFAcbZOat+4EU/iVh18dz/STgTe1K8jA02cE1E8oNAvoDstfUBzI5N+zKJcSQyQr0oft/MePqZO9fgqcDdkxDGQ3x2XHp56br8nKcZWMHaqz4EZXTSEajejlwFj2efNPSvjrBRstePCUhfL0wPnk5E2tiPKxZFSVUQj3TAgMBAAECggEBAIWY2QUJCeeRgxy/+YKaLvsQ94TdI3mmsSK5akefByYb3T5CVcsT8KwSKc97rSVkHHk04BmLq6gUxbvo2sBNLp07a6teW4Wto+tpXM797+RLDjAd3Z8km55P3/qcvZuPAVOHzOZM6RYEzARsMLiK78S9yGbxZao75n2um5sCtNHIojJFvQfdLw9jxmku9fG+B3pXDivGCijE1fMAA3rS+DdiyB033R8RK7uKPpThY5QIINSmyP2Qnd4Ys7ghen6pNXMZcZbpjBFhkUHtOeSqJ232Dvx+hTBzi2aXLpAldp1kQunE9tdsNPmoNuRo6u6Af7rLuZ7b5I2FMIEl0EIaqBECgYEA4a2fAJ2UQ07ts0H7P6s7/y7VwZwcEgmhfTd444N/jfP1HATe2sAqzPJsV2Fy7iS9kUwZhG50HUpep0Dq6JTqjgU/M8kz7TCtyGd6CMgmjuKqwuibluvw4EJM0HPFXuQDcbMfFJjN7P+pllNbTG7OGXs/UNQ63oTvjJoykc4Blx0CgYEA4JKAxc/Tv/mZYIUadYc7u/2ZvylYOTMh7Xdn5Y+XbKaNBas/iMUw5gcjKRuWUGHkaljAKd9G+0iMg8YrOwOTzU0tE93R/FQJh52A9roWZd9SeWR1Wvtw8xjGpCOS3v/TZlIB+S2UfyHwR6SY9S42BLbFaOia/NnpJU/rK3XL5a8CgYEA1Pb3xifuIrpX+8J3K7UWcBAtbYA4yRjHiReQ//o2o6mlE2TRPNL9UNwwOyFdyLdpILUUm3F0J7PnsKPFSehFk/IFm4PyeZFnXWewtJrUMCBcjoPdV8WdULOgM1Ic3hgD3AbxfIlaQp1c1twgmvcxjMBOlqNATn5aZG+a6xbhT00CgYEAxbpYZVLaZxI2KFSpekeqoZflkfmuMxbBTZsvwGDQe5qdWhtwolS0/CPWonAmxfmbKsOf1n4/uiojhjaqg5hfv4ivIunQ5HF9volALnykEegybq2z4nq29WOgKo8j1vK6yEF2eVXXhKR2Mi5LerzIVRgz8m2zYOPgsriOIELkVwMCgYEArQZ9ZcqTijD01SNKMVCPnjRkR9ICi/gKbOovudJc6EyOGAdY21sD1ZSls5Z8977xbWomWrNgmmRrMm+a8cO6aOMMl/bnR4vKF9dcPw+rDShm+qz3WAfOjCDC8l/WMQmc22pwgNfmieX5r9p5B/HNMLtAP11/Mp4sij0ONaNRZnY=";
    private static  String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    public AlipayUtils(Activity activity) {
        this.activity = activity;
    }

    public void  setAppID(String appId){
       APPID = appId;
       TARGET_ID = appId+appId;
   }
    public void  setRsaPrivate(String RSA_PRIVATE){
        this.RSA2_PRIVATE = RSA_PRIVATE;
        this.RSA_PRIVATE = RSA_PRIVATE;
   }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        if (resultCallback!=null)resultCallback.payResultSuccess(payResult);
                        Log.e("resultCallback", "支付成功: "+payResult.toString());
//                        Toast.makeText(activity, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        if (resultCallback!=null)resultCallback.payResultFail(payResult);
                        Log.e("resultCallback", "支付失败: "+payResult.toString());
//                        Toast.makeText(activity, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        if (resultCallback!=null)resultCallback.alipayAuthSuccess(authResult);
                        // 传入，则支付账户为该授权账户
                        Log.e("resultCallback", "授权成功: "+authResult.toString());
//                        Toast.makeText(activity,
//                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
//                                .show();
                    } else {
                        if (resultCallback!=null)resultCallback.alipayAuthFail(authResult);
                        // 其他状态值则为授权失败
                        Log.e("resultCallback", "授权失败: "+authResult.toString());
//                        Toast.makeText(activity,
//                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };
    
    /**
     * 支付宝账户授权业务
     */
    public void authV2() {
        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
                || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
                || TextUtils.isEmpty(TARGET_ID)) {
            new AlertDialog.Builder(activity).setTitle("警告").setMessage("需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
                    .setPositiveButton("确定", (dialoginterface, i) -> {
                    }).show();
            return;
        }
        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险； 
         *
         * authInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);
        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
        final String authInfo = info + "&" + sign;
        Runnable authRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(activity);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    /**
     * 支付宝支付业务
     */
    public void payV2() {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            new AlertDialog.Builder(activity).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {

                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险； 
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * get the sdk version. 获取SDK版本号
     *
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(activity);
        String version = payTask.getVersion();
        Toast.makeText(activity, version, Toast.LENGTH_SHORT).show();
    }


    // 支付宝支付 授权成功回调
    public interface   AlipayResultCallback{
        void  payResultSuccess(PayResult payResult);
        void  payResultFail(PayResult payResult);
        void  alipayAuthSuccess(AuthResult authResult);
        void  alipayAuthFail(AuthResult authResult);
    }
    AlipayResultCallback resultCallback;

    public void setResultCallback(AlipayResultCallback resultCallback) {
        this.resultCallback = resultCallback;
    }
}
