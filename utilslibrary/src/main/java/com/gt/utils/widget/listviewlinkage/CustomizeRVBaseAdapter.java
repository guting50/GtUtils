package com.gt.utils.widget.listviewlinkage;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public abstract class CustomizeRVBaseAdapter extends RecyclerView.Adapter {
    public static final int TYPE_HEADER = -3;
    public static final int TYPE_FOOTER = -5;
    public static final int TYPE_NORMAL = 0;
    public static final int HEADER_VIEW_TYPE = 1;
    public static final int ITEM_VIEW_TYPE = 2;

    private SparseArray<Integer> mSectionPositionCache;
    private SparseArray<Integer> mSectionCache;
    private SparseArray<Integer> mSectionCountCache;
    private int mCount;
    private int mSectionCount;

    public CustomizeRVBaseAdapter() {
        super();
        mSectionPositionCache = new SparseArray<Integer>();
        mSectionCache = new SparseArray<Integer>();
        mSectionCountCache = new SparseArray<Integer>();
        mCount = -1;
        mSectionCount = -1;
    }

    public void notifyChanged() {
        mSectionCache.clear();
        mSectionPositionCache.clear();
        mSectionCountCache.clear();
        mCount = -1;
        mSectionCount = -1;
        super.notifyDataSetChanged();
    }

    @Override
    public final int getItemCount() {
        if (mCount >= 0) {
            return mCount;
        }
        int count = 0;
        for (int i = 0; i < internalGetSectionCount(); i++) {
            count += internalGetCountForSection(i);
            count++;
        }
        mCount = count;
        return count;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == HEADER_VIEW_TYPE) {
            return getSectionHeaderView(viewGroup, viewType);
        }
        return getItemView(viewGroup, viewType, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == HEADER_VIEW_TYPE) {
            onBindSectionHeaderViewHolder(viewHolder, getSectionForPosition(position));
        } else {
            onBindItemViewHolder(viewHolder, getSectionForPosition(position), getPositionInSectionForPosition(position));
        }
    }

    @Override
    public final int getItemViewType(int position) {
        if (isSectionHeader(position)) {
            return HEADER_VIEW_TYPE;
        }
        return ITEM_VIEW_TYPE;
    }

    //获取段落位置
    public final int getSectionForPosition(int position) {
        Integer cachedSection = mSectionCache.get(position);
        if (cachedSection != null) {
            return cachedSection;
        }
        int sectionStart = 0;
        for (int i = 0; i < internalGetSectionCount(); i++) {
            int sectionCount = internalGetCountForSection(i);
            int sectionEnd = sectionStart + sectionCount + 1;
            if (position >= sectionStart && position < sectionEnd) {
                mSectionCache.put(position, i);
                return i;
            }
            sectionStart = sectionEnd;
        }
        return 0;
    }

    //获取节点位置
    public int getPositionInSectionForPosition(int position) {
        Integer cachedPosition = mSectionPositionCache.get(position);
        if (cachedPosition != null) {
            return cachedPosition;
        }
        int sectionStart = 0;
        for (int i = 0; i < internalGetSectionCount(); i++) {
            int sectionCount = internalGetCountForSection(i);
            int sectionEnd = sectionStart + sectionCount + 1;
            if (position >= sectionStart && position < sectionEnd) {
                int positionInSection = position - sectionStart - 1;
                mSectionPositionCache.put(position, positionInSection);
                return positionInSection;
            }
            sectionStart = sectionEnd;
        }
        return 0;
    }

    public final boolean isSectionHeader(int position) {
        int sectionStart = 0;
        for (int i = 0; i < internalGetSectionCount(); i++) {
            if (position == sectionStart) {
                return true;
            } else if (position < sectionStart) {
                return false;
            }
            sectionStart += internalGetCountForSection(i) + 1;
        }
        return false;
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
                    return getCustomizeRVBaseAdapterSpanSize(gridManager, position);
                }
            });
        }
    }

    public int getCustomizeRVBaseAdapterSpanSize(GridLayoutManager gridManager, int position) {
        return (getItemViewType(position) == HEADER_VIEW_TYPE) ? gridManager.getSpanCount() : 1;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && (getItemViewType(holder.getLayoutPosition()) == HEADER_VIEW_TYPE)) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    public abstract int getSectionCount();

    public abstract int getCountForSection(int section);

    public abstract RecyclerView.ViewHolder getItemView(@NonNull ViewGroup viewGroup, int sectionViewType, int viewType);

    public abstract RecyclerView.ViewHolder getSectionHeaderView(@NonNull ViewGroup viewGroup, int sectionViewType);

    public abstract void onBindItemViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int section, int position);

    public abstract void onBindSectionHeaderViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int section);

    //获取段落数量
    private int internalGetCountForSection(int section) {
        Integer cachedSectionCount = mSectionCountCache.get(section);
        if (cachedSectionCount != null) {
            return cachedSectionCount;
        }
        int sectionCount = getCountForSection(section);
        mSectionCountCache.put(section, sectionCount);
        return sectionCount;
    }

    //获取内部节点数
    private int internalGetSectionCount() {
        if (mSectionCount >= 0) {
            return mSectionCount;
        }
        mSectionCount = getSectionCount();
        return mSectionCount;
    }
}