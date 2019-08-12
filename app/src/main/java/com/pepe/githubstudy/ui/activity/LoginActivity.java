package com.pepe.githubstudy.ui.activity;

import com.pepe.githubstudy.inject.component.AppComponent;
import com.pepe.githubstudy.inject.module.ActivityModule;
import com.pepe.githubstudy.ui.activity.base.BaseActivity;

/**
 * @author 1one
 * @date 2019/8/7.
 */
public class LoginActivity extends BaseActivity {


    /**
     * 依赖注入的入口
     *
     * @param appComponent appComponent
     */
    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
//        DaggerActivityComponent.builder()
//                .appComponent(appComponent)
//                .activityModule(new ActivityModule(getActivity()))
//                .build()
//                .inject(this);
    }

    @Override
    protected int getContentView() {
        return 0;
    }

    @Override
    protected void initActivity() {
        super.initActivity();
    }
}
