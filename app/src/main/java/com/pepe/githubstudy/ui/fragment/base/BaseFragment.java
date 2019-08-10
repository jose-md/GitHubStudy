package com.pepe.githubstudy.ui.fragment.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pepe.githubstudy.mvp.presenter.base.BasePresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author 1one
 * @date 2019/8/8.
 */
public abstract class BaseFragment extends Fragment {

    protected BasePresenter mPresenter;
    private ProgressDialog mProgressDialog;
    Unbinder unbinder;
    /**
     * fragment for viewpager
     */
    private boolean isPagerFragment = false;
    private boolean isFragmentShowed = false;

    protected abstract int getLayoutId();

    protected abstract void initFragment(Bundle savedInstanceState);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //some page contain WebView will make default language changed
        View fragmentView = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, fragmentView);
        initFragment(savedInstanceState);
        return fragmentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (mPresenter != null) {
//            mPresenter.onRestoreInstanceState(getArguments());
//        }
    }

    public void scrollToTop() {

    }

    public void onFragmentShowed() {
        isFragmentShowed = true;
    }

    public void onFragmentHided() {
        isFragmentShowed = false;
    }

    public BaseFragment setPagerFragment(boolean flag) {
        isPagerFragment = flag;
        return this;
    }
}
