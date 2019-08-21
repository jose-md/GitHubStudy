package com.pepe.githubstudy.mvp.presenter;

import android.support.annotation.NonNull;

import com.pepe.githubstudy.AppData;
import com.pepe.githubstudy.bean.UserInfo;
import com.pepe.githubstudy.dao.AuthUser;
import com.pepe.githubstudy.dao.AuthUserDao;
import com.pepe.githubstudy.dao.DaoSession;
import com.pepe.githubstudy.dareen.HttpCallBack;
import com.pepe.githubstudy.dareen.HttpUtils;
import com.pepe.githubstudy.mvp.contract.IMainContract;
import com.pepe.githubstudy.mvp.model.User;
import com.pepe.githubstudy.mvp.presenter.base.BasePresenter;
import com.pepe.githubstudy.utils.LogUtil;
import com.pepe.githubstudy.utils.PrefUtils;

import java.util.List;

import javax.inject.Inject;

/**
 * @author 1one
 * @date 2019/8/8.
 */
public class MainPresenter extends BasePresenter<IMainContract.View>
        implements IMainContract.Presenter{

    @Inject
    public MainPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    public void getUserInfo(String username) {
        String url = "https://api.github.com/users/" + username;
        HttpUtils.with().get().url(url).request(new HttpCallBack<UserInfo>() {
            @Override
            public void onFailure(Exception e) {
                LogUtil.d("error : " + e.getMessage());
            }

            @Override
            protected void onSuccess(UserInfo userInfo) {
                mView.getUserInfo(userInfo);
            }
        });
    }

    @Override
    public boolean isFirstUseAndNoNewsUser() {
        User user = AppData.INSTANCE.getLoggedUser();
        if(user.getFollowing() == 0
                && user.getPublicRepos() == 0 && user.getPublicGists() == 0
                && PrefUtils.isFirstUse()){
            PrefUtils.set(PrefUtils.FIRST_USE, false);
            return true;
        }
        return false;
    }

    @Override
    public List<AuthUser> getLoggedUserList() {
        List<AuthUser> users = daoSession.getAuthUserDao().loadAll();
        if(users != null){
            for(AuthUser user : users){
                if(AppData.INSTANCE.getLoggedUser().getLogin().equals(user.getLoginId())){
                    users.remove(user);
                    break;
                }
            }
        }
        return users;
    }

    @Override
    public void toggleAccount(@NonNull String loginId) {
        String removeSelectSql = "UPDATE " + daoSession.getAuthUserDao().getTablename()
                + " SET " + AuthUserDao.Properties.Selected.columnName + " = 0 "
                + " WHERE " + AuthUserDao.Properties.LoginId.columnName
                + " ='" + AppData.INSTANCE.getLoggedUser().getLogin() + "'";
        String selectSql = "UPDATE " + daoSession.getAuthUserDao().getTablename()
                + " SET " + AuthUserDao.Properties.Selected.columnName + " = 1"
                + " WHERE " + AuthUserDao.Properties.LoginId.columnName
                + " ='" + loginId + "'";
        daoSession.getAuthUserDao().getDatabase().execSQL(removeSelectSql);
        daoSession.getAuthUserDao().getDatabase().execSQL(selectSql);
        AppData.INSTANCE.setAuthUser(null);
        AppData.INSTANCE.setLoggedUser(null);
        mView.restartApp();
    }

    @Override
    public void logout() {
        daoSession.getAuthUserDao().delete(AppData.INSTANCE.getAuthUser());
        AppData.INSTANCE.setAuthUser(null);
        AppData.INSTANCE.setLoggedUser(null);
        mView.restartApp();
    }

}
