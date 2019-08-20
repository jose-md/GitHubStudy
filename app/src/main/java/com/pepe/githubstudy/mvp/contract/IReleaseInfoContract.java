package com.pepe.githubstudy.mvp.contract;


import com.pepe.githubstudy.mvp.contract.base.IBaseContract;
import com.pepe.githubstudy.mvp.model.Release;

/**
 * Created by ThirtyDegreesRay on 2017/9/16 13:09:26
 */

public interface IReleaseInfoContract {

    interface View extends IBaseContract.View{
        void showReleaseInfo(Release release);
    }

    interface Presenter extends IBaseContract.Presenter<IReleaseInfoContract.View>{

    }

}
