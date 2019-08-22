

package com.pepe.pithub.mvp.contract.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by ThirtyDegreesRay on 2017/8/11 9:56:42
 */

public interface IBaseContract {

    interface View {

        void showToast(int type,String message);

        void showLoading();

        void hideLoading();

        void showLoginPage();

    }

    interface Presenter<V extends IBaseContract.View>{

        void onSaveInstanceState(Bundle outState);

        void onRestoreInstanceState(Bundle outState);

        void attachView(@NonNull V view);

        void detachView();

        /**
         * view initialized, you can init view data
         */
        void onViewInitialized();

        @Nullable
        Context getContext();
    }

}
