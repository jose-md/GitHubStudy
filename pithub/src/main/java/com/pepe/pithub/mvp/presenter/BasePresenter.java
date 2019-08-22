package com.pepe.pithub.mvp.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import com.pepe.pithub.AppConfig;
import com.pepe.pithub.http.core.HttpObserver;
import com.pepe.pithub.http.core.HttpResponse;
import com.pepe.pithub.http.core.HttpSubscriber;
import com.pepe.pithub.http.error.HttpError;
import com.pepe.pithub.http.error.HttpErrorCode;
import com.pepe.pithub.http.error.HttpPageNoFoundError;
import com.pepe.pithub.http.error.UnauthorizedError;
import com.pepe.pithub.mvp.contract.base.IBaseContract;
import com.pepe.pithub.utils.LogUtil;
import com.pepe.pithub.utils.NetHelper;
import com.pepe.pithub.utils.PrefUtils;
import com.thirtydegreesray.dataautoaccess.DataAutoAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * @author 1one
 * @date 2019/8/22.
 */
public abstract class BasePresenter<V extends IBaseContract.View> implements IBaseContract.Presenter<V> {

    //View
    protected V mView;
    private ArrayList<HttpSubscriber> subscribers;
    private boolean isAttached = false;
    private boolean isViewInitialized = false;

    private CompositeDisposable mCompositeDisposable;

    public BasePresenter() {
        subscribers = new ArrayList<>();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        DataAutoAccess.saveData(this, outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle outState) {
        if (outState == null) {
            return;
        }
        DataAutoAccess.getData(this, outState);
    }

    /**
     * 绑定View
     * @param view view
     */
    @Override
    public void attachView(@NonNull V view) {
        LogUtil.d("===> attachView");
        mView = view;
        onViewAttached();
        isAttached = true;
        // when create UI
        mCompositeDisposable = new CompositeDisposable();
    }

    /**
     * 取消View绑定
     */
    @Override
    public void detachView() {
        mView = null;
        //view 取消绑定时，把请求取消订阅
        for (HttpSubscriber observer : subscribers) {
            if (observer != null && !observer.getDisposable().isDisposed()) {
                observer.getDisposable().dispose();
                LogUtil.d("unsubscribe:" + observer.toString());
            }
        }
        // when destroy UI
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear(); // clear时网络请求会随即cancel
            mCompositeDisposable = null;
        }
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

    @NonNull
    protected String getString(@StringRes int resId) {
        return getContext().getResources().getString(resId);
    }

    protected interface IObservableCreator<T> {
        Observable<Response<T>> createObservable(boolean forceNetWork);
    }


    /**
     * 一般的rx http请求执行
     * @param observable
     * @param subscriber null 表明不管数据回调
     * @param <T>
     */
    protected <T> void generalRxHttpExecute(
            @NonNull Observable<Response<T>> observable, @Nullable HttpSubscriber<T> subscriber, final ProgressDialog dialog) {
        if (subscriber != null) {
            // when request data
            subscribers.add(subscriber);
            observable.subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            if (dialog != null) {
                                dialog.show();
                            }
                        }
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
            if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
                mCompositeDisposable.add(subscriber.getDisposable());
            }
        } else {
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new HttpSubscriber<T>());
        }
    }

    protected <T> void generalRxHttpExecute(@NonNull IObservableCreator<T> observableCreator
            , @NonNull HttpObserver<T> httpObserver) {
        generalRxHttpExecute(observableCreator, httpObserver, false);
    }

    protected <T> void generalRxHttpExecute(@NonNull IObservableCreator<T> observableCreator
            , @NonNull HttpObserver<T> httpObserver, final boolean readCacheFirst) {
        generalRxHttpExecute(observableCreator, httpObserver, readCacheFirst, null);
    }

    //防止死循环
    private Map<String, Integer> requestTimesMap = new HashMap<>();

    protected <T> void generalRxHttpExecute(@NonNull final IObservableCreator<T> observableCreator
            , @NonNull final HttpObserver<T> httpObserver, final boolean readCacheFirst
            , @Nullable final ProgressDialog progressDialog) {
        requestTimesMap.put(observableCreator.toString(), 1);

        final HttpObserver<T> tempObserver = new HttpObserver<T>() {
            @Override
            public void onError(Throwable error) {
//                if (!checkIsUnauthorized(error)) {
                httpObserver.onError(error);
//                }
            }

            @Override
            public void onSuccess(@NonNull HttpResponse<T> response) {
                LogUtil.d("===> onSuccess");
                if (response.isSuccessful()) {
                    if (readCacheFirst && response.isFromCache()
                            && NetHelper.INSTANCE.getNetEnabled()
                            && requestTimesMap.get(observableCreator.toString()) < 2) {
                        requestTimesMap.put(observableCreator.toString(), 2);
                        generalRxHttpExecute(observableCreator.createObservable(true),
                                new HttpSubscriber<>(this), progressDialog);
                    }
                    httpObserver.onSuccess(response);
                } else if (response.getOriResponse().code() == 404) {
                    onError(new HttpPageNoFoundError());
                } else if (response.getOriResponse().code() == 504) {
                    onError(new HttpError(HttpErrorCode.NO_CACHE_AND_NETWORK));
                } else if (response.getOriResponse().code() == 401) {
                    onError(new UnauthorizedError());
                } else {
                    onError(new Error(response.getOriResponse().message()));
                }
            }
        };

        boolean cacheFirstEnable = PrefUtils.isCacheFirstEnable();
        generalRxHttpExecute(observableCreator.createObservable(!cacheFirstEnable || !readCacheFirst),
                new HttpSubscriber<>(tempObserver), progressDialog);
    }

    private <T> HttpSubscriber<T> getHttpSubscriber(HttpObserver<T> httpObserver) {
        return new HttpSubscriber<>(httpObserver);
    }

    private <T> T getServices(Class<T> serviceClass) {
        return getServices(serviceClass, AppConfig.GITHUB_API_BASE_URL, true);
    }

    protected <T> T getServices(Class<T> serviceClass, String baseUrl, boolean isJson) {
//        return AppRetrofit.INSTANCE
//                .getRetrofit(baseUrl, AppData.INSTANCE.getAccessToken(), isJson)
//                .create(serviceClass);
        return null;
    }
}
