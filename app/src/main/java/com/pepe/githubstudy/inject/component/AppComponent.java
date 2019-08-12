

package com.pepe.githubstudy.inject.component;



import com.pepe.githubstudy.AppApplication;
import com.pepe.githubstudy.dao.DaoSession;
import com.pepe.githubstudy.inject.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    /**
     * 获取AppApplication
     * @return
     */
    AppApplication getApplication();

    /**
     * 获取数据库Dao
     * @return
     */
    DaoSession getDaoSession();

}
