package com.pepe.githubstudy.http;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.pepe.githubstudy.service.GitHubService;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author 1one
 * @date 2019/8/6.
 */
public class HttpUtils {
    private IHttpRequest mHttpRequest;
    private static IHttpRequest mInitHttpRequest;
    private final int TYPE_POST = 0x0011, TYPE_GET = 0x0022;
    private int mType = TYPE_GET;
    private Map<String, Object> mParams;
    private String mUrl;
    // 指定配置 config 参数
    static EngineConfig mConfig;

    public static HttpUtils with() {
        return new HttpUtils();
    }

    public HttpUtils httpRequest(IHttpRequest httpRequest) {
        mHttpRequest = httpRequest;
        return this;
    }

    public HttpUtils get() {
        mType = TYPE_GET;
        return this;
    }

    private HttpUtils() {
        mParams = new HashMap<>();
    }

    public static void initConfig(EngineConfig engineConfig) {
        mConfig = engineConfig;
        mInitHttpRequest = mConfig.getEngineRequest();
    }

    public HttpUtils param(String key, Object value) {
        mParams.put(key, value);
        return this;
    }

    public HttpUtils url(String url) {
        this.mUrl = url;
        return this;
    }

    public HttpUtils cache(boolean cache) {
        // TODO
        return this;
    }

    public <T> void request() {
        request(null);
    }

    public <T> void request(final HttpCallBack<T> callback) {
        if (mHttpRequest == null) {
            mHttpRequest = mInitHttpRequest;
        }
        if (mHttpRequest == null) {
            throw new NullPointerException("HttpRequest 是空，请配置");
        }
        if (TextUtils.isEmpty(mUrl)) {
            throw new NullPointerException("访问路径为空");
        }
        // 异常判断
        if (mType == TYPE_GET) {
            mHttpRequest.get( mUrl, mParams, callback, true);
        } else if (mType == TYPE_POST) {
            mHttpRequest.post( mUrl, mParams, callback, true);
        }
    }

}
