package com.pepe.githubstudy.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;


import com.pepe.githubstudy.mvp.contract.IMainContract;
import com.pepe.githubstudy.mvp.contract.ISplashContract;
import com.pepe.githubstudy.mvp.presenter.MainPresenter;
import com.pepe.githubstudy.mvp.presenter.SplashPresenter;
import com.pepe.githubstudy.ui.activity.base.BaseActivity;

/**
 * @author 1one
 * @date 2019/8/7.
 */
public class SplashActivity extends BaseActivity<SplashPresenter> implements ISplashContract.View {

    public static final String USER_NAME = "494778200pepe";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void showMainPage() {
        delayFinish();
        Uri dataUri = getIntent().getData();
        if (dataUri == null) {
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
    }

    @Override
    public void showLoginPage() {
        delayFinish();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    /**
     * 获取ContentView id
     * @return
     */
    @Override
    protected int getContentView() {
        return 0;
    }

    @Override
    protected void initActivity() {
        super.initActivity();
        showMainPage();
    }

    @Override
    protected void setPresenter() {
        mPresenter = new SplashPresenter();
    }
}
