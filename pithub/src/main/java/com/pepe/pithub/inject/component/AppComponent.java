

package com.pepe.pithub.inject.component;




import com.pepe.pithub.AppApplication;
import com.pepe.pithub.inject.module.AppModule;

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


}
