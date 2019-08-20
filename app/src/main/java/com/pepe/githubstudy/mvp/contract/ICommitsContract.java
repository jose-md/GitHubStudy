

package com.pepe.githubstudy.mvp.contract;


import com.pepe.githubstudy.mvp.contract.base.IBaseContract;
import com.pepe.githubstudy.mvp.contract.base.IBaseListContract;
import com.pepe.githubstudy.mvp.contract.base.IBasePagerContract;
import com.pepe.githubstudy.mvp.model.RepoCommit;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/10/17 14:26:15
 */

public interface ICommitsContract {

    interface View extends IBaseContract.View, IBasePagerContract.View, IBaseListContract.View {
        void showCommits(ArrayList<RepoCommit> commits);
    }

    interface Presenter extends IBasePagerContract.Presenter<ICommitsContract.View>{
        void loadCommits(boolean isReload, int page);
    }

}
