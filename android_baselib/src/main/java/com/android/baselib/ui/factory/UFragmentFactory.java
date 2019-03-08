package com.android.baselib.ui.factory;

import android.support.v4.app.Fragment;
import android.util.SparseArray;

import com.android.baselib.log.ULog;

/**
 * 子fragment工厂
 *
 * @author PF-NAN
 * @date 2018/11/8
 */
public class UFragmentFactory<T extends Fragment> {
    private static UFragmentFactory mInstance = null;
    private SparseArray<T> mFragmentCache = new SparseArray<>();//缓存fragment

    public static UFragmentFactory getInstance() {
        if (null == mInstance) {
            mInstance = new UFragmentFactory();
        }
        return mInstance;
    }


    /**
     * 创建子Fragment缓存,并获取
     *
     * @param position
     * @param clazz
     * @param inter
     * @return
     */
    public void create(int position, Class<T> clazz, CreateInter inter) {
        T childFragment = mFragmentCache.get(position);
        if (childFragment == null) {
            try {
                childFragment = clazz.newInstance();
                mFragmentCache.put(position, childFragment);
                inter.firstCreate(position, childFragment);
            } catch (Exception e) {
                ULog.e(e);
            }
        }
    }


    public interface CreateInter {
        void firstCreate(int position, Fragment childFragment);
    }


    /**
     * 获取缓存的fragment
     *
     * @return
     */
    public SparseArray<T> getCache() {
        return mFragmentCache;
    }


    /**
     * 清空缓存
     */
    public void clearCache() {
        mFragmentCache.clear();
    }
}
