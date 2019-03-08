package com.zh.tszj.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.baselib.util.UCommonUtils;
import com.zh.tszj.R;
import com.zh.tszj.base.BaseActivity;
import com.zh.tszj.bean.BannerItem;
import com.zh.tszj.config.CacheConfig;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity {

    public ViewPager vp_guide_bg;
    public LinearLayout ll_guide_points;
    public Button bt_guide_start;
    public View guide_checkPoint;

    private List<ImageView> images;

    @Override
    protected int onLayoutResID() {
        return R.layout.act_layout_guide;
    }
    public static List<BannerItem> BANNER_ITEM=new ArrayList<BannerItem>(){{
        add(new BannerItem("图片1",R.mipmap.guide1));
        add(new BannerItem("图片2",R.mipmap.guide2));
        add(new BannerItem("图片3",R.mipmap.guide3));
    }};

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        // 准备数据
        images = new ArrayList<>();
        for (int i = 0; i < BANNER_ITEM.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(BANNER_ITEM.get(i).pic);
            images.add(imageView);
            // 创建灰色点
            View view = new View(this);
            view.setBackgroundResource(R.mipmap.gundong_h);

            int dp2px = UCommonUtils.dp2px(getApplicationContext(), 10);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp2px, dp2px);
            if (i != 0) {
                params.leftMargin = dp2px;
            }
            view.setLayoutParams(params);
            // 添加灰点
            ll_guide_points.addView(view);
        }
        if (BANNER_ITEM.size() == 1) {
            bt_guide_start.setVisibility(View.VISIBLE);
            ll_guide_points.setVisibility(View.GONE);
            guide_checkPoint.setVisibility(View.GONE);
        } else {
            bt_guide_start.setVisibility(View.GONE);
            ll_guide_points.setVisibility(View.VISIBLE);
            guide_checkPoint.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initEvent(@Nullable Bundle savedInstanceState) {
        super.initEvent(savedInstanceState);
        bt_guide_start.setOnClickListener(this);
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        // 设置数据适配器
        vp_guide_bg.setAdapter(new MyPagerAdapter());
        // 监听ViewPager滚动事件
        vp_guide_bg.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            // 计算点应该移动的距离
            int redMoveX = (int) ((position + positionOffset) * UCommonUtils.dp2px(getApplicationContext(), 20));// 手指移动的距离/屏幕宽度 * 灰点间距
            // 设置点的位置
            android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) guide_checkPoint.getLayoutParams();
            params.leftMargin = redMoveX;
            guide_checkPoint.setLayoutParams(params);
        }

        @Override
        public void onPageSelected(int position) {
            if (position == images.size() - 1) {
                bt_guide_start.setVisibility(View.VISIBLE);
                ll_guide_points.setVisibility(View.GONE);
                guide_checkPoint.setVisibility(View.GONE);
            } else {
                bt_guide_start.setVisibility(View.GONE);
                ll_guide_points.setVisibility(View.VISIBLE);
                guide_checkPoint.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }
    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = images.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    @Override
    public void onClick(View view) {
        if (bt_guide_start==view){
            startTo(MainActivity.class);
            CacheConfig.isFirstToNo();
            finish();
        }
    }
}
