package com.android.baselib.net2;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ServiceAPI {
    //密码登陆接口
    @GET("user/login?")
    Call<ResponseBody> textLoginByPwd(@Query("mobile") String tel, @Query("password") String password);

    /**
     * 单文件上传
     *
     * @param description
     * @param file        @Part MultipartBody.Part file 使用MultipartBody.Part类发送文件file到服务器
     * @return 状态信息String
     */
    @Multipart
    @POST("file/upFile")
    Call<ResponseBody> upFileSingle(@Part("description") RequestBody description, @Part MultipartBody.Part file);
//    // 上传单一文件
//    String des = "a image";
//    RequestBody description = RequestBody.create( MediaType.parse("multipart/form-data"), des);
//
//    RequestBody requestFile = RequestBody.create(MediaType.parse("text/plain"), new File("/sdcard/0/test.jpg"));
//    MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
//    Call<String> call = fileUpload.uploadFile(description, body);


    /**
     * 多文件上传：通过 List<MultipartBody.Part> 传入多个part实现
     *
     * @param parts 每一个part代表一个文件
     * @return 状态信息String
     */
    @Multipart
    @POST("file/upFile")
    Call<ResponseBody> uploadFilesMultipartBodyParts(@Part() List<MultipartBody.Part> parts);
//    // 上传多文件，参数：List<MultipartBody.Part> parts对象
//    ArrayList<File> list = new ArrayList<File>();
//    list.add(new File("/sdcard/0/test0.jpg"));
//    list.add(new File("/sdcard/0/test1.jpg"));
//
//    List<MultipartBody.Part> parts = new ArrayList<>(list.size());
//    for(File file : list) {
//        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
//        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
//        parts.add(part);
//    }
//    Call<String> call = fileUpload.uploadFilesMultipartBodyParts(parts);


    /**
     * 通过 MultipartBody和@body作为参数来实现多文件上传
     *
     * @param multipartBody MultipartBody包含多个Part
     * @return 状态信息String
     */
    @POST("UploadServerAddr")
    Call<ResponseBody> uploadFilesMultipartBody(@Body MultipartBody multipartBody);
//    // 上传多文件，参数：MultipartBody对象
//    ArrayList<File> list = new ArrayList<File>();
//    list.add(new
//    File("/sdcard/0/test0.jpg"));
//    list.add(new
//    File("/sdcard/0/test1.jpg"));
//    MultipartBody.Builder builder = new MultipartBody.Builder();
//    builder.setType(MultipartBody.FORM);
//    for( File file :list)
//    {
//        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
//        builder.addFormDataPart("image", file.getName(), requestBody);
//    }
//    MultipartBody multipartBody = builder.build(); //List<MultipartBody.Part> parts = builder.build().parts();
//    Call<String> call = fileUpload.uploadFilesMultipartBody(multipartBody);

}
