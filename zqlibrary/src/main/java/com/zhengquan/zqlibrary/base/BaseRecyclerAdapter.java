package com.zhengquan.zqlibrary.base;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 创 建 人：zheng Quan
 * 创建日期：2019/7/3
 * 修改时间：
 * 修改备注：
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<T> mData = new ArrayList<>();
    private Context context;
    private onRecyclerItemClickLitener onRecyclerItemClickLitener;

    public BaseRecyclerAdapter(Context context) {
        this.context = context;
    }

    public BaseRecyclerAdapter(Context context, List<T> data) {
        this(context);
        setData(data);
    }

    /**
     * 清楚数据
     */
    public void clearData() {
        this.mData.clear();
    }

    /**
     * 设置数据
     *
     * @param mData 数据集合
     */
    public void setData(List<T> mData) {
        this.mData = mData;
    }

    /**
     * 添加单个数据
     *
     * @param position 位置
     * @param data     数据对象
     */
    public void addData(int position, T data) {
        mData.add(position, data);
        notifyItemInserted(position);
    }

    /**
     * 添加集合数据
     *
     * @param data 添加数据的集合
     */
    public void appendData(List<T> data) {
        if (null == data) return;
        int size = mData.size();
        this.mData.addAll(data);
        notifyItemRangeInserted(size, data.size());
    }

    /**
     * 移除某一项
     *
     * @param position 位置
     */
    public void removeItem(int position) {
        if (null != mData && position < mData.size()) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * 将其中的item从某个位置移动到某个位置
     *
     * @param form 起始位置
     * @param to   移动到的位置
     */
    public void movedItem(int form, int to) {
        if (null != mData && !mData.isEmpty()) {
            if (form < 0 || form >= getItemCount() || to < 0 || to >= getItemCount()) {
                return;
            }
            T item = mData.get(form);
            mData.remove(form);
            mData.add(to - 1, item);
            notifyItemMoved(form, to);
        }
    }

    public Context getContext() {
        return context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onMyCreateViewHolder(parent, viewType);
    }

    public abstract RecyclerView.ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (onRecyclerItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRecyclerItemClickLitener.onRecyclerItemClick(holder, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onRecyclerItemClickLitener.onRecyclerItemLongClick(holder, position);
                    return false;
                }
            });
        }
        onMyBindViewHolder(holder, position);
    }

    public abstract void onMyBindViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface onRecyclerItemClickLitener {
        void onRecyclerItemClick(RecyclerView.ViewHolder view, int position);

        void onRecyclerItemLongClick(RecyclerView.ViewHolder view, int position);
    }

    public void setOnRecyclerItemClickLitener(onRecyclerItemClickLitener onRecyclerItemClickLitener) {
        this.onRecyclerItemClickLitener = onRecyclerItemClickLitener;
    }

    /**
     * 为RecyclerView添加上拉加载更多的实现接口
     * firstVisibleItem=页面显示的第一个Item的Position
     * visibleItemCount=页面显示的Item的数量
     * totalItemCount=总共的Item的数量
     * previousTotal=与totalItemCount做比较，用于判断是否可以执行加载
     * loading=是否处于加载中
     * currentPage=页数
     * firstVisibleItem+visibleItemCount=totalItemCount 即拉倒了最底部。
     * 当页面刷新时，必须将previousTotal变为0.否则无法执行上拉加载
     */
    public abstract static class LoadMoreRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
        private int previousTotal = 0;
        private boolean loading = true;
        int firstVisibleItem, visibleItemCount, totalItemCount;
        private int currentPage = 1;

        private LinearLayoutManager mLinearLayoutManager;

        public LoadMoreRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
            this.mLinearLayoutManager = linearLayoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = mLinearLayoutManager.getItemCount();
            firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= firstVisibleItem) {
                currentPage++;
                onLoadMore(currentPage);
                loading = true;
            }
        }

        public abstract void onLoadMore(int currentPage);

        public void clearPreviousTotal() {
            previousTotal = 0;
        }
    }
}