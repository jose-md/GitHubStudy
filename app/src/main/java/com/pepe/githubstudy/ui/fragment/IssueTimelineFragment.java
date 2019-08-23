package com.pepe.githubstudy.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.pepe.githubstudy.R;
import com.pepe.githubstudy.inject.component.AppComponent;
import com.pepe.githubstudy.inject.component.DaggerFragmentComponent;
import com.pepe.githubstudy.inject.module.FragmentModule;
import com.pepe.githubstudy.mvp.contract.IIssueTimelineContract;
import com.pepe.githubstudy.mvp.model.Issue;
import com.pepe.githubstudy.mvp.model.IssueEvent;
import com.pepe.githubstudy.mvp.presenter.IssueTimelinePresenter;
import com.pepe.githubstudy.ui.activity.IssueDetailActivity;
import com.pepe.githubstudy.ui.activity.MarkdownEditorActivity;
import com.pepe.githubstudy.ui.activity.ViewerActivity;
import com.pepe.githubstudy.ui.adapter.IssueTimelineAdapter;
import com.pepe.githubstudy.ui.fragment.base.ListFragment;
import com.pepe.githubstudy.utils.BundleHelper;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/9/27 16:32:49
 */

public class IssueTimelineFragment extends ListFragment<IssueTimelinePresenter, IssueTimelineAdapter>
        implements IIssueTimelineContract.View {

    public static IssueTimelineFragment create(@NonNull Issue issue){
        IssueTimelineFragment fragment = new IssueTimelineFragment();
        fragment.setArguments(BundleHelper.builder().put("issue", issue).build());
        return fragment;
    }

    @AutoAccess
    String editingCommentId;

    @Override
    protected void setAdapter() {
        mAdapter = new IssueTimelineAdapter(getActivity(),this);
    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        super.initFragment(savedInstanceState);
        setLoadMoreEnable(true);
        setAutoJudgeCanLoadMoreEnable(false);
    }

    @Override
    public void showTimeline(ArrayList<IssueEvent> events) {
        mAdapter.setData(events);
        postNotifyDataSetChanged();
    }

    @Override
    public void showEditCommentPage(String commentId, String body) {
        MarkdownEditorActivity.show(getActivity(), R.string.comment,
                IssueDetailActivity.EDIT_COMMENT_REQUEST_CODE,
                body);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerFragmentComponent.builder()
                .appComponent(appComponent)
                .fragmentModule(new FragmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onReLoadData() {
        mPresenter.loadTimeline(1, true);
    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_comments);
    }

    @Override
    protected void onLoadMore(int page) {
        super.onLoadMore(page);
        mPresenter.loadTimeline(page, false);
    }

    @Override
    protected int getHeaderSize() {
        return 1;
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        super.onItemClick(position, view);
        if(IssueEvent.Type.commented.equals(mAdapter.getData().get(position).getType())){
            ViewerActivity.showMdSource(getActivity(), getString(R.string.comment),
                    mAdapter.getData().get(position).getBodyHtml());
        }
    }

    public void addComment(IssueEvent event){
        mPresenter.getTimeline().add(event);
        postNotifyDataSetChanged();
        recyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
    }

    @Override
    public boolean onItemLongClick(final int position, @NonNull View view) {
        final IssueEvent issueEvent = mAdapter.getData().get(position);
        if(!IssueEvent.Type.commented.equals(issueEvent.getType())){
            return true;
        }

        String[] actions ;
        if(mPresenter.isEditAndDeleteEnable(position) && position != 0){
            actions = new String[]{getString(R.string.share), getString(R.string.edit), getString(R.string.delete)};
        } else {
            actions = new String[]{getString(R.string.share)};

        }
        new AlertDialog.Builder(getActivity())
                .setItems(actions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which){
                            case 0:
                                // TODO AppOpener
//                                AppOpener.shareText(getActivity(), issueEvent.getHtmlUrl());
                                break;
                            case 1:
                                editingCommentId = issueEvent.getId();
                                showEditCommentPage(editingCommentId, issueEvent.getBody());
                                break;
                            case 2:
                                showDeleteCommentWarning(position, issueEvent.getId());
                                break;
                        }
                    }
                })
                .show();
        return true;
    }

    private void showDeleteCommentWarning(final int position, final String commentId){
        new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setTitle(R.string.warning_dialog_tile)
                .setMessage(R.string.delete_comment_warning)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mAdapter.getData().remove(position);
                        mAdapter.notifyItemRemoved(position);
                        mPresenter.deleteComment(commentId);
                    }
                })
                .show();
    }

    public void onEditComment(String body) {
        mPresenter.editComment(editingCommentId, body);
    }

    public void onEditIssue(Issue issue){
        mPresenter.setIssue(issue);
        mAdapter.getData().get(0).setBody(issue.getBody());
        mAdapter.getData().get(0).setBodyHtml(issue.getBodyHtml());
        mAdapter.getData().get(0).setParentIssue(issue);
        mAdapter.notifyItemChanged(0);
    }

    public ArrayList<String> getIssueUsersExceptMe(){
        return mPresenter.getIssueUsersExceptMe();
    }

}
