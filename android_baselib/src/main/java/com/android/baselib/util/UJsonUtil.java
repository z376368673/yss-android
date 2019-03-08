package com.android.baselib.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.android.baselib.log.ULog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * json数据解析
 *
 * @author nanPengFei
 */
public class UJsonUtil {

    /**
     * 把一个map解析成为json字符串
     *
     * @param obj
     * @return String
     */
    public static String parseObjTJson(@NonNull Object obj) {
        try {
            Gson gson = new Gson();
            return gson.toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{}";
    }

    /**
     * 把一个json字符串解析成为对象
     *
     * @param json
     * @param cls
     * @return <T> T
     */
    public static <T> T parseJsonToBean(String json, Class<T> cls) {
        Gson gson = new Gson();
        T t = null;
        try {
            t = gson.fromJson(json, cls);
        } catch (Exception e) {
            e.printStackTrace();
            ULog.e("解析异常------", e);
        }
        return t;
    }

    /**
     * 把json字符串解析成为map
     *
     * @param json
     * @return HashMap<String                                                                                                                               ,                                                                                                                                                                                                                                                               Object>
     */
    public static HashMap<String, Object> parseJsonToMap(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();
        HashMap<String, Object> map = null;
        try {
            map = gson.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 把json字符串解析成为map
     *
     * @param json
     * @param type new TypeToken<HashMap<String, JsonObject>>(){}.getType()
     * @return HashMap<?                                                                                                                               ,                                                                                                                                                                                                                                                               ?>
     */
    public static HashMap<?, ?> parseJsonToMap(String json, Type type) {
        Gson gson = new Gson();
        HashMap<?, ?> map = gson.fromJson(json, type);
        return map;
    }

    /**
     * 把json字符串解析成为集合 params: new TypeToken<List<yourbean>>(){}.getType(),
     *
     * @param json
     * @param type new TypeToken<List<yourbean>>(){}.getType()
     * @return List<?>
     */
    public static List<?> parseJsonToList(String json, Type type) {
        Gson gson = new Gson();
        List<?> list = gson.fromJson(json, type);
        return list;
    }

    /**
     * 获取json串中某个字段的值!注意:只能获取同一层级的value
     *
     * @param json
     * @param key
     * @return String
     */
    public static String getFieldValue(String json, String key) {
        if (TextUtils.isEmpty(json))
            return null;
        if (!json.contains(key))
            return "";
        JSONObject jsonObject = null;
        String value = null;
        try {
            jsonObject = new JSONObject(json);
            value = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static JSONObject getJSONObj(String json){
        try {
            JSONObject  jsonObject = new JSONObject(json);
            return  jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



}