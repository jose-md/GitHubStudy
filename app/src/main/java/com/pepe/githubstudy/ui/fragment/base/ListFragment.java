

package com.pepe.githubstudy.ui.fragment.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pepe.githubstudy.R;
import com.pepe.githubstudy.mvp.contract.base.IBaseContract;
import com.pepe.githubstudy.ui.adapter.base.BaseAdapter;
import com.pepe.githubstudy.ui.adapter.base.BaseViewHolder;
import com.pepe.githubstudy.ui.adapter.base.CatchableLinearLayoutManager;
import com.pepe.githubstudy.utils.NetHelper;
import com.pepe.githubstudy.utils.PrefUtils;
import com.pepe.githubstudy.utils.ViewUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created on 2017/7/20.
 * @author ThirtyDegreesRay
 */

public abstract class ListFragment<P extends IBaseContract.Presenter, A extends BaseAdapter>
        extends BaseFragment<P> implements IBaseContract.View,
        BaseViewHolder.OnItemClickListener,
        BaseViewHolder.OnItemLongClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.refresh_layout)
    protected SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    protected RecyclerView recyclerView;
    protected A mAdapter;
    private RecyclerView.AdapterDataObserver observer;

    @BindView(R.id.lay_tip)
    LinearLayout layTip;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.error_image)
    AppCompatImageView errorImage;

    private int curPage = 1;

    private boolean refreshEnable = true;
    private boolean loadMoreEnable = false;
    private boolean canLoadMore = false;
    private boolean autoJudgeCanLoadMoreEnable = true;
    private boolean isLoading = false;
    private final int DEFAULT_PAGE_SIZE = 30;

    protected abstract void setAdapter();

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(ViewUtils.getRefreshLayoutColors(getContext()));
        setAdapter();
        recyclerView.setLayoutManager(new CatchableLinearLayoutManager(getActivity()));
        mAdapter.setOnItemLongClickListener(this);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);

        layTip.setVisibility(View.GONE);

        //mAdapter 数据观察者，当数据为空时，显示空提示
        observer = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                int itemCount = mAdapter.getItemCount();
                if (itemCount == 0) {
                    refreshLayout.setVisibility(View.GONE);
                    layTip.setVisibility(View.VISIBLE);
                    tvTip.setText(getEmptyTip());
                    errorImage.setVisibility(View.GONE);
                } else {
                    refreshLayout.setVisibility(View.VISIBLE);
                    layTip.setVisibility(View.GONE);
                    itemCount -= getHeaderSize();
                    if (loadMoreEnable && autoJudgeCanLoadMoreEnable) {
                        canLoadMore = itemCount % getPagerSize() == 0;
//                        curPage = itemCount % getPagerSize() == 0 ?
//                                itemCount / getPagerSize() : (itemCount / getPagerSize()) + 1;
                    }
                }
            }
        };
        mAdapter.registerAdapterDataObserver(observer);
        recyclerView.setOnScrollListener(new ScrollListener());
        refreshLayout.setRefreshing(true);
        initScrollListener();
    }

    private class ScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (!loadMoreEnable || !canLoadMore || isLoading ||
                    !NetHelper.INSTANCE.getNetEnabled()) {
                return;
            }
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            //only LinearLayoutManager can find last visible
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                int lastPosition = linearManager.findLastVisibleItemPosition();
                if (lastPosition == mAdapter.getItemCount() - 1) {
                    onLoadMore(++curPage);
                }
            }
        }
    }

    public int getVisibleItemCount() {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        //only LinearLayoutManager can find last visible
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            int firstPosition = linearManager.findFirstVisibleItemPosition();
            int lastPosition = linearManager.findLastVisibleItemPosition();
            return lastPosition - firstPosition + 1;
        } else {
            throw new UnsupportedOperationException("only for Linear RecyclerView ");
        }
    }

    public int getItemCount() {
        return mAdapter.getItemCount();
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {

    }

    @Override
    public boolean onItemLongClick(int position, @NonNull View view) {
        return false;
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        curPage = 1;
        onReLoadData();
    }

    @OnClick(R.id.retry_bn)
    public void onRetryClick(@NonNull View view) {
        refreshLayout.setVisibility(View.VISIBLE);
        layTip.setVisibility(View.GONE);
        refreshLayout.setRefreshing(true);
        curPage = 1;
        onReLoadData();
    }

    protected void setErrorTip(String errorTip) {
        refreshLayout.setVisibility(View.GONE);
        errorImage.setVisibility(View.VISIBLE);
        layTip.setVisibility(View.VISIBLE);
        tvTip.setText(errorTip);
    }

    protected void addVerticalDivider() {
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    /**
     * load more switch
     * @param loadMoreEnable flag
     */
    public void setLoadMoreEnable(boolean loadMoreEnable) {
        this.loadMoreEnable = loadMoreEnable;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    public void setRefreshEnable(boolean refreshEnable) {
        this.refreshEnable = refreshEnable;
        refreshLayout.setEnabled(refreshEnable);
    }

    public void setAutoJudgeCanLoadMoreEnable(boolean autoJudgeLoadMoreEnable) {
        this.autoJudgeCanLoadMoreEnable = autoJudgeLoadMoreEnable;
        canLoadMore = !autoJudgeLoadMoreEnable;
    }

    public int getCurPage() {
        return curPage;
    }

    @Override
    public void showLoading() {
        isLoading = true;
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        isLoading = false;
        refreshLayout.setRefreshing(false);
    }

    protected abstract void onReLoadData();

    protected abstract String getEmptyTip();

    protected int getPagerSize() {
        return DEFAULT_PAGE_SIZE;
    }

    protected int getHeaderSize() {
        return 0;
    }

    protected void onLoadMore(int page) {
        if (page == 3 && PrefUtils.isDoubleClickTitleTipAble()) {
            showOperationTip(R.string.double_click_toolbar_tip);
            PrefUtils.set(PrefUtils.DOUBLE_CLICK_TITLE_TIP_ABLE, false);
        }
    }

    public void showLoadError(String error) {
        setErrorTip(error);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null && observer != null) {
            mAdapter.unregisterAdapterDataObserver(observer);
        }
    }

    @Override
    public void scrollToTop() {
        super.scrollToTop();
        if (recyclerView != null) {
            recyclerView.scrollToPosition(0);
        }
    }

    protected void postNotifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    private ListScrollListener mListScrollListener;

    public void setListScrollListener(ListScrollListener listScrollListener) {
        mListScrollListener = listScrollListener;
        initScrollListener();
    }

    private void initScrollListener() {
        if (recyclerView == null || mListScrollListener == null) {
            return;
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    mListScrollListener.onScrollUp();
                } else {
                    mListScrollListener.onScrollDown();
                }
            }
        });
    }

    public interface ListScrollListener {
        void onScrollUp();

        void onScrollDown();
    }

}
