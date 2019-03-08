package com.zh.tszj.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.baselib.ui.adapter.ListViewItemChickClichListener;
import com.android.baselib.util.UToastUtil;
import com.android.baselib.view.NoScrollGridView;
import com.youth.banner.Banner;
import com.zh.tszj.R;
import com.zh.tszj.activity.home.ShopTypeActivity;
import com.zh.tszj.activity.home.StoreNewActivity;
import com.zh.tszj.activity.home.StoreQJDActivity;
import com.zh.tszj.activity.home.StoreTypeActivity;
import com.zh.tszj.adapter.holder.AdvSingleViewHolder;
import com.zh.tszj.adapter.holder.ShopHolder;
import com.zh.tszj.adapter.holder.TitleViewHolder;
import com.zh.tszj.banner.GlideImageLoader;
import com.zh.tszj.bean.AdvertBean;
import com.zh.tszj.bean.NoticeBean;
import com.zh.tszj.bean.ShopDetails;
import com.zh.tszj.db.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

public class Home1Adapter extends RecyclerView.Adapter {
    Activity activity;
    int TYPE_HEADVIEW = 100; //头布局
    int TYPE_ADV_SINGLE = 101; //广告
    int TYPE_TITLE = 102; //广告
    int TYPE_SHOP = 106;//商品布局

    List<ShopDetails> dataList;
    private String title = "精选好物";

    public Home1Adapter(Activity activity) {
        this.dataList = new ArrayList<>();
        dataList.add(null);
        dataList.add(null);
        dataList.add(null);
        this.activity = activity;
    }

    public void addAll(List<ShopDetails> list) {
        if (list == null) return;
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        if (dataList != null)
            dataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADVIEW;
        } else if (position == 1) {
            return TYPE_ADV_SINGLE;
        } else if (position == 2) {
            return TYPE_TITLE;
        } else {
            return TYPE_SHOP;
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        if (viewType == TYPE_HEADVIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_home_1_head, parent, false);
            holder = new HeadViewHolder1(view, activity);
        } else if (viewType == TYPE_ADV_SINGLE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_home_adv_single, parent, false);
            holder = new AdvSingleViewHolder(view, activity);
        } else if (viewType == TYPE_TITLE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_home_title, parent, false);
            holder = new TitleViewHolder(view, activity);
        } else if (viewType == TYPE_SHOP) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_shop, parent, false);
            holder = new ShopHolder(activity, view);
        }
        return holder;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getItemViewType(position) == TYPE_SHOP) {
                        return 1;
                    } else {
                        return gridManager.getSpanCount();
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadViewHolder1) {

        } else if (holder instanceof AdvSingleViewHolder) {

        } else if (holder instanceof TitleViewHolder) {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.setData(getTitle());
        } else if (holder instanceof ShopHolder) {
            ShopHolder shopHolder = (ShopHolder) holder;
            shopHolder.setData(dataList.get(position), position);
            shopHolder.setItemChickClichListener(itemChickClichListener);
        }
    }

    private String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 首页 推荐的 HeadView
     */
    public class HeadViewHolder1 extends RecyclerView.ViewHolder {
        Activity activity;
        Banner banner;
        TextView tv_notice;
        NoScrollGridView gridView;
        List<NoticeBean> beanList;
        int index = 0;

        public HeadViewHolder1(View itemView, Activity activity) {
            super(itemView);
            this.activity = activity;
            DatabaseUtils.initHelper(activity, DatabaseUtils.DATA);
            banner = itemView.findViewById(R.id.banner);
            tv_notice = itemView.findViewById(R.id.tv_notice);
            gridView = itemView.findViewById(R.id.gridView);
            beanList = DatabaseUtils.getHelper().queryAll(NoticeBean.class);
            setAdvertData();
            setNoticeData();
            setShopTypeData();
        }

        //通知
        public void setNoticeData() {
            handler.sendEmptyMessageDelayed(1, 300);
        }

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (beanList == null || beanList.size() < 1) {
                    return;
                }
                index++;
                index = index % beanList.size();
                tv_notice.setOnClickListener(v -> {
                    UToastUtil.showToastShort(beanList.get(index).title);
                });
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(tv_notice, "translationY", 0, -300);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(tv_notice, "translationY", 300, 0);
                AnimatorSet set = new AnimatorSet();
                set.setDuration(300);
                set.setInterpolator(new AccelerateInterpolator());
                set.play(animator2).after(animator1);
                set.start();
                set.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        tv_notice.setText(beanList.get(index).title);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                handler.sendEmptyMessageDelayed(1, 3000);
            }
        };

        //活动类型
        public void setShopTypeData() {
            GridViewAdapter adapter = new GridViewAdapter(activity);
            adapter.add("");
            adapter.add("");
            adapter.add("");
            adapter.add("");
            gridView.setAdapter(adapter);
        }

        //banner 广告
        public void setAdvertData() {
            List<AdvertBean> advertBeanList = DatabaseUtils.getHelper().queryByKey(AdvertBean.class, "type", "-1");
            if (advertBeanList == null) {
                advertBeanList = new ArrayList<>();
            }
            //设置图片加载器
            banner.setImageLoader(new GlideImageLoader());
            //设置轮播时间
            banner.setDelayTime(3000);
            //设置图片集合
            banner.setImages(advertBeanList);
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        }

        /**
         * 商品分类适配器
         */
        private class GridViewAdapter extends ArrayAdapter<String> {
            public GridViewAdapter(@NonNull Context context) {
                super(context, 0);
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = LayoutInflater.from(activity).inflate(R.layout.item_home_shop_type, null);
                TextView tv_text1 = view.findViewById(R.id.tv_text1);
                TextView tv_text2 = view.findViewById(R.id.tv_text2);
                ImageView iv_img = view.findViewById(R.id.iv_img);
                switch (position) {
                    case 0:
                        tv_text1.setText("畅销榜");
                        tv_text2.setText("目之所聚 心之所欲");
                        iv_img.setImageResource(R.mipmap.iv_cxb_img);
                        view.setOnClickListener(v -> {
                            Intent intent = new Intent(activity, ShopTypeActivity.class);
                            intent.setAction("畅销榜");
                            activity.startActivity(intent);
                        });
                        break;
                    case 1:
                        tv_text1.setText("分享有奖");
                        tv_text2.setText("分享赚佣金");
                        iv_img.setImageResource(R.mipmap.iv_yxb_img);
                        view.setOnClickListener(v -> {
                            Intent intent = new Intent(activity, ShopTypeActivity.class);
                            intent.setAction("分享有奖");
                            activity.startActivity(intent);
                        });
                        break;
                    case 2:
                        tv_text1.setText("新驻良铺");
                        tv_text2.setText("新驻良铺");
                        iv_img.setImageResource(R.mipmap.iv_xdrz_img);
                        view.setOnClickListener(v -> {
                            Intent intent = new Intent(activity, StoreNewActivity.class);
                            intent.setAction("新驻良铺");
                            activity.startActivity(intent);
                        });
                        break;
                    case 3:
                        tv_text1.setText("旗舰店");
                        tv_text2.setText("监以严 品于坚");
                        iv_img.setImageResource(R.mipmap.iv_qjd_img);
                        view.setOnClickListener(v -> {
                            Intent intent = new Intent(activity, StoreQJDActivity.class);
                            intent.setAction("旗舰店");
                            activity.startActivity(intent);
                        });
                        break;
                }
                return view;
            }
        }
    }

    ListViewItemChickClichListener itemChickClichListener;

    public void setItemChickClichListener(ListViewItemChickClichListener itemChickClichListener) {
        this.itemChickClichListener = itemChickClichListener;
    }
}
