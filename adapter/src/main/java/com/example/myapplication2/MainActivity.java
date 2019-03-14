package com.example.myapplication2;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.myapplication2.base.BaseAdapter;
import com.example.myapplication2.base.BaseLinearLayoutManager;
import com.example.myapplication2.base.BaseViewHolder;

import java.util.ArrayList;

/**
 *
 */
public class MainActivity extends AppCompatActivity {

    ArrayList<String> mData;
    SwipeRefreshLayout sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mData = new ArrayList<>();
        final RecyclerView recyclerView = findViewById(R.id.rv_content);
        recyclerView.setLayoutManager(new BaseLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        baseAdapter.setLoadMore(true);
//        baseAdapter.setLoadNoMore("jfdkjfkdjsfljdlsfjlsd");
        baseAdapter.setLoadMoreListener(new BaseAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page1++;
                setmData(page1);
            }

            @Override
            public void onErrorClick() {
                page1++;
                setmData(page1);
            }
        });
        sw = findViewById(R.id.sw);
        sw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                page1 = 1;
                baseAdapter.setRefreshing(true);
                setmData(page1);
            }
        });
        setmData(page1);
        recyclerView.setAdapter(baseAdapter);
    }

    int page1 = 1;
    int pageCount = 15;

    private synchronized void setmData(final int page) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 *要执行的操作
                 */
                if (page == 4) {
                    baseAdapter.showNoMore();
                } else if (page == 2) {
                    baseAdapter.showError();
                } else {
                    if (page == 1) {
                        mData.clear();
                    }

                    int start = (page - 1) * pageCount;
                    for (int i = start; i < start + pageCount; i++) {
                        mData.add("我是第" + i + "个");
                        Log.e("aaaa", "我是第" + i + "个");
                    }
                    if (page == 1) {
                        sw.setRefreshing(false);
                        baseAdapter.showComplete();
                    }
                    baseAdapter.setmData(mData);
//                    baseAdapter.showComplete();
                }

            }
        }, 1500);//3秒后执行Runnable中的run方法


    }

    private BaseAdapter<String> baseAdapter = new BaseAdapter<String>(R.layout.item_order_details_goods, mData) {

        @Override
        public void onBindViewHolder(BaseViewHolder holder, ArrayList<String> data, int position) {
            holder.setText(R.id.tv_order_details_goods_name, mData.get(position));
        }
    };
}
