package com.pepe.githubstudy.mvp.presenter;

import com.pepe.githubstudy.dao.DaoSession;
import com.pepe.githubstudy.mvp.contract.ISplashContract;
import com.pepe.githubstudy.mvp.presenter.base.BasePresenter;

import javax.inject.Inject;

/**
 * @author 1one
 * @date 2019/8/7.
 */
public class SplashPresenter extends BasePresenter<ISplashContract.View>
        implements ISplashContract.Presenter{

    @Inject
    public SplashPresenter(DaoSession daoSession) {
        super(daoSession);
    }

}
