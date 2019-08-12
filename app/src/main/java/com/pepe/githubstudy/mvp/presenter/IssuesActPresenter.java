package com.pepe.githubstudy.mvp.presenter;

import com.pepe.githubstudy.dao.DaoSession;
import com.pepe.githubstudy.mvp.contract.IIssuesActContract;
import com.pepe.githubstudy.mvp.presenter.base.BasePresenter;

/**
 * @author 1one
 * @date 2019/8/12.
 */
public class IssuesActPresenter  extends BasePresenter<IIssuesActContract.View>
        implements IIssuesActContract.Presenter{

    public IssuesActPresenter(DaoSession daoSession) {
        super(daoSession);
    }
}
