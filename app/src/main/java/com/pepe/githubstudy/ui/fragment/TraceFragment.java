package com.pepe.githubstudy.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.pepe.githubstudy.R;
import com.pepe.githubstudy.inject.component.AppComponent;
import com.pepe.githubstudy.inject.component.DaggerFragmentComponent;
import com.pepe.githubstudy.inject.module.FragmentModule;
import com.pepe.githubstudy.mvp.contract.ITraceContract;
import com.pepe.githubstudy.mvp.model.TraceExt;
import com.pepe.githubstudy.mvp.presenter.TracePresenter;
import com.pepe.githubstudy.ui.activity.ProfileActivity;
import com.pepe.githubstudy.ui.activity.RepositoryActivity;
import com.pepe.githubstudy.ui.adapter.TraceAdapter;
import com.pepe.githubstudy.ui.adapter.base.ItemTouchHelperCallback;
import com.pepe.githubstudy.ui.fragment.base.ListFragment;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener;

import java.util.ArrayList;

/**
 * @author 1one
 * @date 2019/8/8.
 */
public class TraceFragment extends ListFragment<TracePresenter, TraceAdapter>
        implements ITraceContract.View, ItemTouchHelperCallback.ItemGestureListener {

    public static TraceFragment create(){
        return new TraceFragment();
    }

    private ItemTouchHelper itemTouchHelper;

    @Override
    public void showTraceList(ArrayList<TraceExt> traceList) {
        mAdapter.setData(traceList);
        postNotifyDataSetChanged();
    }

    @Override
    public void notifyItemAdded(int position) {
        if(mAdapter.getData().size() == 1){
            postNotifyDataSetChanged();
        } else {
            mAdapter.notifyItemInserted(position);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void setAdapter() {
        mAdapter = new TraceAdapter(getActivity(),this);
    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        super.initFragment(savedInstanceState);
        setLoadMoreEnable(true);
        ItemTouchHelperCallback callback = new ItemTouchHelperCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
        recyclerView.addItemDecoration(headersDecor);

        StickyRecyclerHeadersTouchListener touchListener =
                new StickyRecyclerHeadersTouchListener(recyclerView, headersDecor);
        touchListener.setOnHeaderClickListener(new StickyRecyclerHeadersTouchListener.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(View header, int position, long headerId) {
//                recyclerView.smoothScrollToPosition(mPresenter.getFirstItemByDate((Long) header.getTag()));
            }
        });
        recyclerView.addOnItemTouchListener(touchListener);
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerFragmentComponent.builder()
                .appComponent(appComponent)
                .fragmentModule(new FragmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onReLoadData() {
        mPresenter.loadTraceList(1);
    }

    @Override
    protected void onLoadMore(int page) {
        super.onLoadMore(page);
        mPresenter.loadTraceList(page);
    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_trace);
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        super.onItemClick(position, view);
        TraceExt trace = mAdapter.getData().get(position);
        if("user".equals(trace.getType())){
            View userAvatar = view.findViewById(R.id.avatar);
            ProfileActivity.show(getActivity(), userAvatar, trace.getUser().getLogin(),
                    trace.getUser().getAvatarUrl());
        } else {
            RepositoryActivity.show(getActivity(), trace.getRepository().getOwner().getLogin(),
                    trace.getRepository().getName());
        }
    }

    @Override
    public boolean onItemMoved(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemSwiped(int position, int direction) {
        mPresenter.removeTrace(position);
        if(mAdapter.getData().size() == 0){
            postNotifyDataSetChanged();
        } else {
            mAdapter.notifyItemRemoved(position);
        }
        Snackbar.make(recyclerView, R.string.trace_deleted, Snackbar.LENGTH_SHORT)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.undoRemoveTrace();
                    }
                })
                .show();
    }
}
