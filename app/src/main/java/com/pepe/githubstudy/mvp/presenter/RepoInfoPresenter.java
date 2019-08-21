

package com.pepe.githubstudy.mvp.presenter;


import com.pepe.githubstudy.AppConfig;
import com.pepe.githubstudy.dao.DaoSession;
import com.pepe.githubstudy.http.core.HttpObserver;
import com.pepe.githubstudy.http.core.HttpResponse;
import com.pepe.githubstudy.http.error.HttpPageNoFoundError;
import com.pepe.githubstudy.mvp.contract.IRepoInfoContract;
import com.pepe.githubstudy.mvp.model.Repository;
import com.pepe.githubstudy.mvp.presenter.base.BasePagerPresenter;
import com.pepe.githubstudy.utils.StringUtils;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
/**
 * Created by ThirtyDegreesRay on 2017/8/11 11:34:31
 */

public class RepoInfoPresenter extends BasePagerPresenter<IRepoInfoContract.View>
        implements IRepoInfoContract.Presenter{

    @AutoAccess
    Repository repository;
    @AutoAccess String curBranch = "";
    private String readmeSource;

    @Inject
    public RepoInfoPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
    }

    @Override
    protected void loadData() {
        mView.showRepoInfo(repository);
        if(readmeSource == null){
            loadReadMe();
        }
    }

    @Override
    public void loadReadMe() {
        final String readmeFileUrl = AppConfig.GITHUB_API_BASE_URL + "repos/" + repository.getFullName()
                + "/" + "readme" + (StringUtils.isBlank(curBranch) ? "" : "?ref=" + curBranch);

        String branch = StringUtils.isBlank(curBranch) ? repository.getDefaultBranch() : curBranch;
        final String baseUrl = AppConfig.GITHUB_BASE_URL + repository.getFullName()
                + "/blob/" + branch  + "/" + "README.md";

//        if(!StringUtils.isBlank(readmeSource)){
//            mView.showReadMe(readmeSource, baseUrl);
//            return;
//        }

        mView.showReadMeLoader();
        HttpObserver<ResponseBody> httpObserver = new HttpObserver<ResponseBody>() {
            @Override
            public void onError(Throwable error) {
                if(error instanceof HttpPageNoFoundError){
                    mView.showNoReadMe();
                } else {
                    mView.showErrorToast(getErrorTip(error));
                }
            }

            @Override
            public void onSuccess(HttpResponse<ResponseBody> response) {
                try {
                    readmeSource = response.body().string();
                    mView.showReadMe(readmeSource, baseUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        generalRxHttpExecute(new IObservableCreator<ResponseBody>() {
            @Override
            public Observable<Response<ResponseBody>> createObservable(boolean forceNetWork) {
                return getRepoService().getFileAsHtmlStream(forceNetWork, readmeFileUrl);
            }
        }, httpObserver, true);

    }

    public Repository getRepository() {
        return repository;
    }

    /**
     * check if the string size is too large to save
     */
    private void checkReadmeSourceSize(){
        if(readmeSource != null && readmeSource.getBytes().length > 128 * 1024){
            readmeSource = null;
        }
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public void setCurBranch(String curBranch) {
        this.curBranch = curBranch;
        readmeSource = null;
    }
}
