

package com.pepe.githubstudy.mvp.presenter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.pepe.githubstudy.AppConfig;
import com.pepe.githubstudy.AppData;
import com.pepe.githubstudy.bean.UserInfo;
import com.pepe.githubstudy.dao.AuthUser;
import com.pepe.githubstudy.dao.AuthUserDao;
import com.pepe.githubstudy.dao.DaoSession;
import com.pepe.githubstudy.http.LoginService;
import com.pepe.githubstudy.http.UserService;
import com.pepe.githubstudy.http.core.AppRetrofit;
import com.pepe.githubstudy.http.core.HttpObserver;
import com.pepe.githubstudy.http.core.HttpResponse;
import com.pepe.githubstudy.http.core.HttpSubscriber;
import com.pepe.githubstudy.http.model.AuthRequestModel;
import com.pepe.githubstudy.mvp.contract.ILoginContract;
import com.pepe.githubstudy.mvp.model.BasicToken;
import com.pepe.githubstudy.mvp.model.OauthToken;
import com.pepe.githubstudy.mvp.model.User;
import com.pepe.githubstudy.mvp.presenter.base.BasePresenter;
import com.pepe.githubstudy.utils.LogUtil;
import com.pepe.githubstudy.utils.StringUtils;

import java.util.Date;
import java.util.UUID;


import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.Credentials;
import retrofit2.Response;

/**
 * Created on 2017/7/12.
 * @author ThirtyDegreesRay
 */

public class LoginPresenter extends BasePresenter<ILoginContract.View>
        implements ILoginContract.Presenter {


    @Inject
    public LoginPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void getToken(String code, String state) {
        Observable<Response<OauthToken>> observable =
                getLoginService().getAccessToken(AppConfig.OPENHUB_CLIENT_ID,
                        AppConfig.OPENHUB_CLIENT_SECRET, code, state);

        HttpSubscriber<OauthToken> subscriber =
                new HttpSubscriber<>(
                        new HttpObserver<OauthToken>() {
                            @Override
                            public void onError(@NonNull Throwable error) {
                                mView.dismissProgressDialog();
                                mView.showErrorToast(getErrorTip(error));
                            }

                            @Override
                            public void onSuccess(@NonNull HttpResponse<OauthToken> response) {
                                OauthToken token = response.body();
                                if (token != null) {
                                    mView.onGetTokenSuccess(BasicToken.generateFromOauthToken(token));
                                } else {
                                    mView.onGetTokenError(response.getOriResponse().message());
                                }
                            }
                        }
                );
        generalRxHttpExecute(observable, subscriber, mView.getProgressDialog(getLoadTip()));

    }

    @NonNull
    @Override
    public String getOAuth2Url() {
        String randomState = UUID.randomUUID().toString();
        return AppConfig.OAUTH2_URL +
                "?client_id=" + AppConfig.OPENHUB_CLIENT_ID +
                "&scope=" + AppConfig.OAUTH2_SCOPE +
                "&state=" + randomState;
    }

    @Override
    public void basicLogin(String userName, String password) {
        AuthRequestModel authRequestModel = AuthRequestModel.generate();
        String token = Credentials.basic(userName, password);
        Observable<Response<BasicToken>> observable =
                getLoginService(token).authorizations(authRequestModel);
        HttpSubscriber<BasicToken> subscriber =
                new HttpSubscriber<>(
                        new HttpObserver<BasicToken>() {
                            @Override
                            public void onError(@NonNull Throwable error) {
//                                mView.dismissProgressDialog();
                                LogUtil.d("===> onError");
                                mView.onGetTokenError(getErrorTip(error));
                            }

                            @Override
                            public void onSuccess(@NonNull HttpResponse<BasicToken> response) {
                                BasicToken token = response.body();
                                LogUtil.d("===> onSuccess  token = " + token.toString());
                                if (token != null) {
                                    mView.onGetTokenSuccess(token);
                                } else {
                                    mView.onGetTokenError(response.getOriResponse().message());
                                }
                            }
                        }
                );
        generalRxHttpExecute(observable, subscriber,null);
//        mView.showProgressDialog(getLoadTip());
    }

    @Override
    public void handleOauth(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null) {
            String code = uri.getQueryParameter("code");
            String state = uri.getQueryParameter("state");
            getToken(code, state);
        }
    }

    @Override
    public void getUserInfo(final BasicToken basicToken) {
        HttpSubscriber<User> subscriber = new HttpSubscriber<>(
                new HttpObserver<User>() {
                    @Override
                    public void onError(Throwable error) {
                        mView.dismissProgressDialog();
                        mView.showErrorToast(getErrorTip(error));
                    }

                    @Override
                    public void onSuccess(HttpResponse<User> response) {
                        mView.dismissProgressDialog();
                        User user = response.body();
                        if(user != null){
                            LogUtil.d("login = " + user.getLogin());
                            LogUtil.d("name = " + user.getName());
                            LogUtil.d("avatarUrl = " + user.getAvatarUrl());
                            saveAuthUser(basicToken, response.body());
                        }
                        mView.onLoginComplete();
                    }
                }
        );
        Observable<Response<User>> observable = getUserService(basicToken.getToken()).
                getPersonInfo(true);
        generalRxHttpExecute(observable, subscriber,null);
        mView.showProgressDialog(getLoadTip());
    }

    private void saveAuthUser(BasicToken basicToken, User userInfo) {
        String updateSql = "UPDATE " + daoSession.getAuthUserDao().getTablename()
                + " SET " + AuthUserDao.Properties.Selected.columnName + " = 0";
        daoSession.getAuthUserDao().getDatabase().execSQL(updateSql);

        String deleteExistsSql = "DELETE FROM " + daoSession.getAuthUserDao().getTablename()
                + " WHERE " + AuthUserDao.Properties.LoginId.columnName
                + " = '" + userInfo.getLogin() + "'";
        daoSession.getAuthUserDao().getDatabase().execSQL(deleteExistsSql);

        AuthUser authUser = new AuthUser();
        String scope = StringUtils.listToString(basicToken.getScopes(), ",");
        Date date = new Date();
        authUser.setAccessToken(basicToken.getToken());
        authUser.setScope(scope);
        authUser.setAuthTime(date);
        authUser.setExpireIn(360 * 24 * 60 * 60);
        authUser.setSelected(true);
        authUser.setLoginId(userInfo.getLogin());
        authUser.setName(userInfo.getName());
        authUser.setAvatar(userInfo.getAvatarUrl());
        daoSession.getAuthUserDao().insert(authUser);

        AppData.INSTANCE.setAuthUser(authUser);
        AppData.INSTANCE.setLoggedUser(userInfo);
    }
}
