package com.example.myapplication2.base;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * viewholder父类
 * Created by ypt on 2017/9/6.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    /**
     * view储存器
     */
    private SparseArray<View> mViews;

    /**
     * 构造函数
     *
     * @param itemView 布局
     */
    public BaseViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }


    /**
     * 根据资源id得到view
     *
     * @param resId 控件id
     * @param <T>   view
     * @return view
     */
    public <T extends View> T getView(int resId) {
        View v = mViews.get(resId);
        if (v == null) {
            v = itemView.findViewById(resId);
            mViews.put(resId, v);
        }
        return (T) v;
    }

    /**
     * 直接设置textview的text
     *
     * @param resId 控件id
     * @param text  显示内容
     * @return 本类
     */
    public BaseViewHolder setText(int resId, String text) {
        if (text == null) {
            text = "";
        }
        View v = mViews.get(resId);
        if (v == null) {
            v = itemView.findViewById(resId);
            mViews.put(resId, v);
        }
        ((TextView) v).setText(text);
        return this;
    }

    /**
     * 直接设置ImageView的src
     *
     * @param resId      控件id
     * @param drawableId 图片id
     * @return 本类
     */
    public BaseViewHolder setImage(int resId, int drawableId) {
        View v = mViews.get(resId);
        if (v == null) {
            v = itemView.findViewById(resId);
            mViews.put(resId, v);
        }
        ((ImageView) v).setImageResource(drawableId);
        return this;
    }
}
