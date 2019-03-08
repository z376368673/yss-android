package com.android.baselib.anno.operate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * K2与V为映射关系，K1与(K2、V)为映射关系的Map封装
 *
 * @author PF-NAN
 * @date 2017/9/29
 */
public class NPFMap<K1, K2, V> {
    private final ConcurrentHashMap<K1, ConcurrentHashMap<K2, V>> mDoubleKeyMap;

    public NPFMap() {
        this.mDoubleKeyMap = new ConcurrentHashMap<>();
    }

    /**
     * 存储操作
     *
     * @param key1       外层Key
     * @param key2       内层Key
     * @param innerValue 内层Value
     */
    public void put(K1 key1, K2 key2, V innerValue) {
        if (null == key1 || null == key2 || null == innerValue) {
            return;
        }
        if (mDoubleKeyMap.containsKey(key1)) {

            ConcurrentHashMap<K2, V> outValue = mDoubleKeyMap.get(key1);

            if (null == outValue) {
                outValue = new ConcurrentHashMap<>();
                outValue.put(key2, innerValue);
                mDoubleKeyMap.put(key1, outValue);
            } else {
                outValue.put(key2, innerValue);
            }
        } else {
            ConcurrentHashMap<K2, V> outValue = new ConcurrentHashMap<>();
            outValue.put(key2, innerValue);
            mDoubleKeyMap.put(key1, outValue);
        }
    }

    /**
     * 获取外层Map
     *
     * @param key1 外层Key
     * @return 外层Map
     */
    public ConcurrentHashMap<K2, V> get(K1 key1) {
        return mDoubleKeyMap.get(key1);
    }

    /**
     * 获取内层value
     *
     * @param key1 外层Key
     * @param key2 内层Key
     * @return 内层value
     */
    public V get(K1 key1, K2 key2) {
        ConcurrentHashMap<K2, V> outValue = mDoubleKeyMap.get(key1);
        return outValue == null ? null : outValue.get(key2);
    }

    /**
     * 是否包含key1
     *
     * @param key1 外层Key
     * @return 是否包含
     */
    public boolean containsKey(K1 key1) {
        return mDoubleKeyMap.containsKey(key1);
    }

    /**
     * 是否包含key1 和 key2
     *
     * @param key1 外层Key
     * @param key2 内层Key
     * @return 是否包含
     */
    public boolean containsKey(K1 key1, K2 key2) {
        if (mDoubleKeyMap.containsKey(key1)) {
            return mDoubleKeyMap.get(key1).containsKey(key2);
        } else {
            return false;
        }
    }

    /**
     * 获取总长度
     *
     * @return Map总长度
     */
    public int size() {
        int size = 0;
        if (mDoubleKeyMap.size() != 0) {
            for (ConcurrentHashMap<K2, V> outValue : mDoubleKeyMap.values()) {
                size += outValue.size();
            }
        }
        return size;
    }

    /**
     * 移除key1
     *
     * @param key1 外层Key
     * @return 被移除的Map
     */
    public ConcurrentHashMap<K2, V> remove(K1 key1) {
        return mDoubleKeyMap.remove(key1);
    }

    /**
     * 移除key1中的key2
     *
     * @param key1 外层Key
     * @param key2 内层Key
     */
    public void remove(K1 key1, K2 key2) {
        ConcurrentHashMap<K2, V> outValue = mDoubleKeyMap.get(key1);
        if (null != outValue) {
            outValue.remove(key2);
        }
        if (null == outValue || outValue.size() == 0) {
            mDoubleKeyMap.remove(key1);
        }
    }

    /**
     * 清空集合
     */
    public void clear() {
        if (mDoubleKeyMap.size() > 0) {
            for (ConcurrentHashMap<K2, V> outValue : mDoubleKeyMap.values()) {
                outValue.clear();
            }
            mDoubleKeyMap.clear();
        }
    }

    /**
     * 获取所有的K1
     *
     * @return 所有的外层Key集合
     */
    public Set<K1> getK1s() {
        return mDoubleKeyMap.keySet();
    }

    /**
     * 获取K1映射的(K2、V)中的所有V
     *
     * @param key1 外层Key
     * @return 所有外层Key对应的ConcurrentHashMap<K2, V>中的Value
     */
    public Collection<V> values(K1 key1) {
        ConcurrentHashMap<K2, V> outValue = mDoubleKeyMap.get(key1);
        return outValue == null ? null : outValue.values();
    }

    /**
     * 获取所有的V
     *
     * @return 所有的Value
     */
    public Collection<V> values() {
        Collection<V> values = null;
        Set<K1> k1s = getK1s();
        if (null != k1s) {
            values = new ArrayList<>();
            for (K1 k1 : k1s) {
                ConcurrentHashMap<K2, V> outValue = mDoubleKeyMap.get(k1);
                if (outValue != null) {
                    Collection<V> innerValues = outValue.values();
                    values.addAll(innerValues);
                }
            }
        }
        return values;
    }

}
