package com.pepe.githubstudy.mvp.presenter.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import com.pepe.githubstudy.R;
import com.pepe.githubstudy.http.GitHubWebPageService;
import com.pepe.githubstudy.http.core.AppRetrofit;
import com.pepe.githubstudy.http.core.HttpObserver;
import com.pepe.githubstudy.http.core.HttpResponse;
import com.pepe.githubstudy.http.core.HttpSubscriber;
import com.pepe.githubstudy.http.error.HttpError;
import com.pepe.githubstudy.http.error.HttpErrorCode;
import com.pepe.githubstudy.http.error.HttpPageNoFoundError;
import com.pepe.githubstudy.http.error.UnauthorizedError;
import com.pepe.githubstudy.mvp.contract.base.IBaseContract;
import com.pepe.githubstudy.utils.LogUtil;
import com.pepe.githubstudy.utils.NetHelper;
import com.pepe.githubstudy.utils.PrefUtils;
import com.pepe.githubstudy.utils.StringUtils;


import org.apache.http.conn.ConnectTimeoutException;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * @author 1one
 * @date 2019/8/7.
 */
public abstract class BasePresenter<V extends IBaseContract.View> implements IBaseContract.Presenter<V> {

    //View
    protected V mView;
    private ArrayList<HttpSubscriber> subscribers;
    private boolean isAttached = false;
    private boolean isViewInitialized = false;

    public BasePresenter() {
        subscribers = new ArrayList<>();
    }

    /**
     * 绑定View
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
        //view 取消绑定时，把请求取消订阅
        for (HttpSubscriber observer : subscribers) {
            if (observer != null && !observer.getDisposable().isDisposed()) {
                observer.getDisposable().dispose();
                LogUtil.d("unsubscribe:" + observer.toString());
            }
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
            @NonNull Observable<Response<T>> observable, @Nullable HttpSubscriber<T> subscriber,final ProgressDialog dialog) {
        if (subscriber != null) {
            subscribers.add(subscriber);
            observable.subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            dialog.show();
                        }
                    })
                    .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
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
//                    httpObserver.onError(error);
//                }
            }

            @Override
            public void onSuccess(@NonNull HttpResponse<T> response) {
                LogUtil.d("===> onError");
                if (response.isSuccessful()) {
                    if (readCacheFirst && response.isFromCache()
                            && NetHelper.INSTANCE.getNetEnabled()
                            && requestTimesMap.get(observableCreator.toString()) < 2) {
                        requestTimesMap.put(observableCreator.toString(), 2);
                        generalRxHttpExecute(observableCreator.createObservable(true),
                                new HttpSubscriber<>(this),progressDialog);
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
//        cacheFirstEnable = cacheFirstEnable || !NetHelper.INSTANCE.getNetEnabled();
        generalRxHttpExecute(observableCreator.createObservable(!cacheFirstEnable || !readCacheFirst),
                new HttpSubscriber<>(tempObserver),progressDialog);
    }

    private <T> HttpSubscriber<T> getHttpSubscriber(HttpObserver<T> httpObserver) {
        return new HttpSubscriber<>(httpObserver);
    }

    protected  <T> T getServices(Class<T> serviceClass, String baseUrl, boolean isJson){
        return AppRetrofit.INSTANCE
                .getRetrofit(baseUrl, null, isJson)
                .create(serviceClass);
    }

    @NonNull
    protected String getLoadTip() {
        return getContext().getString(R.string.loading).concat("...");
    }

    protected boolean isLastResponse(@NonNull HttpResponse response) {
        return response.isFromNetWork() ||
                !NetHelper.INSTANCE.getNetEnabled();
    }

    @NonNull
    protected String getErrorTip(@NonNull Throwable error) {
        String errorTip = null;
        if(error == null){
            return errorTip;
        }
        if(error instanceof UnknownHostException){
            errorTip = getString(R.string.no_network_tip);
        } else if (error instanceof SocketTimeoutException || error instanceof ConnectTimeoutException) {
            errorTip = getString(R.string.load_timeout_tip);
        } else if (error instanceof HttpError) {
            errorTip = error.getMessage();
        } else {
            errorTip = StringUtils.isBlank(error.getMessage()) ? error.toString() : error.getMessage();
        }
        return errorTip;
    }
}
