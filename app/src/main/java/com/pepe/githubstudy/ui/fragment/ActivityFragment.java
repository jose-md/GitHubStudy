package com.pepe.githubstudy.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import com.pepe.githubstudy.R;
import com.pepe.githubstudy.inject.component.AppComponent;
import com.pepe.githubstudy.inject.component.DaggerFragmentComponent;
import com.pepe.githubstudy.inject.module.FragmentModule;
import com.pepe.githubstudy.mvp.contract.IActivityContract;
import com.pepe.githubstudy.mvp.model.ActivityRedirectionModel;
import com.pepe.githubstudy.mvp.model.Event;
import com.pepe.githubstudy.mvp.presenter.ActivityPresenter;
import com.pepe.githubstudy.ui.activity.CommitDetailActivity;
import com.pepe.githubstudy.ui.activity.CommitsListActivity;
import com.pepe.githubstudy.ui.activity.IssueDetailActivity;
import com.pepe.githubstudy.ui.activity.IssuesActivity;
import com.pepe.githubstudy.ui.activity.ProfileActivity;
import com.pepe.githubstudy.ui.activity.ReleasesActivity;
import com.pepe.githubstudy.ui.activity.RepositoryActivity;
import com.pepe.githubstudy.ui.adapter.ActivitiesAdapter;
import com.pepe.githubstudy.ui.fragment.base.ListFragment;
import com.pepe.githubstudy.ui.widget.ContextMenuRecyclerView;
import com.pepe.githubstudy.utils.BundleHelper;
import com.pepe.githubstudy.utils.LogUtil;
import com.pepe.githubstudy.utils.PrefUtils;

import java.util.ArrayList;

/**
 * @author 1one
 * @date 2019/8/8.
 */
public class ActivityFragment extends ListFragment<ActivityPresenter, ActivitiesAdapter>
        implements IActivityContract.View {

    public enum ActivityType {
        News, User, Repository, PublicNews
    }

    public static ActivityFragment create(@NonNull ActivityType type, @NonNull String user) {
        return create(type, user, null);
    }

    public static ActivityFragment create(@NonNull ActivityType type, @NonNull String user,
                                          @Nullable String repo) {
        ActivityFragment fragment = new ActivityFragment();
        fragment.setArguments(BundleHelper.builder().put("type", type)
                .put("user", user).put("repo", repo).build());
        return fragment;
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
        mAdapter = new ActivitiesAdapter(getActivity(), this);
    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        super.initFragment(savedInstanceState);
        setLoadMoreEnable(true);
        registerForContextMenu(recyclerView);
    }

    @Override
    protected void onReLoadData() {
        mPresenter.loadEvents(true, 1);
    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_activity);
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        super.onItemClick(position, view);
        Event event = mAdapter.getData().get(position);
        if (event.getRepo() == null) {
            ProfileActivity.show(getActivity(), null, event.getActor().getLogin(), event.getActor().getAvatarUrl());
            return;
        }
        //TODO to be better redirection
        String owner = event.getRepo().getFullName().split("/")[0];
        String repoName = event.getRepo().getFullName().split("/")[1];
        LogUtil.d("event.getType() = " + event.getType());
        switch (event.getType()) {
            case ForkEvent:
                String actorId = event.getActor().getLogin();
                RepositoryActivity.show(getContext(), actorId, event.getRepo().getName());
                break;
            case ReleaseEvent:
//                ReleaseInfoActivity.show(getActivity(), owner, repoName,
//                        event.getPayload().getRelease().getTagName());
                break;
            case IssueCommentEvent:
            case IssuesEvent:
                IssueDetailActivity.show(getActivity(), owner, repoName,
                        event.getPayload().getIssue().getNumber());
                break;
            case PushEvent:
                if (event.getPayload().getCommits() == null) {
                    RepositoryActivity.show(getContext(), owner, event.getRepo().getName());
                } else if (event.getPayload().getCommits().size() == 1) {
                    View userAvatar = view.findViewById(R.id.user_avatar);
                    CommitDetailActivity.show(getActivity(), owner, repoName,
                            event.getPayload().getCommits().get(0).getSha(), userAvatar,
                            event.getActor().getAvatarUrl());
                } else {
                    CommitsListActivity.showForCompare(getActivity(), owner, repoName,
                            event.getPayload().getBefore(), event.getPayload().getHead());
                }
                break;
            default:
                RepositoryActivity.show(getContext(), owner, event.getRepo().getName());
                break;
        }
    }

    @Override
    public boolean onItemLongClick(int position, @NonNull View view) {
        PrefUtils.set(PrefUtils.ACTIVITY_LONG_CLICK_TIP_ABLE, false);
        return super.onItemLongClick(position, view);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.go_to);
        ContextMenuRecyclerView.RecyclerViewContextMenuInfo info =
                (ContextMenuRecyclerView.RecyclerViewContextMenuInfo) menuInfo;
        Event event = mAdapter.getData().get(info.getPosition());
        ArrayList<ActivityRedirectionModel> redirectionModels = mPresenter.getRedirectionList(event);
        for (ActivityRedirectionModel model : redirectionModels) {
            addRedirectionMenuItem(menu, model);
        }
    }

    private void addRedirectionMenuItem(ContextMenu menu, ActivityRedirectionModel model) {
        String text = null;
        Intent intent = null;
        String owner = model.getOwner();
        String repo = model.getRepoName();
        switch (model.getType()) {
            case User:
                text = getString(R.string.user) + " " + model.getActor();
                intent = ProfileActivity.createIntent(getActivity(), model.getActor());
                break;
            case Repo:
                text = getString(R.string.repo) + " " + repo;
                intent = RepositoryActivity.createIntent(getActivity(), owner, repo);
                break;
            case Fork:
                text = getString(R.string.forks) + " " + repo;
                intent = RepositoryActivity.createIntent(getActivity(), model.getActor(), repo);
                break;

            case Issues:
                text = getString(R.string.issues_list);
                intent = IssuesActivity.createIntentForRepo(getActivity(), owner, repo);
                break;
            case Issue:
                text = getString(R.string.issue) + " " + model.getIssue().getNumber();
                intent = IssueDetailActivity.createIntent(getActivity(), owner,
                        repo, model.getIssue().getNumber());
                break;
            case Commits:
                text = getString(R.string.commits_list);
                intent = CommitsListActivity.createIntentForRepo(getActivity(), owner,
                        repo, model.getBranch());
                break;
            case CommitCompare:
                text = getString(R.string.compare);
                intent = CommitsListActivity.createIntentForCompare(getActivity(), owner,
                        repo, model.getDiffBefore(), model.getDiffHead());
                break;
            case Commit:
                text = getString(R.string.commit) + " " + model.getCommitSha().substring(0, 7);
                intent = CommitDetailActivity.createIntent(getActivity(), owner,
                        repo, model.getCommitSha());
                break;

            case Releases:
                text = getString(R.string.releases_list);
                intent = ReleasesActivity.createIntent(getActivity(), owner, repo);
                break;
            case Release:
//                text = getString(R.string.release) + " " + model.getRelease().getTagName();
//                intent = ReleaseInfoActivity.createIntent(getActivity(), owner,
//                        repo, model.getRelease().getTagName());
                break;
        }
        menu.add(text).setIntent(intent);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getIntent() != null) {
            startActivity(item.getIntent());
        }
        return true;
    }

    @Override
    protected void onLoadMore(int page) {
        super.onLoadMore(page);
        mPresenter.loadEvents(false, page);
    }

    @Override
    public void showEvents(ArrayList<Event> events) {
        mAdapter.setData(events);
        postNotifyDataSetChanged();
        if (getCurPage() == 2 && PrefUtils.isActivityLongClickTipAble()) {
            showOperationTip(R.string.activity_click_tip);
            PrefUtils.set(PrefUtils.ACTIVITY_LONG_CLICK_TIP_ABLE, false);
        }
    }

    @Override
    public void onFragmentShowed() {
        super.onFragmentShowed();
        if (mPresenter != null) {
            mPresenter.prepareLoadData();
        }
    }

}
