

package com.pepe.pithub.http.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pepe.pithub.AppApplication;
import com.pepe.pithub.http.HttpConfig;
import com.pepe.pithub.utils.FileUtil;
import com.pepe.pithub.utils.LogUtil;
import com.pepe.pithub.utils.StringUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Retrofit 网络请求
 * Created by ThirtyDegreesRay on 2016/7/15 11:39
 */
public enum AppRetrofit {
    INSTANCE;

    private HashMap<String, Retrofit> retrofitMap = new HashMap<>();
    private String token;

    private void createRetrofit(@NonNull String baseUrl, boolean isJson) {
        int timeOut = HttpConfig.HTTP_TIME_OUT;
        Cache cache = new Cache(FileUtil.getHttpImageCacheDir(AppApplication.get()),
                HttpConfig.HTTP_MAX_CACHE_SIZE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(timeOut, TimeUnit.MILLISECONDS)
                .addInterceptor(new BaseInterceptor())
                .cache(cache)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient);

        if(isJson){
            builder.addConverterFactory(GsonConverterFactory.create());
        } else {
            builder.addConverterFactory(SimpleXmlConverterFactory.createNonStrict());
        }

        retrofitMap.put(baseUrl + "-" + isJson, builder.build());
    }

    public Retrofit getRetrofit(@NonNull String baseUrl, @Nullable String token, boolean isJson) {
        this.token = token;
        String key = baseUrl + "-" + isJson;
        if (!retrofitMap.containsKey(key)) {
            createRetrofit(baseUrl, isJson);
        }
        return retrofitMap.get(key);
    }

    public Retrofit getRetrofit(@NonNull String baseUrl, @Nullable String token) {
        return getRetrofit(baseUrl, token, true);
    }

    /**
     * 拦截器
     */
    private class BaseInterceptor implements Interceptor {
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            //add access token
            if(!StringUtils.isBlank(token)){
                String auth = token.startsWith("Basic") ? token : "token " + token;
                request = request.newBuilder()
                        .addHeader("Authorization", auth)
                        .build();
            }
           LogUtil.d( request.url().toString());
            Response response = chain.proceed(request);
            ResponseBody responseBody = response.body();
            long contentLength = responseBody.contentLength();
            LogUtil.d("<-- "
                    + response.code()
                    + (response.message().isEmpty() ? "" : ' ' + response.message())
                    + ' ' + response.request().url()
                    );
            LogUtil.d("body = " + responseBody.toString());
            return response;
        }
    }

}
