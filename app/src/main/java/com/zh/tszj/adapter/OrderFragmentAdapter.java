package com.zh.tszj.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.zh.tszj.activity.fragment.HomeItem1Fragment;
import com.zh.tszj.activity.fragment.HomeItem2Fragment;
import com.zh.tszj.activity.fragment.OrderFragment;
import com.zh.tszj.base.BaseFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息内容子页面适配器
 */
public class OrderFragmentAdapter extends FragmentPagerAdapter {
    private List<String> names;

    public OrderFragmentAdapter(FragmentManager fm) {
        super(fm);
        this.names = new ArrayList<>();
    }

    /**
     * 数据列表
     *
     * @param datas
     */
    public void setList(List<String> datas) {
        this.names.clear();
        this.names.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment fragment;
        fragment = new OrderFragment();
//        int id = names.get(position);
//        fragment.setClassId(id);
//        Bundle bundle = new Bundle();
//        bundle.putInt("name", id);
//        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String plateName = names.get(position);
        if (plateName == null) {
            plateName = "全部";
        } else if (plateName.length() > 5) {
            plateName = plateName.substring(0, 5) + "...";
        }
        return plateName;
    }
}
