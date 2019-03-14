package com.example.myapplication2.base;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.myapplication2.R;

import java.util.ArrayList;

/**
 * 适配器父类
 * Created by ypt on 2017/9/6.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    /*可以加载更多的viewType*/
    private final int LOAD_TYPE = -1;
    /*不可以加载更多的viewType*/
    private final int NORMAL_TYPE = -2;

    private String LOAD_MORE = "加载更多";
    private String LOADIND = "正在加载...";
    private String LOAD_NO_MORE = "没有更多内容了";
    private String LOAD_ERROR = "加载失败，点击重试";
    public final static int LOADE_MORE_TYPE = 1;
    public final static int LOADIND_TYPE = 2;
    public final static int LOAD_NO_MORE_TYPE = 3;
    public final static int LOAD_ERROR_TYPE = 4;
    /*下拉刷新的状态1，加载更多2，显示正在加载3，显示没有更多内容了*/
    private int loadType = 1;

    /**
     * layout资源id
     */
    private int layoutResId;
    /**
     * 是否开启加载更多
     */
    private boolean isLoadMore = false;
    /**
     * 加载更多回调参数
     */
    private OnLoadMoreListener onLoadMoreListener;
    /**
     * 设置底部的回调
     */
    private OnSetBottomListener onSetBottomListener;
    /**
     * 正在刷新
     */
    private boolean isRefreshing = false;
    /**
     * 数据
     */
    private ArrayList<T> mData;
    /**
     * 父布局
     */
    private ViewGroup parent;

    /**
     * 底部布局
     */
    private View footer;

    /**
     * 设置布局显示文本
     *
     * @param loadType
     */
    public void setLoadType(int loadType) {
        this.loadType = loadType;
    }

    /**
     * 构造器
     *
     * @param resId layout资源id
     * @param data  数据
     */
    public BaseAdapter(int resId, ArrayList<T> data) {
        this.layoutResId = resId;
        this.mData = data;
    }

    public void setmData(ArrayList<T> data) {
        if (data != null) {
            this.mData = new ArrayList<>();
            this.mData.addAll(data);
            Handler handler1 = new Handler();
            final Runnable r = new Runnable() {
                public void run() {
                    notifyDataSetChanged();
                }
            };
            handler1.post(r);
        }
    }

    /**
     * 设置支持加载更多
     *
     * @param loadMore
     */
    public void setLoadMore(boolean loadMore) {
        this.isLoadMore = loadMore;
    }

    /**
     * 设置底部布局
     * 自定义布局的view_id必须和默认的id名称一致
     *
     * @param layoutResId
     */
    public void setFooter(int layoutResId) {
        footer = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
    }


    /**
     * 加载完成时调用
     */
    public void showComplete() {
        isRefreshing = false;
        setLoadType(BaseAdapter.LOADE_MORE_TYPE);
        Handler handler1 = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                notifyDataSetChanged();
            }
        };
        handler1.post(r);
    }

    /**
     * 没有更多
     */
    public void showNoMore() {
        isRefreshing = false;
        setLoadType(BaseAdapter.LOAD_NO_MORE_TYPE);
        Handler handler1 = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                notifyDataSetChanged();
            }
        };
        handler1.post(r);
    }

    /**
     * 显示错误。点击重试
     */
    public void showError() {
        isRefreshing = false;
        setLoadType(BaseAdapter.LOAD_ERROR_TYPE);
        Handler handler1 = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                notifyDataSetChanged();
            }
        };
        handler1.post(r);
    }

    /**
     * 设置加载更多文本
     *
     * @param LOAD_MORE
     */
    public void setLoadMore(String LOAD_MORE) {
        this.LOAD_MORE = LOAD_MORE;
    }

    /**
     * 设置没有更多内容了文本
     *
     * @param LOAD_NO_MORE
     */
    public void setLoadNoMore(String LOAD_NO_MORE) {
        this.LOAD_NO_MORE = LOAD_NO_MORE;
    }

    /**
     * 设置加载中文本
     *
     * @param LOADIND
     */
    public void setLoading(String LOADIND) {
        this.LOADIND = LOADIND;
    }


    /**
     * 设置加载失败的文本
     *
     * @param LOAD_ERROR
     */
    public void setLoadError(String LOAD_ERROR) {
        this.LOAD_ERROR = LOAD_ERROR;
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.onLoadMoreListener = loadMoreListener;
    }

    public void setOnSetBottomListener(OnSetBottomListener onSetBottomListener) {
        this.onSetBottomListener = onSetBottomListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadMore) {
            if (position == this.mData.size()) { //如果支持加载更多，并且是最后一个
                return LOAD_TYPE;
            } else {
                return getViewType(position);
            }
        } else {
            return getViewType(position);
        }
    }

    /**
     * 上拉刷新的时候设置为true，不然会出错.刷新完成设置为false
     *
     * @param refreshing
     */
    public void setRefreshing(boolean refreshing) {
        isRefreshing = refreshing;
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = null;
        switch (viewType) {
            case NORMAL_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
                break;
            case LOAD_TYPE:
                view = onCreateView(parent, viewType);
                break;
        }
        return new BaseViewHolder(view);
    }

    /**
     * 重写此方法和onCreateView可实现viewtype功能
     *
     * @param position
     * @return
     */
    public int getViewType(int position) {
        return NORMAL_TYPE;
    }

    /**
     * 重写此方法和getViewType可实现viewtype功能
     *
     * @param parent
     * @param viewType
     * @return
     */
    public View onCreateView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.base_adapter_normal_footer, parent, false);
    }


    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        //如果加载更多开关开启&&没有在刷新中&&绘制下标和是最后一个item。则刷新
        if (!isRefreshing && getItemViewType(position) == LOAD_TYPE) {
            isRefreshing = true;
            if (onLoadMoreListener == null) {
                throw new NullPointerException("you must set OnLoadMoreListener");
            } else {
                if (loadType == LOADE_MORE_TYPE) {
                    setLoadType(LOADIND_TYPE);
                }
            }
        }
        switch (getItemViewType(position)) {
            case LOAD_TYPE:
                if (this.onSetBottomListener == null) {
                    switch (loadType) {
                        case LOADE_MORE_TYPE:
                            holder.setText(R.id.tv_load_prompt, LOAD_MORE);
                            holder.getView(R.id.progress).setVisibility(View.GONE);
                            holder.getView(R.id.tv_load_prompt).setOnClickListener(null);

                            break;
                        case LOADIND_TYPE:
                            holder.setText(R.id.tv_load_prompt, LOADIND);
                            holder.getView(R.id.progress).setVisibility(View.VISIBLE);
                            setLoadType(LOADIND_TYPE); //基本没有显示
                            onLoadMoreListener.onLoadMore();
                            holder.getView(R.id.tv_load_prompt).setOnClickListener(null);
                            break;
                        case LOAD_NO_MORE_TYPE:
                            holder.setText(R.id.tv_load_prompt, LOAD_NO_MORE);
                            holder.getView(R.id.progress).setVisibility(View.GONE);
                            holder.getView(R.id.tv_load_prompt).setOnClickListener(null);
                            break;
                        case LOAD_ERROR_TYPE:
                            holder.setText(R.id.tv_load_prompt, LOAD_ERROR);
                            holder.getView(R.id.progress).setVisibility(View.GONE);
                            holder.getView(R.id.tv_load_prompt).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    holder.setText(R.id.tv_load_prompt, LOADIND);
                                    holder.getView(R.id.progress).setVisibility(View.VISIBLE);
                                    setLoadType(LOADIND_TYPE); //基本没有显示
                                    holder.getView(R.id.tv_load_prompt).setOnClickListener(null);
                                    onLoadMoreListener.onErrorClick();
                                }
                            });
                            break;
                    }
                } else {
                    switch (loadType) {
                        case LOADE_MORE_TYPE:
                            this.onSetBottomListener.onLoadMore(holder, position);
                            break;
                        case LOADIND_TYPE:
                            this.onSetBottomListener.onLoading(holder, position);
                            break;
                        case LOAD_NO_MORE_TYPE:
                            this.onSetBottomListener.onNoMore(holder, position);
                            break;
                        case LOAD_ERROR_TYPE:
                            this.onSetBottomListener.onLoadError(holder, position);
                            break;
                    }
                }
                break;
            case NORMAL_TYPE:
                onBindViewHolder(holder, mData, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (isLoadMore) {
            return mData == null ? 0 : mData.size() + 1;
        } else {
            return mData == null ? 0 : mData.size();
        }
    }

    public abstract void onBindViewHolder(BaseViewHolder holder, ArrayList<T> data, int position);

    /**
     * 用户上啦加载和失败点击重新加载
     */
    public interface OnLoadMoreListener {
        void onLoadMore();

        void onErrorClick();
    }

    /**
     * 用户可以定制自定义底部后如果，底部布局变化非常大。可以实现自定义回调
     */
    public interface OnSetBottomListener {
        void onLoadMore(final BaseViewHolder holder, int position);

        void onLoading(final BaseViewHolder holder, int position);

        void onNoMore(final BaseViewHolder holder, int position);

        void onLoadError(final BaseViewHolder holder, int position);
    }
}
