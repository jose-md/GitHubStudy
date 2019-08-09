package com.pepe.githubstudy.mvp.presenter.base;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.pepe.githubstudy.mvp.contract.base.IBaseContract;
import com.pepe.githubstudy.utils.LogUtil;

import org.reactivestreams.Subscriber;

/**
 * @author 1one
 * @date 2019/8/7.
 */
public abstract class BasePresenter<V extends IBaseContract.View> implements IBaseContract.Presenter<V> {

    //View
    protected V mView;
    private boolean isAttached = false;
    private boolean isViewInitialized = false;
    /**
     * 绑定View
     *
     * @param view view
     */
    @Override
    public void attachView(@NonNull V view) {
        mView = view;
        onViewAttached();
        isAttached = true;
    }

    /**
     * 取消View绑定
     */
    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void onViewInitialized() {
        isViewInitialized = true;
    }

    protected boolean isViewInitialized() {
        return isViewInitialized;
    }

    /**
     * presenter和view绑定成功
     */
    @CallSuper
    protected void onViewAttached() {

    }

    /**
     * 获取上下文，需在onViewAttached()后调用
     *
     * @return
     */
    @NonNull
    @Override
    public Context getContext() {
        if (mView instanceof Context) {
            return (Context) mView;
        } else if (mView instanceof Fragment) {
            return ((Fragment) mView).getContext();
        } else {
            throw new NullPointerException("BasePresenter:mView is't instance of Context,can't use getContext() method.");
        }
    }
}
