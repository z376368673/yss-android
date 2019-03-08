package com.zh.tszj.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.android.baselib.image.UImage;
import com.android.baselib.net2.HttpClient;
import com.android.baselib.net2.ResultBean;
import com.android.baselib.net2.ResultDataCallback;
import com.android.baselib.util.UToastUtil;
import com.android.baselib.view.UImageView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zh.tszj.R;
import com.zh.tszj.api.API;
import com.zh.tszj.base.BaseActivity;
import com.zh.tszj.bean.HomeStoreBean;
import com.zh.tszj.bean.UserInfo;
import com.zh.tszj.config.CacheData;
import com.zh.tszj.db.DatabaseUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 完善资料
 */
public class AddUserInfoAct extends BaseActivity {
    RadioGroup radioGroup;
    EditText et_phone_number;
    UImageView iv_head;
    ImageView iv_back;
    //登陆
    Button btn_login;
    int type = 0;

    @Override
    protected int onLayoutResID() {
        return R.layout.act_add_user_info;
    }

    @Override
    public void onClick(View view) {
        if (btn_login == view) {
            sumbit();
        } else if (iv_head == view) {
            selectPhoto();
        }
    }
    String name;
    private void sumbit(){
        onStart("");
        name =  et_phone_number.getText().toString();
        if (TextUtils.isEmpty(imagePath)){
            UToastUtil.showToastShort("请选择头像");
            return;
        }
       if (TextUtils.isEmpty(name.trim())){
           UToastUtil.showToastShort("请输入昵称");
           return;
       }
        OkHttpClient okHttpClient = new OkHttpClient();
        Log.d("imagePath", imagePath);
        File file = new File(imagePath);
        RequestBody image = RequestBody.create(MediaType.parse("image/png"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", imagePath, image)
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.0.26/file/upFile")
                .post(requestBody)
                .build();
        okhttp3.Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e("onResult", "onResult: "+call );
                onEnd("");
            }
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                //{"msg":"上传成功","imgUrl":"//carousel/1551835070541.jpg","imgPath":"http://120.79.217.49:8889///carousel/1551835070541.jpg","state":1}
                String json =  response.body().string();
                Log.e("onResult", "onResult: "+json );
                if (json.contains("imgUrl")){
                    String imgUrl =  JSON.parseObject(json).getString("imgUrl");
                    if (!TextUtils.isEmpty(imgUrl)&&imgUrl.length()>3)
                    updateUserInfo(imgUrl);
                }
            }
        });

    }

    /**
     *
     * 更新用户信息
     */
    protected void updateUserInfo(String imgUrl) {
        int sex = 1;
        if (R.id.radio_girl == radioGroup.getCheckedRadioButtonId() ){
            sex = 2;
        }
        Call<ResponseBody> call = HttpClient.getApi(API.class).updateUserInfo(CacheData.getToken(),imgUrl,sex,name);
        HttpClient.enqueue(call, new ResultDataCallback(this, false) {
            @Override
            public void onResult(ResultBean bean, String error) {
                if (bean.state == 1) {
                    CacheData.saveUser(bean.getObjData(UserInfo.class));
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    UToastUtil.showToastShort(error);
                }
            }
            @Override
            public void onFail(String error) {
                super.onFail(error);
                onEnd("");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                onEnd("");
            }
        });
    }



    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
    }

    @Override
    protected void initEvent(@Nullable Bundle savedInstanceState) {
        super.initEvent(savedInstanceState);
        btn_login.setOnClickListener(this);
        iv_head.setOnClickListener(this);
        iv_back.setOnClickListener(v -> finish());
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_man) {
                type = 0;
            } else {
                type = 1;
            }
        });
    }

    private void selectPhoto() {
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                .theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
//                .glideOverride(100, 100)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }
    String imagePath = "";
    //    ArrayList<String> photoList;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode != RESULT_OK) return;
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                // 图片、视频、音频选择结果回调
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                if (selectList==null||selectList.size()<1){
                    return;
                }
                if (selectList.size() > 0) {
                    UImage.getInstance().loadFile(this, selectList.get(0).getPath(), R.mipmap.ic_head_default, iv_head);
                    imagePath = selectList.get(0).getPath();
                } else {
                    UToastUtil.showToastShort("选择头像失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
