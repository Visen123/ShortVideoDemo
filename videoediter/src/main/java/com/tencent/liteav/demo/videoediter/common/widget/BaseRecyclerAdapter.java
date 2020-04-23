package com.tencent.liteav.demo.videoediter.common.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

/**
 * Created by hans on 2017/11/6.
 * <p>
 * 重写的父类Adapter
 * <p>
 * 1. 添加item的点击事件
 * 2. 添加item的长安点击事件
 */
public abstract class BaseRecyclerAdapter<V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> implements View.OnClickListener, View.OnLongClickListener {
    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;
    private WeakReference<RecyclerView> mRecyclerView;

    @Override
    public void onBindViewHolder(V holder, int position) {
        if (holder != null) {
            if (mOnItemClickListener != null)
                holder.itemView.setOnClickListener(this);
            if (mOnItemLongClickListener != null)
                holder.itemView.setOnLongClickListener(this);
            onBindVH(holder, position);
        }
    }

    abstract public void onBindVH(V holder, int position);

    @Override
    public V onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mRecyclerView == null)
            mRecyclerView = new WeakReference<RecyclerView>((RecyclerView) parent);
        return onCreateVH(parent, viewType);
    }

    public abstract V onCreateVH(ViewGroup parent, int viewType);

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setOnLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }


    @Override
    public void onClick(View v) {
        RecyclerView recyclerView = mRecyclerView.get();
        if (recyclerView != null) {
            int position = recyclerView.getChildAdapterPosition(v);
            mOnItemClickListener.onItemClick(v, position);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        RecyclerView recyclerView = mRecyclerView.get();
        if (recyclerView != null) {
            int position = recyclerView.getChildAdapterPosition(v);
            return mOnItemLongClickListener.onItemLongClick(v, position);
        }
        return true;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }
}