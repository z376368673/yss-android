package com.android.baselib.ui.adapter;

import android.support.annotation.Nullable;
import com.android.baselib.ui.adapter.view.RCLoadMoreView;
import com.android.baselib.ui.adapter.viewholder.RCViewHolder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;

import java.util.List;


/**
 * item的layout单一的adapter
 *
 * @author PF-NAN
 * @date 2018/3/12
 */

public  class RCSingleAdapter<T, V extends RCViewHolder> extends BaseQuickAdapter<T, V> {
    public ListViewItemChickClichListener<T> itemChickClichListener;
    public RCSingleAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
        init();
    }
    public void addAll(List<T> list) {
        if (list == null) return;
        addData(list);
        notifyDataSetChanged();
    }
    private void init() {
        this.setLoadMoreView(getCustomLoadMoreView());
    }

    /**
     * 返回自定义的加载更多控件.
     *
     * @return
     */
    protected LoadMoreView getCustomLoadMoreView() {
        return new RCLoadMoreView();
    }

    public void clearData(){
        if (getData() != null)
        getData().clear();
        notifyDataSetChanged();
    }

    @Override
    protected void convert(V holder, T item) {
        holder.itemView.setOnClickListener(v->{
            if (itemChickClichListener!=null)
                itemChickClichListener.onClickItemListener(holder.itemView,item,holder.getAdapterPosition());
        });
    }

    public void setItemChickClichListener(ListViewItemChickClichListener<T> itemChickClichListener) {
        this.itemChickClichListener = itemChickClichListener;
    }
}
