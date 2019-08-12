package com.pepe.githubstudy.http.retrofit;

import android.util.Log;

import com.pepe.githubstudy.utils.LogUtil;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author 1one
 * @date 2019/8/7.
 */
public class RetrofitClient {
    private final static RetrofitApi mRetrofitApi;

    static {
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        LogUtil.d(message);
                    }
                }).setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        // 各种套路和招式 ，发现问题解决问题，基础，源码的理解
        // 1. 没打印？
        // 2. 数据格式不一致？成功 data 是个对象，不成功 data 是个 String
        // 3. 还有就是 baseUrl 问题？ (Retrofit 找不到任何入口可以修改)
        //        3.1 不同的 baseUrl 构建不同的 Retrofit 对象 （直不应该首选）
        //        3.2 自己想办法，取巧也行走漏洞
        Retrofit retrofit = new Retrofit.Builder()
                // 访问后台接口的主路径
                // .baseUrl("http://ppw.zmzxd.cn/index.php/api/v1/")
                // 添加解析转换工厂,Gson 解析，Xml解析，等等
                // 不要转换工厂默认的情况下返回的是 ResponseBody ,有一个方面是可以去切换解析工厂
                // .addConverterFactory(GsonConverterFactory.create())
                // .addCallAdapterFactory()
                // 添加 OkHttpClient,不添加默认就是 光杆 OkHttpClient
                .baseUrl("http://ppw.zmzxd.cn/index.php/api/v1/")
                .client(okHttpClient)
                .build();

        // 创建一个 实例对象
        mRetrofitApi = retrofit.create(RetrofitApi.class);
    }

    public static RetrofitApi getServiceApi() {
        return mRetrofitApi;
    }
}
