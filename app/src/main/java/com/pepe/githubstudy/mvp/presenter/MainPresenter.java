package com.pepe.githubstudy.mvp.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pepe.githubstudy.bean.UserInfo;
import com.pepe.githubstudy.http.HttpCallBack;
import com.pepe.githubstudy.http.HttpUtils;
import com.pepe.githubstudy.mvp.contract.IMainContract;
import com.pepe.githubstudy.mvp.presenter.base.BasePresenter;

/**
 * @author 1one
 * @date 2019/8/8.
 */
public class MainPresenter extends BasePresenter<IMainContract.View>
        implements IMainContract.Presenter{

    public MainPresenter() {
    }

    public void getUserInfo(String username,HttpCallBack<UserInfo> callBack) {
        String url = "https://api.github.com/users/" + username;
        HttpUtils.with().get().url(url).request(callBack);
    }

    @Override
    public boolean isFirstUseAndNoNewsUser() {
        return false;
    }

    @Override
    public void toggleAccount(@NonNull String loginId) {

    }

    @Override
    public void logout() {

    }

}
