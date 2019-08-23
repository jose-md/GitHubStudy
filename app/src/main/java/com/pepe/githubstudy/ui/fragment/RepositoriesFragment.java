package com.pepe.githubstudy.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.pepe.githubstudy.AppData;
import com.pepe.githubstudy.R;
import com.pepe.githubstudy.inject.component.AppComponent;
import com.pepe.githubstudy.inject.component.DaggerFragmentComponent;
import com.pepe.githubstudy.inject.module.FragmentModule;
import com.pepe.githubstudy.mvp.contract.IRepositoriesContract;
import com.pepe.githubstudy.mvp.model.Collection;
import com.pepe.githubstudy.mvp.model.Repository;
import com.pepe.githubstudy.mvp.model.SearchModel;
import com.pepe.githubstudy.mvp.model.Topic;
import com.pepe.githubstudy.mvp.model.TrendingLanguage;
import com.pepe.githubstudy.mvp.model.filter.RepositoriesFilter;
import com.pepe.githubstudy.mvp.model.filter.TrendingSince;
import com.pepe.githubstudy.mvp.presenter.RepositoriesPresenter;
import com.pepe.githubstudy.ui.activity.RepositoryActivity;
import com.pepe.githubstudy.ui.activity.TrendingActivity;
import com.pepe.githubstudy.ui.adapter.RepositoriesAdapter;
import com.pepe.githubstudy.ui.fragment.base.ListFragment;
import com.pepe.githubstudy.ui.fragment.base.OnDrawerSelectedListener;
import com.pepe.githubstudy.utils.BundleHelper;
import com.pepe.githubstudy.utils.LogUtil;

import java.util.ArrayList;

/**
 * @author 1one
 * @date 2019/8/8.
 */
public class RepositoriesFragment extends ListFragment<RepositoriesPresenter, RepositoriesAdapter>
        implements IRepositoriesContract.View, OnDrawerSelectedListener,
        TrendingActivity.LanguageUpdateListener{

    public enum RepositoriesType{
        OWNED, PUBLIC, STARRED, TRENDING, SEARCH, FORKS, TRACE, BOOKMARK, COLLECTION, TOPIC
    }

    public static RepositoriesFragment create(@NonNull RepositoriesType type,
                                              @NonNull String user){
        RepositoriesFragment fragment = new RepositoriesFragment();
        fragment.setArguments(BundleHelper.builder().put("type", type)
                .put("user", user).build());
        return fragment;
    }

    public static RepositoriesFragment createForCollection(@NonNull Collection collection){
        RepositoriesFragment fragment = new RepositoriesFragment();
        fragment.setArguments(BundleHelper.builder()
                .put("type", RepositoriesType.COLLECTION)
                .put("collection", collection)
                .build());
        return fragment;
    }

    public static RepositoriesFragment createForTopic(@NonNull Topic topic){
        RepositoriesFragment fragment = new RepositoriesFragment();
        fragment.setArguments(BundleHelper.builder()
                .put("type", RepositoriesType.TOPIC)
                .put("topic", topic)
                .build());
        return fragment;
    }

    public static RepositoriesFragment createForForks(@NonNull String user, @NonNull String repo){
        RepositoriesFragment fragment = new RepositoriesFragment();
        fragment.setArguments(BundleHelper.builder()
                .put("type", RepositoriesType.FORKS)
                .put("user", user)
                .put("repo", repo)
                .build());
        return fragment;
    }

    public static RepositoriesFragment createForSearch(@NonNull SearchModel searchModel){
        RepositoriesFragment fragment = new RepositoriesFragment();
        fragment.setArguments(
                BundleHelper.builder()
                        .put("type", RepositoriesType.SEARCH)
                        .put("searchModel", searchModel)
                        .build()
        );
        return fragment;
    }

    public static RepositoriesFragment createForTrending(@NonNull TrendingSince since){
        RepositoriesFragment fragment = new RepositoriesFragment();
        fragment.setArguments(
                BundleHelper.builder()
                        .put("type", RepositoriesType.TRENDING)
                        .put("since", since)
                        .build()
        );
        return fragment;
    }

    public static RepositoriesFragment createForTrace(){
        RepositoriesFragment fragment = new RepositoriesFragment();
        fragment.setArguments(BundleHelper.builder().put("type", RepositoriesType.TRACE).build());
        return fragment;
    }

    public static RepositoriesFragment createForBookmark(){
        RepositoriesFragment fragment = new RepositoriesFragment();
        fragment.setArguments(BundleHelper.builder().put("type", RepositoriesType.BOOKMARK).build());
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void showRepositories(ArrayList<Repository> repositoryList) {
        LogUtil.d("===> showRepositories");
        mAdapter.setData(repositoryList);
        postNotifyDataSetChanged();
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
        mAdapter = new RepositoriesAdapter(getActivity(),this);
    }

    @Override
    protected void initFragment(Bundle savedInstanceState){
        super.initFragment(savedInstanceState);
        setLoadMoreEnable(!RepositoriesType.COLLECTION.equals(mPresenter.getType()));
    }

    @Override
    protected void onReLoadData() {
        mPresenter.loadRepositories(true, 1);
    }

    @Override
    protected String getEmptyTip() {
        if(RepositoriesType.TRENDING.equals(mPresenter.getType())){
            return String.format(getString(R.string.no_trending_repos), mPresenter.getLanguage().getName());
        }
        return getString(R.string.no_repository);
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        super.onItemClick(position, view);
        if(RepositoriesType.TRENDING.equals(mPresenter.getType())
                || RepositoriesType.TRACE.equals(mPresenter.getType())
                || RepositoriesType.BOOKMARK.equals(mPresenter.getType())
                || RepositoriesType.COLLECTION.equals(mPresenter.getType())){
            RepositoryActivity.show(getActivity(), mAdapter.getData().get(position).getOwner().getLogin(),
                    mAdapter.getData().get(position).getName());
        } else {
            RepositoryActivity.show(getActivity(), mAdapter.getData().get(position));
        }
    }

    @Override
    protected void onLoadMore(int page) {
        super.onLoadMore(page);
        mPresenter.loadRepositories(false, page);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        if(mPresenter.getType().equals(RepositoriesType.OWNED)){
//            inflater.inflate(R.menu.menu_owned_repo, menu);
//            if(!mPresenter.getUser().equals(AppData.INSTANCE.getLoggedUser().getLogin())){
//                menu.findItem(R.id.action_filter_public).setVisible(false);
//                menu.findItem(R.id.action_filter_private).setVisible(false);
//            }
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentShowed() {
        super.onFragmentShowed();
        if(mPresenter != null) {
            mPresenter.prepareLoadData();
        }
    }

    @Override
    public void onDrawerSelected(@NonNull NavigationView navView, @NonNull MenuItem item) {
        RepositoriesFilter filter = RepositoriesFilter.generateFromDrawer(navView);
        mPresenter.loadRepositories(filter);
    }

    @Override
    public void onLanguageUpdate(TrendingLanguage language) {
        if(mPresenter != null){
            mPresenter.setLanguage(language);
            mPresenter.setLoaded(false);
            mPresenter.prepareLoadData();
        } else {
            getArguments().putParcelable("language", language);
        }
    }
}
