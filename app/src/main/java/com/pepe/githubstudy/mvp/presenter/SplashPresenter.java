package com.pepe.githubstudy.mvp.presenter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.pepe.githubstudy.bean.UserInfo;
import com.pepe.githubstudy.http.HttpCallBack;
import com.pepe.githubstudy.http.HttpUtils;
import com.pepe.githubstudy.mvp.contract.ISplashContract;
import com.pepe.githubstudy.mvp.presenter.base.BasePresenter;
import com.pepe.githubstudy.ui.activity.SplashActivity;
import com.pepe.githubstudy.utils.LogUtil;

/**
 * @author 1one
 * @date 2019/8/7.
 */
public class SplashPresenter extends BasePresenter<ISplashContract.View>
        implements ISplashContract.Presenter{

}
