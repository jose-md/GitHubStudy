

package com.pepe.githubstudy.ui.activity.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.pepe.githubstudy.R;
import com.pepe.githubstudy.mvp.contract.base.IBaseContract;
import com.pepe.githubstudy.ui.fragment.base.BaseFragment;
import com.pepe.githubstudy.utils.LogUtil;

import javax.inject.Inject;


public abstract class SingleFragmentActivity<P extends IBaseContract.Presenter, F extends Fragment>
        extends BaseDrawerActivity<P> implements IBaseContract.View{

    private F mFragment;
    private final String FRAGMENT_TAG = "mFragment";

    @Override
    protected int getContentView() {
        return R.layout.activity_single_fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarBackEnable();
        Fragment temp = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if(temp == null){
            mFragment = createFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, mFragment, FRAGMENT_TAG)
                    .commit();
           LogUtil.d("temp fragment is null");
        } else {
            mFragment = (F) temp;
           LogUtil.d("temp fragment not null");
        }
    }

    protected abstract F createFragment();

    protected F getFragment(){
        return mFragment;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
       LogUtil.d("onAttachFragment");
    }


    @Override
    protected void onToolbarDoubleClick() {
        super.onToolbarDoubleClick();
        if (mFragment instanceof BaseFragment){
            ((BaseFragment)mFragment).scrollToTop();
        }
    }
}
