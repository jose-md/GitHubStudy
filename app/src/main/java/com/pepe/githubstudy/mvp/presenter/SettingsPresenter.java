

package com.pepe.githubstudy.mvp.presenter;


import com.pepe.githubstudy.dao.DaoSession;
import com.pepe.githubstudy.mvp.contract.ISettingsContract;
import com.pepe.githubstudy.mvp.presenter.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created on 2017/8/1.
 *
 * @author ThirtyDegreesRay
 */

public class SettingsPresenter extends BasePresenter<ISettingsContract.View>
        implements ISettingsContract.Presenter{

    @Inject
    public SettingsPresenter(DaoSession daoSession) {
        super(daoSession);
    }


    @Override
    public void logout() {

    }
}
