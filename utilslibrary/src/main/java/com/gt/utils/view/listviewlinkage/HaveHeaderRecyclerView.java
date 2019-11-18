package com.gt.utils.view.listviewlinkage;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class HaveHeaderRecyclerView extends RecyclerView {
    //手动滑动监听
    private OnScrollListener mOnManualScrollListener;

    private WrapAdapter mAdapter;
    //标题
    private View mCurrentHeader;
    //显示第几个标题
    private int mCurrentHeaderViewIndex = 0;
    //标题距顶部的距离
    private float mHeaderOffset;
    //是否固定第一个标题
    private boolean mShouldPin = true;
    //当前部分
    private int mCurrentSection = 0;
    //宽度
    private int mWidthMode;
    //高度
    private int mHeightMode;
    private boolean manualScroll = false;

    public HaveHeaderRecyclerView(Context context) {
        super(context);
        addListener();
    }

    public HaveHeaderRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addListener();
    }

    public HaveHeaderRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addListener();
    }

    public void addListener() {
        super.addOnScrollListener(scrollListener);
    }

    //重写绑定适配器
    public void setAdapter(CustomizeRVBaseAdapter adapter) {
        setAdapter(adapter, null, null);
    }

    //重写绑定适配器
    public void setAdapter(CustomizeRVBaseAdapter adapter, View headerView, View footView) {
        mCurrentHeader = null;
        mAdapter = new WrapAdapter(headerView, footView, adapter);
        super.setAdapter(mAdapter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        manualScroll = true;
        return super.onTouchEvent(ev);
    }

    //当前屏幕中第一个item的index
    public int findFirstCompletelyVisibleItemPosition() {
        int firstVisibleItem = 0;
        LayoutManager layoutManager = this.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager || layoutManager instanceof GridLayoutManager || layoutManager instanceof StaggeredGridLayoutManager) {
            if (layoutManager instanceof LinearLayoutManager) {
                firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
            }
            if (layoutManager instanceof GridLayoutManager) {
                firstVisibleItem = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
            }
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] mItems = null;
                mItems = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(mItems);
                if (mItems != null && mItems.length > 0)
                    firstVisibleItem = mItems[0];
            }
        }
        return firstVisibleItem;
    }

    //当前屏幕中item的个数
    public int findLastCompletelyVisibleItemPosition() {
        int lastVisibleItem = 0;
        LayoutManager layoutManager = this.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager || layoutManager instanceof GridLayoutManager || layoutManager instanceof StaggeredGridLayoutManager) {
            if (layoutManager instanceof LinearLayoutManager) {
                lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
            }
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItem = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] items = null;
                items = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(items);
                if (items != null && items.length > 0)
                    lastVisibleItem = items[items.length - 1];
            }
        }
        return lastVisibleItem;
    }

    private OnScrollListener scrollListener = new OnScrollListener() {
        //滚动
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int firstVisibleItem = findFirstCompletelyVisibleItemPosition();
            if (mAdapter == null || mAdapter.getItemCount() - mAdapter.getHeaderViewCount() - mAdapter.getFootViewCount() == 0 || !mShouldPin
                    || mAdapter.getItemViewType(firstVisibleItem) == CustomizeRVBaseAdapter.TYPE_HEADER
                    || mAdapter.getItemViewType(firstVisibleItem) == CustomizeRVBaseAdapter.TYPE_FOOTER) {
                //当适配器为空或适配器中无数据或mShouldPin为false或者可见视同中第一个索引小于0则return
                mCurrentHeader = null;
            } else {
                CustomizeRVBaseAdapter adapter = mAdapter.getContentAdapter();
                //根据可见视图的第一个索引去获取section
                int section = adapter.getSectionForPosition(findFirstCompletelyVisibleItemPosition() - mAdapter.getHeaderViewCount());
                //获取标题
                mCurrentHeader = getSectionHeaderView(adapter, section, mCurrentHeader);
                //更换标题
                ensureHaveHeaderLayout(mCurrentHeader);
                //改成当前标题所对应的值
                mCurrentHeaderViewIndex = section;
                //设置标题距顶部距离
                mHeaderOffset = 0.0f;
                // 第一个Item的位置
                /*
                 *与findFirstCompletelyVisibleItemPosition不同的是：
                 * findFirstCompletelyVisibleItemPosition获取的是当前屏幕中可见的item的index
                 * getChildLayoutPosition(getChildAt(0))获取的是列表中第一个item的index
                 * 第一个item不一定是可见的
                 * 当手动滑动列表的时候，这两个是相等的，但是当通过调用代码去滑动，这两个的值就不一样了
                 */
                int firstItem = getChildLayoutPosition(getChildAt(0));
                // 最后一个Item的位置
                int lastVisibleItem = getChildLayoutPosition(getChildAt(getChildCount() - 1));
                for (int i = firstItem; i < lastVisibleItem; i++) {
                    if (adapter.getItemViewType(i - mAdapter.getHeaderViewCount()) == CustomizeRVBaseAdapter.HEADER_VIEW_TYPE) {
                        //得到真实的子Item的值
                        View childView = getChildAt(i - firstItem);
                        //得到子Item距顶部的距离
                        float childViewTop = childView.getTop();
                        //得到子Item的高度
                        float childViewHeight = childView.getMeasuredHeight();
                        if (childViewHeight >= childViewTop && childViewTop > 0) {
                            //当子Item的高度>子Item距顶部的距离时，则标题应该逐步消失
                            mHeaderOffset = childViewTop - childViewHeight;
                        }
                    }
                }

                //刷新
                invalidate();
            }
            if (mOnManualScrollListener != null && manualScroll) {
                mOnManualScrollListener.onScrolled(recyclerView, dx, dy);
            }
        }

        //滑动状态改变
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (mOnManualScrollListener != null && manualScroll) {
                mOnManualScrollListener.onScrollStateChanged(recyclerView, newState);
            }
            switch (newState) {
                // 当不滚动时
                case RecyclerView.SCROLL_STATE_IDLE:
                    manualScroll = false;
                    break;
            }
        }
    };

    //事件分发子组件绘制
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawCurrentHeader(canvas);
    }

    protected void drawCurrentHeader(Canvas canvas) {
        if (mAdapter == null || !mShouldPin || mCurrentHeader == null) {
            //adapter为空，mShouldPin为false，mCurrentHeader为空，则不绘制
            return;
        }
        //保存Canvas状态
        int saveCount = canvas.save();
        //平移
        canvas.translate(0, mCurrentHeaderViewIndex == 0 && -mHeaderOffset == mCurrentHeader.getMeasuredHeight() ? 0 : mHeaderOffset);
        //设置显示范围,左，上，右，下
        canvas.clipRect(0, 0, getWidth(), mCurrentHeader.getMeasuredHeight());
        mCurrentHeader.draw(canvas);
        //恢复Canvas状态
        canvas.restoreToCount(saveCount);
    }

    //测量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //宽
        mWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        //高
        mHeightMode = MeasureSpec.getMode(heightMeasureSpec);
    }

    public void setOnManualScrollListener(OnScrollListener l) {
        mOnManualScrollListener = l;
    }

    public View getSectionHeaderView(CustomizeRVBaseAdapter adapter, int section, View oldView) {
        //是否显示，即，section不等于当前显示的section，且View不为空
        boolean shouldLayout = section != mCurrentSection || oldView == null;
        //获取View
        RecyclerView.ViewHolder viewHolder = adapter.getSectionHeaderView(this, section);
        adapter.onBindSectionHeaderViewHolder(viewHolder, section);
        View view = viewHolder.itemView;
        if (shouldLayout) {
            //显示标头
            ensureHaveHeaderLayout(view);
            //并将section赋值给mCurrentSection
            mCurrentSection = section;
        }
        //返回加载好的View
        return view;
    }

    //显示标题
    private void ensureHaveHeaderLayout(View header) {
        if (header.isLayoutRequested()) {
            //设置宽(返回值是测量值+mode值)
            int widthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), mWidthMode);
            int heightSpec;
            //父布局参数
            ViewGroup.LayoutParams layoutParams = header.getLayoutParams();
            if (layoutParams != null && layoutParams.height > 0) {
                //若有父布局则header高为父布局的
                heightSpec = MeasureSpec.makeMeasureSpec(layoutParams.height, MeasureSpec.EXACTLY);
            } else {
                //否则，header高为自适应大小
                heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            }
            //设置header宽高
            header.measure(widthSpec, heightSpec);
            //设置header相对于父布局的位置，左，上，右，下
            header.layout(0, 0, header.getMeasuredWidth(), header.getMeasuredHeight());
        }
    }

    public void setSelectionTitle(int position) {
        CustomizeRVBaseAdapter adapter = mAdapter.getContentAdapter();

        int section = mAdapter.getHeaderViewCount();
        for (int i = 0; i < position; i++) {
            //查找
            section += adapter.getCountForSection(i) + 1;
        }

        //滑动到指定位置
        LinearSmoothScroller s1 = new LinearSmoothScroller(getContext()) {
            //重写下面的方法是为了保证将指定位置滑动到最顶部
            @Override
            protected int getHorizontalSnapPreference() {
                return SNAP_TO_START;//具体见源码注释
            }

            @Override
            protected int getVerticalSnapPreference() {
                return SNAP_TO_START;//具体见源码注释
            }
        };
        s1.setTargetPosition(section);
        getLayoutManager().startSmoothScroll(s1);
    }

    public void setShouldPin(boolean shouldPin) {
        this.mShouldPin = shouldPin;
    }

    public View getCurrentHeader() {
        return mCurrentHeader;
    }

    //获取当前选中的段落
    public final int getSelectedSectionForPosition() {
        return mAdapter.getContentAdapter().getSectionForPosition(findFirstCompletelyVisibleItemPosition() - mAdapter.getHeaderViewCount());
    }

    private class WrapAdapter extends RecyclerView.Adapter<ViewHolder> {

        private CustomizeRVBaseAdapter adapter;

        private View mHeaderView;
        private int headerViewCount = 0;

        private View mFootView;
        private int footViewCount = 0;

        public WrapAdapter(View headerView, View footView, CustomizeRVBaseAdapter adapter) {
            this.adapter = adapter;
            this.mHeaderView = headerView;
            this.mFootView = footView;
            if (this.mHeaderView != null)
                headerViewCount = 1;
            if (this.mFootView != null)
                footViewCount = 1;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (isHeader(position) || isFooter(position))
                                ? gridManager.getSpanCount() : adapter.getCustomizeRVBaseAdapterSpanSize(gridManager, position - headerViewCount);
                    }
                });
            }
        }

        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                if (isHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition())) {
                    StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                    p.setFullSpan(true);
                } else {
                    adapter.onViewAttachedToWindow(holder);
                }
            }
        }

        public boolean isHeader(int position) {
            return headerViewCount > 0 && position == 0;
        }

        public boolean isFooter(int position) {
            return position < getItemCount() && position >= getItemCount() - footViewCount;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == CustomizeRVBaseAdapter.TYPE_HEADER) {
                return new SimpleViewHolder(mHeaderView);
            } else if (viewType == CustomizeRVBaseAdapter.TYPE_FOOTER) {
                return new SimpleViewHolder(mFootView);
            }
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (isHeader(position) || isFooter(position)) {
                return;
            }
            int adjPosition = position - headerViewCount;
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    adapter.onBindViewHolder(holder, adjPosition);
                }
            }
        }

        @Override
        public int getItemCount() {
            if (adapter != null) {
                return headerViewCount + footViewCount + adapter.getItemCount();
            } else {
                return headerViewCount + footViewCount;
            }
        }

        public int getHeaderViewCount() {
            return headerViewCount;
        }

        public int getFootViewCount() {
            return footViewCount;
        }

        @Override
        public int getItemViewType(int position) {
            if (isHeader(position)) {
                return CustomizeRVBaseAdapter.TYPE_HEADER;
            }
            if (isFooter(position)) {
                return CustomizeRVBaseAdapter.TYPE_FOOTER;
            }
            int adjPosition = position - headerViewCount;
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return adapter.getItemViewType(adjPosition);
                }
            }
            return CustomizeRVBaseAdapter.TYPE_NORMAL;
        }

        @Override
        public long getItemId(int position) {
            if (adapter != null && !(isHeader(position) || isFooter(position))) {
                int adjPosition = position - headerViewCount;
                int adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return adapter.getItemId(adjPosition);
                }
            }
            return -1;
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            if (adapter != null) {
                adapter.unregisterAdapterDataObserver(observer);
            }
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            if (adapter != null) {
                adapter.registerAdapterDataObserver(observer);
            }
        }

        public CustomizeRVBaseAdapter getContentAdapter() {
            return adapter;
        }

        private class SimpleViewHolder extends RecyclerView.ViewHolder {
            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}