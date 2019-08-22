package com.pepe.pithub;

import android.app.Application;

import com.pepe.pithub.inject.component.AppComponent;
import com.pepe.pithub.inject.component.DaggerAppComponent;
import com.pepe.pithub.inject.module.AppModule;

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
