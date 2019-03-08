package com.zh.tszj.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.baselib.scaner.tools.RxBarTool;
import com.zh.tszj.R;
import com.zh.tszj.base.BaseActivity;
import com.zh.tszj.activity.fragment.HomeFragment;
import com.zh.tszj.activity.fragment.HomeGwcFragment;
import com.zh.tszj.activity.fragment.HomeMeFragment;

public class MainActivity extends BaseActivity  implements TabHost.OnTabChangeListener {

    public FragmentTabHost tabHost;

    String[] tabName={"首页","购物车","我的"};
    int[] tabIcon={R.drawable.tab_home_home,R.drawable.tab_home_gwc,R.drawable.tab_home_me};
    Class mFragmentArray[]={ HomeFragment.class,HomeGwcFragment.class, HomeMeFragment.class};//InforFragment.class

    @Override
    protected int onLayoutResID() {
        return R.layout.act_main;
    }
    @Override
    public void onClick(View view) {

    }
    @Override
    public void onTabChanged(String s) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        tabHost = (FragmentTabHost)findViewById(R.id.tabHost);
        tabHost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        int count=mFragmentArray.length;
        for (int i=0;i<count;i++){
            TabHost.TabSpec tabSpec=tabHost.newTabSpec(tabName[i]).setIndicator(getIndicatorView(tabIcon[i],tabName[i]));
            tabHost.addTab(tabSpec,mFragmentArray[i],null);
        }
        tabHost.setCurrentTab(0);
    }

    private View getIndicatorView(int r,String name){
        View view=getLayoutInflater().inflate(R.layout.tab_item,null);
        ImageView image=view.findViewById(R.id.tab_image);
        TextView text=view.findViewById(R.id.tab_text);
        image.setImageResource(r);
        text.setText(name);
        return view;
    }

}
