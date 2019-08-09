package com.pepe.githubstudy.mvp.presenter;

import android.support.annotation.NonNull;

import com.pepe.githubstudy.bean.UserInfo;
import com.pepe.githubstudy.http.HttpCallBack;
import com.pepe.githubstudy.http.HttpUtils;
import com.pepe.githubstudy.mvp.contract.IMainContract;
import com.pepe.githubstudy.mvp.presenter.base.BasePresenter;
import com.pepe.githubstudy.utils.LogUtil;

/**
 * @author 1one
 * @date 2019/8/8.
 */
public class MainPresenter extends BasePresenter<IMainContract.View>
        implements IMainContract.Presenter{

    public MainPresenter() {
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
        return false;
    }

    @Override
    public void toggleAccount(@NonNull String loginId) {

    }

    @Override
    public void logout() {

    }

}
