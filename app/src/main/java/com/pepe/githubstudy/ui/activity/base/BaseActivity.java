package com.pepe.githubstudy.ui.activity.base;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.bumptech.glide.Glide;
import com.pepe.githubstudy.R;
import com.pepe.githubstudy.mvp.contract.base.IBaseContract;
import com.pepe.githubstudy.mvp.presenter.base.BasePresenter;
import com.pepe.githubstudy.ui.activity.LoginActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

/**
 * @author 1one
 * @date 2019/8/7.
 */
public abstract class BaseActivity<P extends IBaseContract.Presenter> extends AppCompatActivity implements IBaseContract.View {

    @BindView(R.id.toolbar)
    @Nullable
    protected Toolbar toolbar;
//    @BindView(R.id.toolbar_layout)
    @Nullable
    protected CollapsingToolbarLayout toolbarLayout;
    protected P mPresenter;
    private ProgressDialog mProgressDialog;

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
        if(mPresenter != null) {
            mPresenter.attachView(this);
            mPresenter.onViewInitialized();
        }
    }

    protected void initView(Bundle savedInstanceState) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
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

    public void finishActivity(){
        finish();
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

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showProgressDialog(String content) {
        getProgressDialog(content);
        mProgressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        if(mProgressDialog != null){
            mProgressDialog.dismiss();
        }else{
            throw new NullPointerException("dismissProgressDialog: can't dismiss a null dialog, must showForRepo dialog first!");
        }
    }

    @Override
    public ProgressDialog getProgressDialog(String content){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setMessage(content);
        return mProgressDialog;
    }

    @Override
    public void showToast(String message){
        Toasty.normal(getActivity(), message).show();
    }

    @Override
    public void showInfoToast(String message) {
        Toasty.info(getActivity(), message).show();
    }

    @Override
    public void showSuccessToast(String message) {
        Toasty.success(getActivity(), message).show();
    }

    @Override
    public void showErrorToast(String message) {
        Toasty.error(getActivity(), message).show();
    }

    @Override
    public void showWarningToast(String message) {
        Toasty.warning(getActivity(), message).show();
    }

    @Override
    public void showTipDialog(String content) {
        new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setTitle("提示")
                .setMessage(content)
                .setCancelable(true)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    @Override
    public void showConfirmDialog(String msn, String title, String confirmText
            , DialogInterface.OnClickListener confirmListener) {
        new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setTitle(title)
                .setMessage(msn)
                .setCancelable(true)
                .setPositiveButton(confirmText, confirmListener)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    @Override
    public void showLoginPage() {
        getActivity().finishAffinity();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
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
}
