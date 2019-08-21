

package com.pepe.githubstudy.mvp.contract;

import android.support.annotation.NonNull;

import com.pepe.githubstudy.mvp.contract.base.IBaseContract;
import com.pepe.githubstudy.mvp.contract.base.IBaseListContract;
import com.pepe.githubstudy.mvp.contract.base.IBasePagerContract;
import com.pepe.githubstudy.mvp.model.ActivityRedirectionModel;
import com.pepe.githubstudy.mvp.model.Event;

import java.util.ArrayList;


/**
 * Created by ThirtyDegreesRay on 2017/8/23 21:51:44
 */

public interface IActivityContract {

    interface View extends IBaseContract.View, IBasePagerContract.View, IBaseListContract.View {
        void showEvents(ArrayList<Event> events);
    }

    interface Presenter extends IBasePagerContract.Presenter<IActivityContract.View>{
        void loadEvents(boolean isReload, int page);
        ArrayList<ActivityRedirectionModel> getRedirectionList(@NonNull Event event);
    }

}
