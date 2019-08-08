package com.pepe.githubstudy.ui.activity.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.pepe.githubstudy.R;
import com.pepe.githubstudy.mvp.presenter.base.BasePresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author 1one
 * @date 2019/8/7.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    @Nullable
    protected Toolbar toolbar;
//    @BindView(R.id.toolbar_layout)
    @Nullable
    protected CollapsingToolbarLayout toolbarLayout;
    protected BasePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.ThemeLightTeal_Green);
        super.onCreate(savedInstanceState);
        if (getContentView() != 0) {
            setContentView(getContentView());
            ButterKnife.bind(getActivity());
        }
        setPresenter();
        initActivity();
        initView(savedInstanceState);
    }

    protected void initView(Bundle savedInstanceState) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    protected BaseActivity getActivity() {
        return this;
    }

    protected abstract void setPresenter();

    protected abstract int getContentView();


    protected abstract void initActivity();

    protected void delayFinish() {
        delayFinish(1000);
    }

    protected void delayFinish(int mills) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, mills);
    }

    protected void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
        if (toolbarLayout != null) {
            toolbarLayout.setTitle(title);
        }
    }

    protected Fragment getVisibleFragment(){
        @SuppressLint("RestrictedApi")
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if(fragmentList != null ){
            for(Fragment fragment : fragmentList){
                if(fragment != null && fragment.isVisible()){
                    return fragment;
                }
            }
        }
        return null;
    }
}
