

package com.pepe.githubstudy.mvp.presenter;

import android.os.Handler;


import com.pepe.githubstudy.R;
import com.pepe.githubstudy.dao.DaoSession;
import com.pepe.githubstudy.http.core.HttpObserver;
import com.pepe.githubstudy.http.core.HttpResponse;
import com.pepe.githubstudy.mvp.contract.ICommitDetailContract;
import com.pepe.githubstudy.mvp.model.GitHubName;
import com.pepe.githubstudy.mvp.model.RepoCommit;
import com.pepe.githubstudy.mvp.model.RepoCommitExt;
import com.pepe.githubstudy.mvp.presenter.base.BasePresenter;
import com.pepe.githubstudy.utils.GitHubHelper;
import com.pepe.githubstudy.utils.StringUtils;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by ThirtyDegreesRay on 2017/10/18 11:31:06
 */

public class CommitDetailPresenter extends BasePresenter<ICommitDetailContract.View>
        implements ICommitDetailContract.Presenter {

    String user;
    String repo;

    RepoCommit commit;
    String commitSha;
    String userAvatarUrl;
    String commitUrl;
    private RepoCommitExt commitExt;


    @Inject
    public CommitDetailPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        if (!StringUtils.isBlank(commitUrl)) {
            if (getInfoFromCommitUrl()) {
                loadCommitInfo();
            }
            return;
        }
        if (commit != null) {
            mView.showCommit(commit);
            commitSha = commit.getSha();
        } else {
            mView.showUserAvatar(userAvatarUrl);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mView == null) {
                    return;
                }
                loadCommitInfo();
            }
        }, 500);
    }

    @Override
    public void loadCommitInfo() {
        mView.showLoading();
        HttpObserver<RepoCommitExt> httpObserver = new HttpObserver<RepoCommitExt>() {
            @Override
            public void onError(Throwable error) {
                mView.hideLoading();
                mView.showErrorToast(getErrorTip(error));
            }

            @Override
            public void onSuccess(HttpResponse<RepoCommitExt> response) {
                mView.hideLoading();
                commitExt = response.body();
                mView.showCommitInfo(response.body());
            }
        };
        generalRxHttpExecute(new IObservableCreator<RepoCommitExt>() {
            @Override
            public Observable<Response<RepoCommitExt>> createObservable(boolean forceNetWork) {
                return getCommitService().getCommitInfo(forceNetWork, user, repo, commitSha);
            }
        }, httpObserver, true);
    }

    public RepoCommit getCommit() {
        return commit == null ? commitExt : commit;
    }

    //https://api.github.com/repos/ThirtyDegreesRay/OpenHub/commits/c4dbb2eade18b510378e46c7281822fc88517105",
    private boolean getInfoFromCommitUrl() {
        commitUrl = commitUrl.replace("api.github.com/repos", "github.com");
        GitHubName gitHubName = GitHubName.fromUrl(commitUrl);
        if (!GitHubHelper.isCommitUrl(commitUrl) || gitHubName == null) {
            mView.showErrorToast(getString(R.string.invalid_url));
            return false;
        }
        user = gitHubName.getUserName();
        repo = gitHubName.getRepoName();
        commitSha = gitHubName.getCommitShaName();
        return true;
    }

}
