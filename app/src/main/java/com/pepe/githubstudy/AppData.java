

package com.pepe.githubstudy;

import android.support.annotation.Nullable;


import com.pepe.githubstudy.dao.AuthUser;
import com.pepe.githubstudy.mvp.model.User;
import com.pepe.githubstudy.utils.LogUtil;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;

import java.util.Locale;

/**
 * Created on 2017/7/14.
 * @author ThirtyDegreesRay
 */

public enum AppData {
    INSTANCE;

    @AutoAccess(dataName = "appData_loggedUser")
    User loggedUser;
    @AutoAccess(dataName = "appData_authUser")
    AuthUser authUser;
    @AutoAccess(dataName = "appData_systemDefaultLocal")
    Locale systemDefaultLocal;

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        LogUtil.d("setLoggedUser getAvatarUrl = " + loggedUser.getAvatarUrl());
        LogUtil.d("setLoggedUser getLogin = " + loggedUser.getLogin());
        this.loggedUser = loggedUser;
    }

    public AuthUser getAuthUser() {
        return authUser;
    }

    public void setAuthUser(AuthUser authUser) {
        this.authUser = authUser;
    }

    @Nullable
    public String getAccessToken() {
        return authUser == null ? null : authUser.getAccessToken();
    }

    public Locale getSystemDefaultLocal() {
        if (systemDefaultLocal == null) {
            systemDefaultLocal = Locale.getDefault();
        }
        return systemDefaultLocal;
    }

}
