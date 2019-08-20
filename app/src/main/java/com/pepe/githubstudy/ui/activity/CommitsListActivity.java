

package com.pepe.githubstudy.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pepe.githubstudy.R;
import com.pepe.githubstudy.mvp.contract.base.IBaseContract;
import com.pepe.githubstudy.ui.activity.base.SingleFragmentActivity;
import com.pepe.githubstudy.ui.fragment.CommitsFragment;
import com.pepe.githubstudy.utils.BundleHelper;

/**
 * Created by ThirtyDegreesRay on 2017/10/20 11:30:58
 */

public class CommitsListActivity extends SingleFragmentActivity<IBaseContract.Presenter, CommitsFragment> {

    public static void showForRepo(@NonNull Activity activity, @NonNull String user,
                                   @NonNull String repo, @Nullable String branch) {
        Intent intent = createIntentForRepo(activity, user, repo, branch);
        activity.startActivity(intent);
    }

    public static void showForCompare(@NonNull Activity activity, @NonNull String user,
                                      @NonNull String repo, @NonNull String before, @NonNull String head) {
        Intent intent = createIntentForCompare(activity, user, repo, before, head);
        activity.startActivity(intent);
    }

    public static Intent createIntentForCompare(@NonNull Activity activity, @NonNull String user,
                                                @NonNull String repo, @NonNull String before, @NonNull String head) {
        return new Intent(activity, CommitsListActivity.class)
                .putExtras(BundleHelper.builder()
                        .put("type", CommitsListActivity.CommitsListType.Compare)
                        .put("user", user)
                        .put("repo", repo)
                        .put("before", before)
                        .put("head", head).build());
    }

    public static Intent createIntentForRepo(@NonNull Activity activity, @NonNull String user,
                                             @NonNull String repo, @Nullable String branch) {
        return new Intent(activity, CommitsListActivity.class)
                .putExtras(BundleHelper.builder()
                        .put("type", CommitsListType.Repo)
                        .put("user", user)
                        .put("repo", repo)
                        .put("branch", branch)
                        .build());
    }

    public enum CommitsListType {
        Compare, Repo
    }

     CommitsListActivity.CommitsListType type;
    String user;
    String repo;

    String branch;

    String before;
    String head;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        String repoFullName = user.concat("/").concat(repo);
        setToolbarTitle(getToolbarTitle(), repoFullName);
        setToolbarScrollAble(true);
    }

    private String getToolbarTitle(){
        if (CommitsListType.Compare.equals(type)) {
            return getString(R.string.compare);
        } else if (CommitsListType.Repo.equals(type)) {
            return getString(R.string.commits).concat(" ").concat(branch);
        } else {
            return getString(R.string.commits);
        }
    }

    @Override
    protected CommitsFragment createFragment() {
        if (CommitsListType.Compare.equals(type)) {
            return CommitsFragment.createForCompare(user, repo, before, head);
        } else if (CommitsListType.Repo.equals(type)) {
            return CommitsFragment.createForRepo(user, repo, branch);
        } else {
            return null;
        }
    }


}
