package com.pepe.githubstudy.http.convert;


import com.pepe.githubstudy.mvp.contract.base.IBaseContract;
import com.pepe.githubstudy.mvp.contract.base.IBaseListContract;
import com.pepe.githubstudy.mvp.model.Topic;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/12/29 11:08:23
 */

public interface ITopicsContract {

    interface View extends IBaseContract.View, IBaseListContract.View{
        void showTopics(ArrayList<Topic> topics);
    }

    interface Presenter extends IBaseContract.Presenter<ITopicsContract.View>{
        void loadTopics(boolean isReload);
    }

}
