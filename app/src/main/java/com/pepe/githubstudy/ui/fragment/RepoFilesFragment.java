

package com.pepe.githubstudy.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;


import com.pepe.githubstudy.R;
import com.pepe.githubstudy.inject.component.AppComponent;
import com.pepe.githubstudy.inject.component.DaggerFragmentComponent;
import com.pepe.githubstudy.inject.module.FragmentModule;
import com.pepe.githubstudy.mvp.contract.IRepoFilesContract;
import com.pepe.githubstudy.mvp.model.Branch;
import com.pepe.githubstudy.mvp.model.FileModel;
import com.pepe.githubstudy.mvp.model.FilePath;
import com.pepe.githubstudy.mvp.model.Repository;
import com.pepe.githubstudy.mvp.presenter.RepoFilesPresenter;
import com.pepe.githubstudy.ui.activity.RepositoryActivity;
import com.pepe.githubstudy.ui.activity.base.PagerActivity;
import com.pepe.githubstudy.ui.adapter.FilePathAdapter;
import com.pepe.githubstudy.ui.adapter.RepoFilesAdapter;
import com.pepe.githubstudy.ui.adapter.base.BaseViewHolder;
import com.pepe.githubstudy.ui.fragment.base.ListFragment;
import com.pepe.githubstudy.utils.BundleHelper;


import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by ThirtyDegreesRay on 2017/8/14 16:13:20
 */

public class RepoFilesFragment extends ListFragment<RepoFilesPresenter, RepoFilesAdapter>
        implements IRepoFilesContract.View, PagerActivity.IFragmentKeyListener,
        RepositoryActivity.RepositoryListener {

    public static RepoFilesFragment create(Repository repository) {
        RepoFilesFragment fragment = new RepoFilesFragment();
        fragment.setArguments(BundleHelper.builder().put("repo", repository).build());
        return fragment;
    }

    @BindView(R.id.path_recycler_view)
    RecyclerView pathRecyclerView;
    @Inject
    FilePathAdapter filePathAdapter;

    @Override
    public void showFiles(ArrayList<FileModel> files) {
        mAdapter.setData(files);
        postNotifyDataSetChanged();
    }

    @Override
    public void showFilePath(final ArrayList<FilePath> filePath) {
        filePathAdapter.setData(filePath);
        filePathAdapter.notifyDataSetChanged();
        if (filePath.size() != 0) {
            pathRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    pathRecyclerView.smoothScrollToPosition(filePath.size() - 1);
                }
            });
        }
    }

    @Override
    public void showLoading() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_repo_files;
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
        mAdapter = new RepoFilesAdapter(getActivity());
    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        super.initFragment(savedInstanceState);
        pathRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        pathRecyclerView.setAdapter(filePathAdapter);
        filePathAdapter.setOnItemClickListener(
                new BaseViewHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, @NonNull View view) {
                        mPresenter.loadFiles(filePathAdapter.getData().get(position).getFullPath(), false);
                    }
                }
        );
    }

    @Override
    protected void onReLoadData() {
        mPresenter.loadFiles(true);
    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_file);
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        super.onItemClick(position, view);
        FileModel model = mAdapter.getData().get(position);
        if (model.isDir()) {
            mPresenter.loadFiles(model.getPath(), false);
        } else {
//            ViewerActivity.show(getActivity(), model, mPresenter.getRepoName());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            return mPresenter.goBack();
        }
        return false;
    }

    @Override
    public void onFragmentShowed() {
        super.onFragmentShowed();
        if (mPresenter != null) {
            mPresenter.prepareLoadData();
        }
    }

    @Override
    public void onRepositoryInfoUpdated(Repository repository) {

    }

    @Override
    public void onBranchChanged(Branch branch) {
        if (mPresenter == null) {
            getArguments().putString("branch", branch.getName());
        } else {
            mPresenter.setCurBranch(branch.getName());
            mPresenter.setLoaded(false);
            mPresenter.prepareLoadData();
        }
    }
}
