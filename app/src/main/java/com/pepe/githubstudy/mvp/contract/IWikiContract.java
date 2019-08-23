package com.pepe.githubstudy.mvp.contract;


import com.pepe.githubstudy.mvp.contract.base.IBaseContract;
import com.pepe.githubstudy.mvp.contract.base.IBaseListContract;
import com.pepe.githubstudy.mvp.model.WikiModel;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/12/6 16:34:22
 */

public interface IWikiContract {

    interface View extends IBaseContract.View, IBaseListContract.View{
        void showWiki(ArrayList<WikiModel> wikiList);
    }

    interface Presenter extends IBaseContract.Presenter<IWikiContract.View>{
        void loadWiki(boolean isReload);
    }

}
