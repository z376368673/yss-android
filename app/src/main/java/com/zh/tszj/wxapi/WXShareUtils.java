package com.zh.tszj.wxapi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zh.tszj.R;

public class WXShareUtils {
    private Activity activity;
    private IWXAPI api;
    public String  shareIcon = "http://www.qq.com";
    public String  webpageUrl = "http://www.qq.com";
    public String  title = "WebPage Title WebPage TitleWebPage Title WebPage Title";
    public String  description = "WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description WebPage";

    public WXShareUtils(Activity activity) {
        this.activity = activity;
        api = WXAPIFactory.createWXAPI(activity, Constants.APP_ID,false);
        api.registerApp(Constants.APP_ID);
    }
    public void WXshare() {
        BottomSheetDialog sheetDialog = new BottomSheetDialog(activity);
        sheetDialog.setCancelable(true);
        View view = LayoutInflater.from(activity).inflate(R.layout.view_share, null);
        sheetDialog.setContentView(view);
        View hy = view.findViewById(R.id.layout_hy);
        View pyq = view.findViewById(R.id.layout_pyq);
        View tv_cancel = view.findViewById(R.id.tv_cancel);
        hy.setOnClickListener(v -> {
            sheetDialog.dismiss();
            share(SendMessageToWX.Req.WXSceneSession);
        });
        pyq.setOnClickListener(v -> {
            sheetDialog.dismiss();
            share(SendMessageToWX.Req.WXSceneTimeline);
        });
        tv_cancel.setOnClickListener(v -> {
            sheetDialog.dismiss();
        });
        sheetDialog.show();
    }
    private void share(int scene) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webpageUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.share);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
//        req.userOpenId = getOpenId();
        req.transaction = "webpage" + System.currentTimeMillis();
        req.message = msg;
        req.scene = scene;
        api.sendReq(req);
    }

}
