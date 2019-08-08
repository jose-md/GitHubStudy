package com.pepe.githubstudy.mvp.presenter;

import android.content.Context;

import com.bumptech.glide.Glide;
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
 * @date 2019/8/7.
 */
public class SplashPresenter extends BasePresenter {


    public void getUserInfo(String username) {
        String url = "https://api.github.com/users/" + username;
        HttpUtils.with().get().url(url).request(new HttpCallBack<UserInfo>() {
            @Override
            public void onFailure(Exception e) {
                LogUtil.d("error : " + e.getMessage());
            }

            @Override
            protected void onSuccess(UserInfo resultObj) {
                LogUtil.d("avatar_url = " + resultObj.getAvatar_url());
                LogUtil.d("thread name = " + Thread.currentThread().getName());
                //设置图片圆角角度
                RoundedCorners roundedCorners = new RoundedCorners(6);
                //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
                RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);

                ((SplashActivity)mActivity).showMainPage();
            }
        });
    }
}
