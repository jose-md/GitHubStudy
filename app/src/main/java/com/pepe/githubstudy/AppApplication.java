package com.pepe.githubstudy;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;

import com.pepe.githubstudy.dareen.EngineConfig;
import com.pepe.githubstudy.dareen.HttpUtils;
import com.pepe.githubstudy.http.convert.GsonConvert;
import com.pepe.githubstudy.http.retrofit.RetrofitRequest;
import com.pepe.githubstudy.inject.component.AppComponent;
import com.pepe.githubstudy.inject.component.DaggerAppComponent;
import com.pepe.githubstudy.inject.module.AppModule;
import com.pepe.githubstudy.service.NetBroadcastReceiver;
import com.pepe.githubstudy.utils.NetHelper;

/**
 * @author 1one
 * @date 2019/8/7.
 */
public class AppApplication extends Application {

    private static AppApplication application;
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        // 配置上层 配置
        EngineConfig config = new EngineConfig.Builder()
                .engineRequest(new RetrofitRequest())
                // 添加一个解析工厂 Gson Xml
                .converter(new GsonConvert())
                // 添加默认参数 platform = android
                .builder();
        HttpUtils.initConfig(config);

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        initNetwork();
    }

    private void initNetwork(){
        NetBroadcastReceiver receiver = new NetBroadcastReceiver();
        IntentFilter filter;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        } else {
            filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        }
        registerReceiver(receiver, filter);

        NetHelper.INSTANCE.init(this);
    }

    public static AppApplication get(){
        return application;
    }

    public AppComponent getAppComponent(){
        return mAppComponent;
    }

}
