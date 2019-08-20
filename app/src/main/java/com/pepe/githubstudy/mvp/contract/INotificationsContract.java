

package com.pepe.githubstudy.mvp.contract;

import android.support.annotation.NonNull;


import com.pepe.githubstudy.mvp.contract.base.IBaseContract;
import com.pepe.githubstudy.mvp.contract.base.IBaseListContract;
import com.pepe.githubstudy.mvp.contract.base.IBasePagerContract;
import com.pepe.githubstudy.mvp.model.Notification;
import com.pepe.githubstudy.mvp.model.Repository;
import com.pepe.githubstudy.ui.adapter.base.DoubleTypesModel;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/11/6 17:44:57
 */

public interface INotificationsContract {

    interface View extends IBaseContract.View, IBasePagerContract.View, IBaseListContract.View{
        void showNotifications(ArrayList<DoubleTypesModel<Repository, Notification>> notifications);
    }

    interface Presenter extends IBasePagerContract.Presenter<INotificationsContract.View> {
        void loadNotifications(int page, boolean isReload);
        void markNotificationAsRead(String threadId);
        void markAllNotificationsAsRead();
        boolean isNotificationsAllRead();
        void markRepoNotificationsAsRead(@NonNull Repository repository);
    }

}
