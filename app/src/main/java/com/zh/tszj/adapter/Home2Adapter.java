package com.zh.tszj.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.baselib.image.UImage;
import com.android.baselib.ui.adapter.ListViewItemChickClichListener;
import com.android.baselib.view.NoScrollGridView;
import com.youth.banner.Banner;
import com.zh.tszj.R;
import com.zh.tszj.activity.home.StoreHomeActivity;
import com.zh.tszj.activity.home.StoreTypeActivity;
import com.zh.tszj.adapter.holder.ShopHolder;
import com.zh.tszj.banner.GlideImageLoader;
import com.zh.tszj.bean.AdvertBean;
import com.zh.tszj.bean.HomeStoreBean;
import com.zh.tszj.bean.ShopClass;
import com.zh.tszj.bean.ShopDetails;
import com.zh.tszj.db.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

import am.widget.wraplayout.WrapLayout;

public class Home2Adapter extends RecyclerView.Adapter {
    Activity activity;
    int TYPE_HEADVIEW = 100; //头布局
    int TYPE_CLASS = 101; //二級分類
    int TYPE_SHOP = 103;//商品布局

    List<ShopDetails> dataList;

    public Home2Adapter(Activity activity) {
        this.dataList = new ArrayList<>();
        dataList.add(null);
        dataList.add(null);
        this.activity = activity;
    }
    public void addAll(List<ShopDetails> list) {
        dataList.addAll(list);
        if (dataList.size()>2){
            isShow = true;
        }else {
            isShow = false;
        }
        notifyDataSetChanged();
    }
    public void clear(){
        if (dataList!=null)
            dataList.clear();
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADVIEW;
        }else if (position == 1){
            return TYPE_CLASS;
        }else {
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_home_2_head, parent, false);
            holder = new HeadViewHolder2(view, activity);
        } else if (viewType == TYPE_CLASS) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home2_shop_type, parent, false);
            holder = new ShopTypeHolder(view);
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
                    return getItemViewType(position) == TYPE_HEADVIEW
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadViewHolder2) {
            HeadViewHolder2 holder2 = (HeadViewHolder2) holder;
            holder2.setType(getShopClass());
        } else if (holder instanceof ShopTypeHolder) {
            ShopTypeHolder shopHolder = (ShopTypeHolder) holder;
            shopHolder.setData(shopClassClickListener,position);
        }else if (holder instanceof ShopHolder) {
            ShopHolder shopHolder = (ShopHolder) holder;
            shopHolder.setData(dataList.get(position),position);
            shopHolder.setItemChickClichListener(shopDetailsClickListener);
        }
    }

    private ShopClass shopClass;
    boolean isShow;
    public void setShow(boolean show) {
        isShow = show;
    }
    public ShopClass getShopClass() {
        return shopClass;
    }
    public void setShopClass(ShopClass shopClass) {
        this.shopClass = shopClass;
        notifyDataSetChanged();
    }
    public class ShopTypeHolder extends RecyclerView.ViewHolder{
        WrapLayout wrapLayout;

        public ShopTypeHolder(View itemView) {
            super(itemView);
            wrapLayout = itemView.findViewById(R.id.wrapLayout);
        }

        private void setData(ListViewItemChickClichListener<ShopClass> listener, int position) {
            if (!isShow){
                itemView.setVisibility(View.INVISIBLE);
                return;
            }else {
                itemView.setVisibility(View.VISIBLE);
            }
            if (shopClass!=null){
               List<ShopClass> list =  DatabaseUtils.getHelper().queryByKey(ShopClass.class,"parent_id",shopClass.id+"");
                if (list!=null){
                    wrapLayout.removeAllViews();
                    for (int i = 0; i <list.size() ; i++) {
                        final  int p = i;
                        TextView textView = (TextView) LayoutInflater.from(activity).inflate(R.layout.item_text1,null);
                        textView.setText(list.get(p).cat_name);
                        textView.setOnClickListener(v ->{
                            if (listener!=null)
                            listener.onClickItemListener(textView,list.get(p),p);
                        });
                        wrapLayout.addView(textView);
                    }
                }
            }
        }

    }

    public class HeadViewHolder2 extends RecyclerView.ViewHolder {
        Activity activity;
        Banner banner;
        LinearLayout layout_more;
        NoScrollGridView gridView;
        ShopClass shopClass;
        public HeadViewHolder2(View itemView,Activity activity) {
            super(itemView);
            LayoutInflater.from(activity).inflate(R.layout.view_home_2_head, null, false);
            this.activity = activity;
            banner = itemView.findViewById(R.id.banner);
            layout_more = itemView.findViewById(R.id.layout_more);
            gridView = itemView.findViewById(R.id.gridView);
            DatabaseUtils.initHelper(activity,DatabaseUtils.DATA);
        }
        public  void setType(ShopClass shopClass){
            this.shopClass = shopClass;
            layout_more.setOnClickListener(v -> {
                Intent intent = new Intent(activity,StoreTypeActivity.class);
                intent.putExtra("type",shopClass.id+"");
                intent.setAction("店铺榜");
                activity.startActivity(intent);
            });
            setAdvertData();
            setData();
        }
        //banner 广告
        private void setAdvertData() {
            if (shopClass==null) {
                banner.setVisibility(View.GONE);
                return;
            }
            banner.setVisibility(View.VISIBLE);
            List<AdvertBean> advertBeanList = DatabaseUtils.getHelper().queryByKey(AdvertBean.class,"type",shopClass.id+"");
            if (advertBeanList!=null&&advertBeanList.size()>0){
                //设置图片加载器
                banner.setImageLoader(new GlideImageLoader());
                //设置轮播时间
                banner.setDelayTime(3000);
                //设置图片集合
                banner.setImages(advertBeanList);
                //banner设置方法全部调用完毕时最后调用
                banner.start();
            }else {
                banner.setVisibility(View.GONE);
            }
        }
        private void setData() {
            if (shopClass==null) {
                gridView.setVisibility(View.GONE);
                layout_more.setVisibility(View.GONE);
                return;
            }
            layout_more.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.VISIBLE);
            List<HomeStoreBean> list =   DatabaseUtils.getHelper().queryByKey(HomeStoreBean.class,"type",shopClass.id+"");
            if (list!=null&&list.size()>0){
                layout_more.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.VISIBLE);
                GridViewAdapter adapter = new GridViewAdapter(activity);
                gridView.setAdapter(adapter);
                adapter.addAll(list);
                adapter.notifyDataSetChanged();
            }else {
                layout_more.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                return;
            }
        }

        private class GridViewAdapter extends ArrayAdapter<HomeStoreBean> {

            public GridViewAdapter(@NonNull Context context) {
                super(context, 0);
            }

            @Override
            public int getCount() {
                return super.getCount()>4 ?4:super.getCount();
            }
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = LayoutInflater.from(activity).inflate(R.layout.item_home_store_qijian,null);
                ImageView iv_img = view.findViewById(R.id.iv_img);
                TextView tv_name = view.findViewById(R.id.tv_name);
                HomeStoreBean bean = getItem(position);
                tv_name.setText(bean.shop_name);
                UImage.getInstance().load(activity,bean.shop_img,iv_img);
                view.setOnClickListener(v->{
                    Intent intent = new Intent(activity,StoreHomeActivity.class);
                    intent.setAction(bean.id);
                    activity.startActivity(intent);
                });
                return view;
            }
        }
    }
    ListViewItemChickClichListener<ShopClass> shopClassClickListener;
    public void setShopClassClickListener(ListViewItemChickClichListener<ShopClass> shopClassClickListener) {
        this.shopClassClickListener = shopClassClickListener;
    }
    ListViewItemChickClichListener<ShopDetails> shopDetailsClickListener;

    public void setShopDetailsClickListener(ListViewItemChickClichListener<ShopDetails> shopDetailsClickListener) {
        this.shopDetailsClickListener = shopDetailsClickListener;
    }
}
