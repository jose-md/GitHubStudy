package com.pepe.githubstudy.mvp.presenter;


import com.pepe.githubstudy.dao.DaoSession;
import com.pepe.githubstudy.mvp.contract.IIssuesActContract;
import com.pepe.githubstudy.mvp.presenter.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by ThirtyDegreesRay on 2017/9/20 17:22:16
 */

public class IssuesActPresenter extends BasePresenter<IIssuesActContract.View>
        implements IIssuesActContract.Presenter{

    @Inject
    public IssuesActPresenter(DaoSession daoSession) {
        super(daoSession);
    }

}
