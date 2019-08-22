

package com.pepe.pithub.http.core;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

/**
 * 网络请求订阅者，处理网络请求的返回，判断数据有效性，同时处理网络请求异常情况<br>
 * Created by ThirtyDegreesRay on 2016/7/15 11:19
 */
public class HttpSubscriber<T> implements Observer<Response<T>> {

    private HttpObserver<T> mObserver;
    private Disposable mDisposable;

    public HttpSubscriber() {
    }

    public HttpSubscriber(HttpObserver<T> observer) {
        mObserver = observer;
    }

    public Disposable getDisposable(){
        return mDisposable;
    }

    @Override
    public void onError(Throwable e) {
        if (mObserver != null) {
            mObserver.onError(e);
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    @Override
    public void onNext(Response<T> r) {
        if (mObserver != null) {
            mObserver.onSuccess(new HttpResponse<>(r));
        }
    }
}
