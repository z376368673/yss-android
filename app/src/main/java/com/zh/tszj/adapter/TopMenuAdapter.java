package com.zh.tszj.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zh.tszj.activity.fragment.HomeItem1Fragment;
import com.zh.tszj.activity.fragment.HomeItem2Fragment;
import com.zh.tszj.base.BaseFragment;
import com.zh.tszj.bean.ShopClass;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息内容子页面适配器
 */
public class TopMenuAdapter extends FragmentPagerAdapter {
    private List<ShopClass> names;
    private int index = -1;
    public TopMenuAdapter(FragmentManager fm) {
        super(fm);
        this.names = new ArrayList<>();
    }

    public int getIndex() {
        if (names!=null)return names.get(index).id;
        return index;
    }

    /**
     * 数据列表
     *
     * @param datas
     */
    public void setList(List<ShopClass> datas) {
        this.names.clear();
        this.names.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        index = position;
        BaseFragment fragment;
        if (position==0){
            fragment = new HomeItem1Fragment();
        }else {
            fragment = new HomeItem2Fragment();
        }
        Bundle bundle = new Bundle();
        fragment.setClassId(names.get(position).id);
        bundle.putSerializable("key",names.get(position));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String plateName = names.get(position).cat_name;
        if (plateName == null) {
            plateName = "推荐";
        } else if (plateName.length() > 5) {
            plateName = plateName.substring(0, 5) + "...";
        }
        return plateName;
    }
}
