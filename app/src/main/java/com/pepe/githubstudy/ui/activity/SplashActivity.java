package com.pepe.githubstudy.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;


import com.pepe.githubstudy.inject.component.AppComponent;
import com.pepe.githubstudy.inject.component.DaggerActivityComponent;
import com.pepe.githubstudy.inject.module.ActivityModule;
import com.pepe.githubstudy.mvp.contract.ISplashContract;
import com.pepe.githubstudy.mvp.presenter.SplashPresenter;
import com.pepe.githubstudy.ui.activity.base.BaseActivity;
import com.pepe.githubstudy.utils.LogUtil;

/**
 * @author 1one
 * @date 2019/8/7.
 */
public class SplashActivity extends BaseActivity<SplashPresenter> implements ISplashContract.View {

    @Override
    public void showMainPage() {
        delayFinish();
        Uri dataUri = getIntent().getData();
        if (dataUri == null) {
            startActivity(new Intent(getActivity(), MainActivity.class));
        } else {
            BrowserFilterActivity.handleBrowserUri(getActivity(), dataUri);
        }
    }

    @Override
    public void showLoginPage() {
        delayFinish();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    /**
     * 依赖注入的入口
     *
     * @param appComponent appComponent
     */
    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(new ActivityModule(getActivity()))
                .build()
                .inject(this);
    }

    /**
     * 获取ContentView id
     *
     * @return
     */
    @Override
    protected int getContentView() {
        return 0;
    }

    /**
     * 初始化activity
     */
    @Override
    protected void initActivity() {
        super.initActivity();
        mPresenter.getUser();
    }

    /**
     * 初始化view
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
//            case REQUEST_ACCESS_TOKEN:
//                if(resultCode == RESULT_OK){
//                    showMainPage();
//                }
//                break;
            default:
                break;
        }
    }

}
