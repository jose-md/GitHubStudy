package com.pepe.githubstudy.mvp.presenter;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.pepe.githubstudy.bean.UserInfo;
import com.pepe.githubstudy.http.HttpCallBack;
import com.pepe.githubstudy.http.HttpUtils;
import com.pepe.githubstudy.mvp.presenter.base.BasePresenter;
import com.pepe.githubstudy.ui.activity.SplashActivity;
import com.pepe.githubstudy.utils.LogUtil;

/**
 * @author 1one
 * @date 2019/8/8.
 */
public class MainPresenter extends BasePresenter {

    public void getUserInfo(String username,HttpCallBack<UserInfo> callBack) {
        String url = "https://api.github.com/users/" + username;
        HttpUtils.with().get().url(url).request(callBack);
    }
}
