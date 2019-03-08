package com.android.baselib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;
import android.text.TextUtils;

import com.android.baselib.UBaseApplication;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * SP维护
 *
 * @author PF-NAN
 * @date 2018/3/14.
 */
public class USpUtil {
    private static SimpleArrayMap<String, USpUtil> SP_UTILS_MAP = new SimpleArrayMap<>();
    private SharedPreferences sp;

    /**
     * 获取 SPUtil 实例
     *
     * @return
     */
    public static USpUtil getInstance() {
        return getInstance("");
    }

    /**
     * 获取 SPUtil 实例
     *
     * @param spName sp文件名.
     * @return
     */
    public static USpUtil getInstance(String spName) {
        if (TextUtils.isEmpty(spName)) {
            spName = "npfSpFile";
        }
        USpUtil lksputil = SP_UTILS_MAP.get(spName);
        if (lksputil == null) {
            lksputil = new USpUtil(spName);
            SP_UTILS_MAP.put(spName, lksputil);
        }
        return lksputil;
    }

    private USpUtil(final String spName) {
        sp = UBaseApplication.getApplication().getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    /**
     * 储存 String 类型.
     *
     * @param key   The key of sp.
     * @param value The value of sp.
     */
    public void put(@NonNull final String key, @NonNull final String value) {
        put(key, value, false);
    }

    /**
     * 储存 String 类型.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit
     */
    public void put(@NonNull final String key,
                    @NonNull final String value,
                    final boolean isCommit) {
        if (isCommit) {
            sp.edit().putString(key, value).commit();
        } else {
            sp.edit().putString(key, value).apply();
        }
    }

    /**
     * 获取 String 类型.
     *
     * @param key key.
     * @return value
     */
    public String getString(@NonNull final String key) {
        return getString(key, "");
    }

    /**
     * 获取 String 类型.
     *
     * @param key          key.
     * @param defaultValue 默认值.
     * @return value
     */
    public String getString(@NonNull final String key, @NonNull final String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    /**
     * 储存 int 类型.
     *
     * @param key   key
     * @param value value
     */
    public void put(@NonNull final String key, final int value) {
        put(key, value, false);
    }

    /**
     * 储存 int 类型
     *
     * @param key      key.
     * @param value    value.
     * @param isCommit
     */
    public void put(@NonNull final String key, final int value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putInt(key, value).commit();
        } else {
            sp.edit().putInt(key, value).apply();
        }
    }

    /**
     * 获取 int 类型值.
     *
     * @param key key
     * @return value
     */
    public int getInt(@NonNull final String key) {
        return getInt(key, -1);
    }

    /**
     * 获取 int 类型的值.
     *
     * @param key          key.
     * @param defaultValue 默认值.
     * @return value
     */
    public int getInt(@NonNull final String key, final int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    /**
     * 保存 long 类型值
     *
     * @param key
     * @param value
     */
    public void put(@NonNull final String key, final long value) {
        put(key, value, false);
    }

    /**
     * 保存 long类型值
     *
     * @param key
     * @param value
     * @param isCommit
     */
    public void put(@NonNull final String key, final long value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putLong(key, value).commit();
        } else {
            sp.edit().putLong(key, value).apply();
        }
    }

    /**
     * 获取long 类型值
     *
     * @param key
     * @return
     */
    public long getLong(@NonNull final String key) {
        return getLong(key, -1L);
    }

    /**
     * 获取long 类型值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public long getLong(@NonNull final String key, final long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    /**
     * 保存 float 类型值
     *
     * @param key
     * @param value
     */
    public void put(@NonNull final String key, final float value) {
        put(key, value, false);
    }

    /**
     * 保存 float 类型值
     *
     * @param key
     * @param value
     * @param isCommit
     */
    public void put(@NonNull final String key, final float value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putFloat(key, value).commit();
        } else {
            sp.edit().putFloat(key, value).apply();
        }
    }

    /**
     * 获取 float 类型值
     *
     * @param key
     * @return
     */
    public float getFloat(@NonNull final String key) {
        return getFloat(key, -1f);
    }

    /**
     * 获取 float 类型值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public float getFloat(@NonNull final String key, final float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    /**
     * 保存 boolean 类型值
     *
     * @param key
     * @param value
     */
    public void put(@NonNull final String key, final boolean value) {
        put(key, value, false);
    }

    /**
     * 保存 boolean 类型值
     *
     * @param key
     * @param value
     * @param isCommit
     */
    public void put(@NonNull final String key, final boolean value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putBoolean(key, value).commit();
        } else {
            sp.edit().putBoolean(key, value).apply();
        }
    }

    /**
     * 获取 boolean 类型值
     *
     * @param key
     * @return
     */
    public boolean getBoolean(@NonNull final String key) {
        return getBoolean(key, false);
    }

    /**
     * 获取 boolean 类型值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean getBoolean(@NonNull final String key, final boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    /**
     * 保存 set集合
     *
     * @param key
     * @param value
     */
    public void put(@NonNull final String key, @NonNull final Set<String> value) {
        put(key, value, false);
    }

    /**
     * 保存 set集合
     *
     * @param key
     * @param value
     * @param isCommit
     */
    public void put(@NonNull final String key,
                    @NonNull final Set<String> value,
                    final boolean isCommit) {
        if (isCommit) {
            sp.edit().putStringSet(key, value).commit();
        } else {
            sp.edit().putStringSet(key, value).apply();
        }
    }

    /**
     * 获取 set 集合
     *
     * @param key
     * @return
     */
    public Set<String> getStringSet(@NonNull final String key) {
        return getStringSet(key, Collections.<String>emptySet());
    }

    /**
     * 获取 set 集合
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public Set<String> getStringSet(@NonNull final String key,
                                    @NonNull final Set<String> defaultValue) {
        return sp.getStringSet(key, defaultValue);
    }

    /**
     * 通过 key 检查是否包含某个值.
     *
     * @param key
     * @return boolean 值
     */
    public boolean contains(@NonNull final String key) {
        return sp.contains(key);
    }

    /**
     * 通过 key 移除对应的值.
     *
     * @param key key.
     */
    public void remove(@NonNull final String key) {
        remove(key, false);
    }

    /**
     * 通过 key 移除对应的值.
     *
     * @param key      key.
     * @param isCommit
     */
    public void remove(@NonNull final String key, final boolean isCommit) {
        if (isCommit) {
            sp.edit().remove(key).commit();
        } else {
            sp.edit().remove(key).apply();
        }
    }

    /**
     * 获取sp 中所有储存的内容.
     *
     * @return all values
     */
    public Map<String, ?> getAll() {
        return sp.getAll();
    }

    /**
     * 清空sp 内容.
     */
    public void clear() {
        clear(false);
    }

    /**
     * 清空所有保存的内容.
     *
     * @param isCommit
     */
    public void clear(final boolean isCommit) {
        if (isCommit) {
            sp.edit().clear().commit();
        } else {
            sp.edit().clear().apply();
        }
    }


}
