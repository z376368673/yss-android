package com.zh.tszj.view.handygridview;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.zh.tszj.R;
import com.zh.tszj.view.handygridview.scrollrunner.OnItemMovedListener;

import java.util.ArrayList;
import java.util.List;

public abstract class GridViewAdapter<T> extends BaseAdapter implements OnItemMovedListener {
    private Context context;
    private List<T> mDatas = new ArrayList<>();

    public GridViewAdapter(Context context, List<T> dataList) {
        this.context = context;
        if(dataList!=null)
        this.mDatas.addAll(dataList);
    }

    private GridView mGridView;
    private boolean inEditMode = false;

    public void setData(List<T> dataList) {
        this.mDatas.clear();
        this.mDatas.addAll(dataList);
        notifyDataSetChanged();
    }

    public void setInEditMode(boolean inEditMode) {
        this.inEditMode = inEditMode;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mGridView == null) {
            mGridView = (GridView) parent;
        }
        TextView textView;
        if (convertView == null) {
            textView = new TextView(context);
            convertView = textView;
            textView.setMaxLines(1);
            textView.setHeight(100);
            textView.setWidth(100);
            textView.setTextColor(Color.parseColor("#b1b1b1"));
            textView.setBackgroundResource(R.drawable.radio_button_bg_selector_white_f2);
            textView.setGravity(Gravity.CENTER);
            String text = getText(mDatas.get(position));
            textView.setText(text);
        }
        return convertView;
    }

    public abstract String getText(T data);

    @Override
    public void onItemMoved(int from, int to) {
        T s = mDatas.remove(from);
        mDatas.add(to, s);
    }

    @Override
    public boolean isFixed(int position) {
        //When postion==0,the item can not be dragged.
        if (position == 0) {
            return true;
        }
        return false;
    }

//    @Override
//    public void onDelete(View deleteView) {
//        int index = mGridView.indexOfChild(deleteView);
//        if (index <= 0) return;
//        int position = index + mGridView.getFirstVisiblePosition();
//        mDatas.remove(position);
//        notifyDataSetChanged();
//    }
}