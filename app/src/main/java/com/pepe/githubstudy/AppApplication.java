package com.pepe.githubstudy;

import android.app.Application;

import com.pepe.githubstudy.dareen.EngineConfig;
import com.pepe.githubstudy.dareen.HttpUtils;
import com.pepe.githubstudy.http.convert.GsonConvert;
import com.pepe.githubstudy.http.retrofit.RetrofitRequest;
import com.pepe.githubstudy.inject.component.AppComponent;
import com.pepe.githubstudy.inject.component.DaggerAppComponent;
import com.pepe.githubstudy.inject.module.AppModule;

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
    }

    public static AppApplication get(){
        return application;
    }

    public AppComponent getAppComponent(){
        return mAppComponent;
    }

}
