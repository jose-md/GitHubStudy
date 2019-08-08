package com.pepe.githubstudy.mvp.presenter.base;

import android.app.Activity;

/**
 * @author 1one
 * @date 2019/8/7.
 */
public abstract class BasePresenter {

    public Activity mActivity;

    public void setView(Activity activity) {
        mActivity = activity;
    }
}
