

package com.pepe.githubstudy.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import com.pepe.githubstudy.R;
import com.pepe.githubstudy.inject.component.AppComponent;
import com.pepe.githubstudy.ui.activity.base.PagerActivity;
import com.pepe.githubstudy.ui.adapter.base.FragmentPagerModel;
import com.pepe.githubstudy.ui.adapter.base.FragmentViewPagerAdapter;
import com.pepe.githubstudy.ui.fragment.NotificationsFragment;

/**
 * Created by ThirtyDegreesRay on 2017/11/6 17:38:52
 */

public class NotificationsActivity extends PagerActivity {

    public static void show(@NonNull Activity activity) {
        Intent intent = new Intent(activity, NotificationsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initActivity() {
        super.initActivity();
        pagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager());
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarScrollAble(true);
        setToolbarBackEnable();
        setToolbarTitle(getString(R.string.notifications));

        pagerAdapter.setPagerList(FragmentPagerModel.createNotificationsPagerList(getActivity(), getFragments()));
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(pagerAdapter);
        showFirstPager();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_view_pager;
    }

    @Override
    public int getPagerSize() {
        return 3;
    }

    @Override
    protected int getFragmentPosition(Fragment fragment) {
        if (fragment instanceof NotificationsFragment) {
            NotificationsFragment.NotificationsType type
                    = (NotificationsFragment.NotificationsType) fragment.getArguments().get("type");
            if (NotificationsFragment.NotificationsType.Unread.equals(type)) {
                return 0;
            } else if (NotificationsFragment.NotificationsType.Participating.equals(type)) {
                return 1;
            } else if (NotificationsFragment.NotificationsType.All.equals(type)) {
                return 2;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

}
