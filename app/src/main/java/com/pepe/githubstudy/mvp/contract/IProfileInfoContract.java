

package com.pepe.githubstudy.mvp.contract;


import com.pepe.githubstudy.mvp.contract.base.IBaseContract;
import com.pepe.githubstudy.mvp.contract.base.IBasePagerContract;
import com.pepe.githubstudy.mvp.model.User;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/8/23 14:33:59
 */

public interface IProfileInfoContract {

    interface View extends IBaseContract.View, IBasePagerContract.View{
        void showProfileInfo(User user);
        void showUserOrgs(ArrayList<User> orgs);
    }

    interface Presenter extends IBasePagerContract.Presenter<IProfileInfoContract.View>{

    }

}
