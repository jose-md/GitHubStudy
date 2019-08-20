

package com.pepe.githubstudy.mvp.contract;


import com.pepe.githubstudy.mvp.contract.base.IBaseContract;
import com.pepe.githubstudy.mvp.model.Branch;
import com.pepe.githubstudy.mvp.model.Repository;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/8/9 21:40:25
 */

public interface IRepositoryContract {

    interface View extends IBaseContract.View{
        void showRepo(Repository repo);
        void showBranchesAndTags(ArrayList<Branch> list, Branch curBranch);
        void invalidateOptionsMenu();
        void showStarWishes();
    }

    interface Presenter extends IBaseContract.Presenter<IRepositoryContract.View>{
        void loadBranchesAndTags();
        void starRepo(boolean star);
        void watchRepo(boolean watch);
        void createFork();
        boolean isForkEnable();
        boolean isBookmarked();
        void bookmark(boolean bookmark);
    }

}
