

package com.pepe.pithub.inject.module;

import android.content.Context;


import com.pepe.pithub.inject.FragmentScope;
import com.pepe.pithub.ui.fragment.base.BaseFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 2017/7/18.
 *
 * @author ThirtyDegreesRay
 */
@Module
public class FragmentModule {

    private BaseFragment mFragment;

    public FragmentModule(BaseFragment fragment) {
        mFragment = fragment;
    }

    @Provides
    @FragmentScope
    public BaseFragment provideFragment(){
        return mFragment;
    }

    @Provides
    @FragmentScope
    public Context provideContext(){
        return mFragment.getActivity();
    }
}
