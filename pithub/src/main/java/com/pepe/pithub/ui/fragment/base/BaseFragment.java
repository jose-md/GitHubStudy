package com.pepe.pithub.ui.fragment.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.pepe.pithub.AppApplication;
import com.pepe.pithub.R;
import com.pepe.pithub.inject.component.AppComponent;
import com.pepe.pithub.mvp.contract.base.IBaseContract;
import com.pepe.pithub.ui.activity.LoginActivity;
import com.thirtydegreesray.dataautoaccess.DataAutoAccess;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

/**
 * @author 1one
 * @date 2019/8/8.
 */
public abstract class BaseFragment<P extends IBaseContract.Presenter>
        extends Fragment implements IBaseContract.View {

    @Inject
    protected P mPresenter;
    Unbinder unbinder;
    /**
     * fragment for viewpager
     */
    private boolean isPagerFragment = false;
    private boolean isFragmentShowed = false;

    private static final int TOAST_NORMAL = 1;
    private static final int TOAST_INFO = 2;
    private static final int TOAST_SUCCESS = 3;
    private static final int TOAST_ERROR = 4;
    private static final int TOAST_WARNING = 5;

    protected abstract int getLayoutId();

    protected abstract void setupFragmentComponent(AppComponent appComponent);

    protected abstract void initFragment(Bundle savedInstanceState);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //some page contain WebView will make default language changed
        View fragmentView = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, fragmentView);
        initFragment(savedInstanceState);
        if (mPresenter != null) {
            mPresenter.onViewInitialized();
        }
        return fragmentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFragmentComponent(getAppComponent());
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        DataAutoAccess.getData(this, savedInstanceState);
        DataAutoAccess.getData(this, getArguments());
        if (mPresenter != null) {
            mPresenter.onRestoreInstanceState(getArguments());
        }
        if (mPresenter != null) {
            mPresenter.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        DataAutoAccess.saveData(this, outState);
        if (mPresenter != null) {
            mPresenter.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.with(this).onLowMemory();
    }

    public void onFragmentShowed() {
        isFragmentShowed = true;
    }

    public void onFragmentHided() {
        isFragmentShowed = false;
    }

    protected AppApplication getAppApplication() {
        return AppApplication.get();
    }

    protected AppComponent getAppComponent() {
        return getAppApplication().getAppComponent();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }



    @Override
    public void showToast(int type,String message) {
        switch (type) {
            case TOAST_NORMAL:
                Toasty.normal(getActivity(), message).show();
                break;
            case TOAST_INFO:
                Toasty.info(getActivity(), message).show();
                break;
            case TOAST_SUCCESS:
                Toasty.success(getActivity(), message).show();
                break;
            case TOAST_ERROR:
                Toasty.error(getActivity(), message).show();
                break;
            case TOAST_WARNING:
                Toasty.warning(getActivity(), message).show();
                break;
            default:
                Toasty.normal(getActivity(), message).show();
                break;
        }
    }

    public boolean isPagerFragment() {
        return isPagerFragment;
    }

    public BaseFragment setPagerFragment(boolean flag) {
        isPagerFragment = flag;
        return this;
    }

    public boolean isFragmentShowed() {
        return isFragmentShowed;
    }

    @Override
    public void showLoginPage() {
        getActivity().finishAffinity();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    public void scrollToTop() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
