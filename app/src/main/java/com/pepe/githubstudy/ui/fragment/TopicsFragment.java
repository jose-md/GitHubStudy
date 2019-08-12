package com.pepe.githubstudy.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import com.pepe.githubstudy.R;
import com.pepe.githubstudy.http.convert.ITopicsContract;
import com.pepe.githubstudy.mvp.model.Topic;
import com.pepe.githubstudy.mvp.presenter.TopicsPresenter;
import com.pepe.githubstudy.ui.adapter.TopicsAdapter;
import com.pepe.githubstudy.ui.fragment.base.ListFragment;
import com.pepe.githubstudy.utils.LogUtil;
import com.pepe.githubstudy.utils.PrefUtils;

import java.util.ArrayList;

/**
 * @author 1one
 * @date 2019/8/8.
 */
public class TopicsFragment extends ListFragment<TopicsPresenter, TopicsAdapter>
        implements ITopicsContract.View {

    public static Fragment create(){
        LogUtil.d("===> create" );
        return new TopicsFragment();
    }

    @Override
    protected void setAdapter() {
        mAdapter = new TopicsAdapter(getActivity(),this);
    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        super.initFragment(savedInstanceState);
        setLoadMoreEnable(false);
    }

    @Override
    public void showTopics(ArrayList<Topic> topics) {
        mAdapter.setData(topics);
        postNotifyDataSetChanged();
        if(topics != null && topics.size() > 0 && PrefUtils.isTopicsTipEnable()){
            showOperationTip(R.string.topics_tip);
            PrefUtils.set(PrefUtils.TOPICS_TIP_ABLE, false);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void setPresenter() {
        LogUtil.d("===> setPresenter" );
        mPresenter = new TopicsPresenter();
    }


    @Override
    protected void onReLoadData() {
        mPresenter.loadTopics(true);
    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_topics);
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        super.onItemClick(position, view);
//        RepoListActivity.showTopic(getActivity(), adapter.getData().get(position));
    }
}
