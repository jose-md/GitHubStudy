package com.pepe.githubstudy.mvp.presenter;

import com.pepe.githubstudy.dao.DaoSession;
import com.pepe.githubstudy.http.core.HttpObserver;
import com.pepe.githubstudy.http.core.HttpResponse;
import com.pepe.githubstudy.mvp.contract.IWikiContract;
import com.pepe.githubstudy.mvp.model.WikiFeedModel;
import com.pepe.githubstudy.mvp.model.WikiModel;
import com.pepe.githubstudy.mvp.presenter.base.BasePresenter;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;

import org.xmlpull.v1.XmlPullParserException;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by ThirtyDegreesRay on 2017/12/6 16:41:32
 */

public class WikiPresenter extends BasePresenter<IWikiContract.View>
        implements IWikiContract.Presenter {

    @AutoAccess
    String owner;
    @AutoAccess
    String repo;
    private ArrayList<WikiModel> wikiList;

    @Inject
    public WikiPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        loadWiki(false);
    }

    @Override
    public void loadWiki(boolean isReload) {
        mView.showLoading();
        HttpObserver<WikiFeedModel> httpObserver = new HttpObserver<WikiFeedModel>() {
            @Override
            public void onError(Throwable error) {
                mView.hideLoading();
                if (error.getCause() != null && error.getCause() instanceof XmlPullParserException) {
                    mView.showWiki(null);
                } else {
                    mView.showLoadError(getErrorTip(error));
                }
            }

            @Override
            public void onSuccess(HttpResponse<WikiFeedModel> response) {
                mView.hideLoading();
                wikiList = response.body().getWikiList();
                mView.showWiki(wikiList);
            }
        };
        generalRxHttpExecute(
                new IObservableCreator<WikiFeedModel>() {
                    @Override
                    public Observable<Response<WikiFeedModel>> createObservable(boolean forceNetWork) {
                        return getGitHubWebPageService()
                                .getWiki(forceNetWork, owner, repo);
                    }
                }
                , httpObserver, !isReload);

    }

}
