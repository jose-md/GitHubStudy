package com.pepe.githubstudy.mvp.contract;


import com.pepe.githubstudy.mvp.contract.base.IBaseContract;
import com.pepe.githubstudy.mvp.contract.base.IBaseListContract;
import com.pepe.githubstudy.mvp.model.TraceExt;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/11/23 10:53:42
 */

public interface ITraceContract {

    interface View extends IBaseContract.View, IBaseListContract.View{
        void showTraceList(ArrayList<TraceExt> traceList);
        void notifyItemAdded(int position);
    }

    interface Presenter extends IBaseContract.Presenter<ITraceContract.View>{
        void loadTraceList(int page);
        void removeTrace(int position);
        void undoRemoveTrace();
        int getFirstItemByDate(long dateTime);
    }

}
