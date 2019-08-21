package com.pepe.githubstudy.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import com.pepe.githubstudy.R;
import com.pepe.githubstudy.inject.component.AppComponent;
import com.pepe.githubstudy.inject.component.DaggerFragmentComponent;
import com.pepe.githubstudy.inject.module.FragmentModule;
import com.pepe.githubstudy.mvp.contract.ICollectionsContract;
import com.pepe.githubstudy.mvp.model.Collection;
import com.pepe.githubstudy.mvp.presenter.CollectionsPresenter;
import com.pepe.githubstudy.ui.adapter.CollectionAdapter;
import com.pepe.githubstudy.ui.fragment.base.ListFragment;
import com.pepe.githubstudy.utils.PrefUtils;

import java.util.ArrayList;

/**
 * @author 1one
 * @date 2019/8/8.
 */
public class CollectionsFragment extends ListFragment<CollectionsPresenter, CollectionAdapter>
        implements ICollectionsContract.View {

    public static Fragment create(){
        return new CollectionsFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
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
    protected void setAdapter() {
        mAdapter = new CollectionAdapter(getActivity(),this);
    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        super.initFragment(savedInstanceState);
        setLoadMoreEnable(false);
    }

    @Override
    protected void onReLoadData() {
        mPresenter.loadCollections(true);
    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_repo_collections);
    }

    @Override
    public void showCollections(ArrayList<Collection> collections) {
        mAdapter.setData(collections);
        postNotifyDataSetChanged();
        if(collections != null && collections.size() > 0 && PrefUtils.isCollectionsTipAble()){
            showOperationTip(R.string.collections_tip);
            PrefUtils.set(PrefUtils.COLLECTIONS_TIP_ABLE, false);
        }
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        super.onItemClick(position, view);
//        RepoListActivity.showCollection(getActivity(), mAdapter.getData().get(position));
    }
}
