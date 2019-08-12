

package com.pepe.githubstudy.mvp.presenter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.pepe.githubstudy.AppConfig;
import com.pepe.githubstudy.dao.DaoSession;
import com.pepe.githubstudy.http.core.HttpObserver;
import com.pepe.githubstudy.http.core.HttpResponse;
import com.pepe.githubstudy.http.core.HttpSubscriber;
import com.pepe.githubstudy.mvp.contract.ILoginContract;
import com.pepe.githubstudy.mvp.model.BasicToken;
import com.pepe.githubstudy.mvp.model.OauthToken;
import com.pepe.githubstudy.mvp.model.User;
import com.pepe.githubstudy.mvp.presenter.base.BasePresenter;
import com.pepe.githubstudy.utils.StringUtils;

import java.util.Date;
import java.util.UUID;


import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.Credentials;
import retrofit2.Response;

/**
 * Created on 2017/7/12.
 *
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

    }

    @NonNull
    @Override
    public String getOAuth2Url() {
      return "";
    }

    @Override
    public void basicLogin(String userName, String password) {

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

    }

//    private void saveAuthUser(BasicToken basicToken, User userInfo) {
//        String updateSql = "UPDATE " + daoSession.getAuthUserDao().getTablename()
//                + " SET " + AuthUserDao.Properties.Selected.columnName + " = 0";
//        daoSession.getAuthUserDao().getDatabase().execSQL(updateSql);
//
//        String deleteExistsSql = "DELETE FROM " + daoSession.getAuthUserDao().getTablename()
//                + " WHERE " + AuthUserDao.Properties.LoginId.columnName
//                + " = '" + userInfo.getLogin() + "'";
//        daoSession.getAuthUserDao().getDatabase().execSQL(deleteExistsSql);
//
//        AuthUser authUser = new AuthUser();
//        String scope = StringUtils.listToString(basicToken.getScopes(), ",");
//        Date date = new Date();
//        authUser.setAccessToken(basicToken.getToken());
//        authUser.setScope(scope);
//        authUser.setAuthTime(date);
//        authUser.setExpireIn(360 * 24 * 60 * 60);
//        authUser.setSelected(true);
//        authUser.setLoginId(userInfo.getLogin());
//        authUser.setName(userInfo.getName());
//        authUser.setAvatar(userInfo.getAvatarUrl());
//        daoSession.getAuthUserDao().insert(authUser);
//
//        AppData.INSTANCE.setAuthUser(authUser);
//        AppData.INSTANCE.setLoggedUser(userInfo);
//    }


}
