package com.zh.tszj.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.baselib.util.UScreenUtil;
import com.android.baselib.util.UToastUtil;
import com.zh.tszj.R;
import com.zh.tszj.activity.home.SearchActivity;
import com.zh.tszj.activity.home.ShopClassActivity;
import com.zh.tszj.adapter.TopMenuAdapter;
import com.zh.tszj.base.BaseFragment;
import com.zh.tszj.bean.ShopClass;
import com.zh.tszj.db.DatabaseUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {
    //    private UNavigationBar uNavigationBar;
    private TabLayout tabLayout;
    private LinearLayout layout_search;
    private LinearLayout layout_title;
    private LinearLayout ll_title;
    private ImageView iv_more;
    private ViewPager viewPager;

    private TopMenuAdapter topMenuAdapter;
    private List<ShopClass> names;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    protected int onLayoutResID() {
        return R.layout.fm_home_home;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        topMenuAdapter = new TopMenuAdapter(getChildFragmentManager());

        viewPager.setAdapter(topMenuAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setTabLayoutData();
    }
    int[] colors = {R.mipmap.iv_tuijian1,R.mipmap.iv_chadao,R.mipmap.iv_xiangdao,R.mipmap.iv_wenwan,
            R.mipmap.iv_yuqi,R.mipmap.iv_shuji,R.mipmap.iv_fushi,R.mipmap.iv_yueqi,R.mipmap.iv_wenfang,R.mipmap.iv_zihua,R.mipmap.iv_gongyi,R.mipmap.iv_tuijian2};
    private  void setTabLayoutData(){
        try{
            String sql = " parent_id = ?  ";
            names = DatabaseUtils.getHelper().queryBySql(ShopClass.class, sql, new String[]{"-1"});
            if (names==null){
                names = new ArrayList<>();
                names.add(0,new ShopClass());
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            names = new ArrayList<>();
            names.add(0,new ShopClass());
        }
        viewPager.setCurrentItem(0);
        topMenuAdapter.setList(names);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                BaseFragment fragment = (BaseFragment) topMenuAdapter.getItem(position);
                int index = fragment.getClassId();
                int color = 0;
                if (index==-1){
                    index = 0;
                    color = colors[index];
                }else {
                    if (index>=colors.length){
                        index = index%colors.length;
                    }
                    color = colors[index];
                }
                ll_title.setBackgroundResource(color);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        reflex(tabLayout);
    }


    @Override
    protected void initEvent(@Nullable Bundle savedInstanceState) {
        super.initEvent(savedInstanceState);
        iv_more.setOnClickListener(v -> startTo(ShopClassActivity.class,1011));
        layout_search.setOnClickListener(v -> startTo(SearchActivity.class));
    }

    @Override
    public void onClick(View view) {

    }
    private void initData() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=Activity.RESULT_OK)return;
        if (requestCode==1011){
            setTabLayoutData();
        }
    }

    public void reflex(final TabLayout tabLayout){
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(() -> {
            try {
                //拿到tabLayout的mTabStrip属性
                LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);
                int dp10 = UScreenUtil.dp2px(10);
                for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                    View tabView = mTabStrip.getChildAt(i);

                    //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                    Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                    mTextViewField.setAccessible(true);
                    TextView mTextView = (TextView) mTextViewField.get(tabView);
                    tabView.setPadding(0, 0, 0, 0);
                    //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                    int width = 0;
                    width = mTextView.getWidth();
                    if (width == 0) {
                        mTextView.measure(0, 0);
                        width = mTextView.getMeasuredWidth();
                    }

                    //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                    params.width = width ;
                    params.leftMargin = dp10;
                    params.rightMargin = dp10;
                    tabView.setLayoutParams(params);

                    tabView.invalidate();
                }

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume", "onResume: " );
    }
}
