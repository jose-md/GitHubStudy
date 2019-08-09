

package com.pepe.githubstudy.mvp.contract;


import com.pepe.githubstudy.mvp.contract.base.IBaseContract;

/**
 * Created on 2017/7/12.
 *
 * @author ThirtyDegreesRay
 */

public interface ISplashContract {

    interface View extends IBaseContract.View{
        void showMainPage();
    }

    interface Presenter extends IBaseContract.Presenter<ISplashContract.View>{

    }

}
