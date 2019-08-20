

package com.pepe.githubstudy.http.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pepe.githubstudy.AppApplication;
import com.pepe.githubstudy.http.HttpConfig;
import com.pepe.githubstudy.utils.FileUtil;
import com.pepe.githubstudy.utils.LogUtil;
import com.pepe.githubstudy.utils.NetHelper;
import com.pepe.githubstudy.utils.StringUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
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
//                .addNetworkInterceptor(new NetworkBaseInterceptor())
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

            //add unique login id in url to differentiate caches
//            if(AppData.INSTANCE.getLoggedUser() != null
//                    && !AppConfig.isCommonPageUrl(request.url().toString())){
//                HttpUrl url = request.url().newBuilder()
//                        .addQueryParameter("uniqueLoginId",
//                                AppData.INSTANCE.getLoggedUser().getLogin())
//                        .build();
//                request = request.newBuilder()
//                        .url(url)
//                        .build();
//            }

            //add access token
            if(!StringUtils.isBlank(token)){
                String auth = token.startsWith("Basic") ? token : "token " + token;
                request = request.newBuilder()
                        .addHeader("Authorization", auth)
                        .build();
            }
           LogUtil.d( request.url().toString());

            //第二次请求，强制使用网络请求
            String forceNetWork = request.header("forceNetWork");
            //有forceNetWork且无网络状态下取从缓存中取
            if (!StringUtils.isBlank(forceNetWork) &&
                    !NetHelper.INSTANCE.getNetEnabled()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            } else if("true".equals(forceNetWork)){
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
            }
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

    /**
     * 网络请求拦截器
     */
    private class NetworkBaseInterceptor implements Interceptor {
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {

            Request request = chain.request();
            Response originalResponse = chain.proceed(request);

//            String serverCacheControl = originalResponse.header("Cache-Control");
            String requestCacheControl = request.cacheControl().toString();

//            //若服务器端有缓存策略，则无需修改
//            if (StringUtil.isBlank(serverCacheControl)) {
//                return originalResponse;
//            }
//            //不设置缓存策略
//            else

            //有forceNetWork时，强制更改缓存策略
            String forceNetWork = request.header("forceNetWork");
            if(!StringUtils.isBlank(forceNetWork)){
                requestCacheControl = getCacheString();
            }

            if (StringUtils.isBlank(requestCacheControl)) {
                return originalResponse;
            }
            //设置缓存策略
            else {
                Response res = originalResponse.newBuilder()
//                        .header("Cache-Control", requestCacheControl)
                        //纠正服务器时间，服务器时间出错时可能会导致缓存处理出错
//                        .header("Date", getGMTTime())
                        .removeHeader("Pragma")
                        .build();
                return res;
            }

        }
    }

    private static String getGMTTime(){
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String gmtTime = format.format(date);
        return gmtTime;
    }

    public static String getCacheString(){
        return "public, max-age=" + HttpConfig.CACHE_MAX_AGE;
    }

}
