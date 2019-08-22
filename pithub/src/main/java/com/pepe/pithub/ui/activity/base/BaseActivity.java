package com.pepe.pithub.ui.activity.base;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.pepe.pithub.AppApplication;
import com.pepe.pithub.inject.component.AppComponent;
import com.pepe.pithub.mvp.contract.base.IBaseContract;
import com.pepe.pithub.ui.activity.LoginActivity;
import com.thirtydegreesray.dataautoaccess.DataAutoAccess;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

/**
 * @author 1one
 * @date 2019/8/22.
 */
public abstract class BaseActivity<P extends IBaseContract.Presenter> extends AppCompatActivity implements IBaseContract.View {

    @Inject
    protected P mPresenter;
    private static BaseActivity curActivity;
    private static final int TOAST_NORMAL = 1;
    private static final int TOAST_INFO = 2;
    private static final int TOAST_SUCCESS = 3;
    private static final int TOAST_ERROR = 4;
    private static final int TOAST_WARNING = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent(getAppComponent());
        DataAutoAccess.getData(this, savedInstanceState);
        if (mPresenter != null) {
            mPresenter.onRestoreInstanceState(savedInstanceState == null ?
                    getIntent().getExtras() : savedInstanceState);
            mPresenter.attachView(this);
        }
        if (getContentView() != 0) {
            setContentView(getContentView());
            ButterKnife.bind(getActivity());
        }

        initActivity();
        initView(savedInstanceState);
        if (mPresenter != null) {
            mPresenter.onViewInitialized();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //系统由于内存不足而杀死activity，此时保存数据
        DataAutoAccess.saveData(this, outState);
        if (mPresenter != null) {
            mPresenter.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    /**
     * 依赖注入的入口
     * @param appComponent appComponent
     */
    protected abstract void setupActivityComponent(AppComponent appComponent);

    /**
     * 获取ContentView id
     * @return
     */
    @LayoutRes
    protected abstract int getContentView();

    /**
     * 初始化activity
     */
    @CallSuper
    protected void initActivity() {

    }

    /**
     * 初始化view
     */
    @CallSuper
    protected void initView(Bundle savedInstanceState) {
    }


    @Override
    protected void onResume() {
        super.onResume();
        curActivity = getActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (this.equals(curActivity)) {
            curActivity = null;
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.with(this).onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.with(this).onLowMemory();
    }

    protected void setTransparentStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finishActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void finishActivity() {
        finish();
    }

    protected Fragment getVisibleFragment() {
        @SuppressLint("RestrictedApi")
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                if (fragment != null && fragment.isVisible()) {
                    return fragment;
                }
            }
        }
        return null;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showToast(int type, String message) {
        switch (type) {
            case TOAST_NORMAL:
                Toasty.normal(getActivity(), message).show();
                break;
            case TOAST_INFO:
                Toasty.info(getActivity(), message).show();
                break;
            case TOAST_SUCCESS:
                Toasty.success(getActivity(), message).show();
                break;
            case TOAST_ERROR:
                Toasty.error(getActivity(), message).show();
                break;
            case TOAST_WARNING:
                Toasty.warning(getActivity(), message).show();
                break;
            default:
                Toasty.normal(getActivity(), message).show();
                break;
        }

    }

    protected void postDelayFinish(int time) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, time);
    }

    public static BaseActivity getCurActivity() {
        return curActivity;
    }

    protected BaseActivity getActivity() {
        return this;
    }

    @NonNull
    protected AppApplication getAppApplication() {
        return (AppApplication) getApplication();
    }

    protected AppComponent getAppComponent() {
        return getAppApplication().getAppComponent();
    }

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

    @Override
    public void showLoginPage() {
        getActivity().finishAffinity();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

}
