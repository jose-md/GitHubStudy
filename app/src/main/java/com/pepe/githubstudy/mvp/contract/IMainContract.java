

package com.pepe.githubstudy.mvp.contract;

import android.support.annotation.NonNull;


import com.pepe.githubstudy.bean.UserInfo;
import com.pepe.githubstudy.mvp.contract.base.IBaseContract;

import java.util.List;

/**
 * Created on 2017/7/18.
 *
 * @author ThirtyDegreesRay
 */

public interface IMainContract {

    interface View extends IBaseContract.View{
        void restartApp();

        void getUserInfo(UserInfo userInfo);
    }

    interface Presenter extends IBaseContract.Presenter<IMainContract.View>{
        boolean isFirstUseAndNoNewsUser();
        void toggleAccount(@NonNull String loginId);
        void logout();
    }

}
