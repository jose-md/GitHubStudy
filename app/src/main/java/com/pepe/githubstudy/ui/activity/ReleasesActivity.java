package com.pepe.githubstudy.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.pepe.githubstudy.R;
import com.pepe.githubstudy.inject.component.AppComponent;
import com.pepe.githubstudy.mvp.contract.base.IBaseContract;
import com.pepe.githubstudy.ui.activity.base.SingleFragmentActivity;
import com.pepe.githubstudy.ui.fragment.ReleasesFragment;
import com.pepe.githubstudy.utils.BundleHelper;


/**
 * Created by ThirtyDegreesRay on 2017/9/16 10:58:03
 */

public class ReleasesActivity extends SingleFragmentActivity<IBaseContract.Presenter, ReleasesFragment> {

    public static void show(Activity activity, String owner, String repo) {
        Intent intent = createIntent(activity, owner, repo);
        activity.startActivity(intent);
    }

    public static Intent createIntent(Activity activity, String owner, String repo) {
        return new Intent(activity, ReleasesActivity.class)
                .putExtras(BundleHelper.builder()
                        .put("owner", owner)
                        .put("repo", repo).build());
    }

    String owner;
    String repo;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        String subTitle = owner.concat("/").concat(repo);
        setToolbarTitle(getString(R.string.releases), subTitle);
        setToolbarScrollAble(true);
    }

    @Override
    protected ReleasesFragment createFragment() {
        return ReleasesFragment.create(owner, repo);
    }
}