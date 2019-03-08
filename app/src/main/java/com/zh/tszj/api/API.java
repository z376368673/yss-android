package com.zh.tszj.api;

import com.android.baselib.net2.ServiceAPI;

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

public interface API{
    //http://192.168.0.26/

    /**
     * 文件上传
     */
    @POST("file/upFile")
    Call<ResponseBody> upFileSingle(@Body MultipartBody multipartBody);
//    // 上传单一文件
//    String des = "a image";
//    RequestBody description = RequestBody.create( MediaType.parse("multipart/form-data"), des);
//
//    RequestBody requestFile = RequestBody.create(MediaType.parse("text/plain"), new File("/sdcard/0/test.jpg"));
//    MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
//    Call<String> call = fileUpload.uploadFile(description, body);

    //密码登陆接口
    @GET("user/login?")
    Call<ResponseBody> loginByPwd(@Query("mobile") String tel, @Query("password") String password);

    //手机验证码登陆接口
    @GET("user/smsLogin?")
    Call<ResponseBody> loginByYzm(@Query("mobile") String mobile, @Query("code") String code);

    //用户注册接口
    @POST("user/userRegister?")
    Call<ResponseBody> userRegister(@Query("mobile") String mobile, @Query("code") String code, @Query("password") String password);

    //用户发送验证码接口
    @POST("user/sendSms?")
    Call<ResponseBody> sendSms(@Query("mobile") String mobile);

    //用户修改密码接口
    @POST("/user/updatePwd?")
    Call<ResponseBody> updatePwd(@Query("mobile") String mobile, @Query("password") String password);

    /**
     * 更新用户信息
     * @param token
     * @param avatar
     * @param sex           性别 男1 女2
     * @param nikename
     * @return
     */
    @POST("user/updateUserInfo?")
    Call<ResponseBody> updateUserInfo(@Query("token") String token,
                                      @Query("avatar") String avatar,
                                      @Query("sex") int sex,
                                      @Query("nikename") String nikename);

    /**
     * 支付宝授权登录
     */
    @POST("alipay/alipayLogin?")
    Call<ResponseBody> alipayLogin(@Query("authcode") String authcode);

    /**
     * 支付宝 跳转授权页面
     */
    @POST("alipay/alipayPage?")
    Call<ResponseBody> alipayPage();

    /**
     * 获取分类列表 默认值传 -1
     *
     * @param id
     * @return
     */
    @GET("data/goodsCatsData?")
    Call<ResponseBody> goodsCatsData(@Query("id") String id);


    /**
     * 轮播图  默认值传 -1
     *
     * @param type
     * @return
     */
    @GET("carousel/carouselData?")
    Call<ResponseBody> carouselData(@Query("type") String type);

    /**
     * 获取通知消息 list
     */
    @GET("notice/noticeData?")
    Call<ResponseBody> noticeData();

    /**
     * 获取商品 列表
     * <p>
     * type  shopId 非必填 所以只有写三个方法
     */
    @GET("goods/getGoods?")
    Call<ResponseBody> getGoods(@Query("typeOne") int typeOne,@Query("typeTwo") int typeTwo, @Query("curr") int curr, @Query("limit") int limit);

    @GET("goods/getGoods?")
    Call<ResponseBody> getGoods(@Query("shopId") String shopId, @Query("curr") int curr, @Query("limit") int limit);

    /**
     * 获取购物车数据
     */
    @GET("cart/cartData?")
    Call<ResponseBody> cartData(@Query("token") String token, @Query("curr") int curr, @Query("limit") int limit);


    /**
     * 购物车清除单个商品
     * @param token
     * @param goodsId 商品ID(多个ID，逗号分隔)
     * @return
     */
    @POST("cart/emptyGoodsByCart")
    Call<ResponseBody> deleteGoodByCart(@Query("token") String token,@Query("goodsId") String goodsId );

    /**
     * 清空购物车
      * @param token
     * @return
     */
    @POST("cart/emptyCart")
    Call<ResponseBody> emptyCart(@Query("token") String token);

    /**
     * 生成订单
     * @param token
     * @param total_money
     * @param real_total_money
     * @param goodsInfo
     * @return
     */
    @POST("order/addOrder")
    Call<ResponseBody> addOrder(@Query("token") String token,
                                @Query("total_money") String total_money,
                                @Query("real_total_money") String real_total_money,
                                @Query("goodsInfo") String goodsInfo);

    /**
     *
     * 根据状态 获取订单数据
     * @param token
     * @return
     */
    @POST("order/orderData")
    Call<ResponseBody> getOrderData(@Query("token") String token,@Query("order_status") String order_status);

    /**
     * 订单数据 获取刚生成的订单
     * @param token
     * @return
     */
    @POST("order/orderData")
    Call<ResponseBody> orderData(@Query("token") String token,@Query("id") String id);


    /**
     * 首页商家
     */
    @GET("shop/indexShops?")
    Call<ResponseBody> indexShops();

    /**
     * /shop/shopInfo
     * 根据ID查找商家信息
     */
    @GET("shop/shopInfo?")
    Call<ResponseBody> shopInfo(@Query("id") String id);

    /**
     * 推荐产品
     */
    @GET("goods/vipGoods?")
    Call<ResponseBody> vipGoods(@Query("type") int type, @Query("curr") int curr, @Query("limit") int limit);

    /**
     * 旗舰店数据
     */
    @GET("shop/vipShops?")
    Call<ResponseBody> vipShops(@Query("curr") int curr, @Query("limit") int limit);

    /**
     * 最新入驻商家数据
     */
    @GET("shop/newShops?")
    Call<ResponseBody> newShops( @Query("curr") int curr, @Query("limit") int limit);

    /**
     * 商家数据
     */
    @GET("shop/getShops?")
    Call<ResponseBody> getShops(@Query("type") String type, @Query("curr") int curr, @Query("limit") int limit);


    /**
     * 收货地址数据
     */
    @POST("userAddress/userAddressData?")
    Call<ResponseBody> userAddressData(@Query("token") String token, @Query("curr") int curr, @Query("limit") int limit);

    /**
     * 删除收货地址
     */
    @POST("userAddress/delUserAddress?")
    Call<ResponseBody> delUserAddress(@Query("token") String token, @Query("id") String id);

    /**
     * 默认收货地址
     */
    @POST("userAddress/selUserAddress?")
    Call<ResponseBody> selUserAddress(@Query("token") String token, @Query("id") String id);

    /**
     * 新增收货地址
     *
     * @param token      令牌
     * @param user_name  名字
     * @param user_phone 电话
     * @param province   省
     * @param city       市
     * @param county     区
     * @param is_default 是否选中 1 选中 2不选中
     * @return
     */
    @POST("userAddress/addUserAddress?")
    Call<ResponseBody> addUserAddress(@Query("token") String token,
                                      @Query("user_name") String user_name,
                                      @Query("user_phone") String user_phone,
                                      @Query("province") String province,
                                      @Query("city") String city,
                                      @Query("county") String county,
                                      @Query("user_address") String user_address,
                                      @Query("is_default") String is_default);

    /**
     * 更新收货地址
     *
     * @param token      令牌
     * @param id      id
     * @param user_name  名字
     * @param user_phone 电话
     * @param province   省
     * @param city       市
     * @param county     区
     * @param is_default 是否选中 1 选中 2不选中
     * @return
     */
    @POST("userAddress/updateUserAddress?")
    Call<ResponseBody> updateUserAddress(@Query("token") String token,
                                         @Query("id") String id,
                                         @Query("user_name") String user_name,
                                         @Query("user_phone") String user_phone,
                                         @Query("province") String province,
                                         @Query("city") String city,
                                         @Query("county") String county,
                                         @Query("user_address") String user_address,
                                         @Query("is_default") String is_default);

}
