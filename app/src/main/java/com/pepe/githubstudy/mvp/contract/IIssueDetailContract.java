package com.pepe.githubstudy.mvp.contract;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pepe.githubstudy.mvp.contract.base.IBaseContract;
import com.pepe.githubstudy.mvp.model.Issue;
import com.pepe.githubstudy.mvp.model.IssueEvent;


/**
 * Created by ThirtyDegreesRay on 2017/9/26 16:18:18
 */

public interface IIssueDetailContract {

    interface View extends IBaseContract.View{
        void showIssue(@NonNull Issue issue);
        void showAddedComment(@NonNull IssueEvent event);
        void showAddCommentPage(@Nullable String text);
    }

    interface Presenter extends IBaseContract.Presenter<IIssueDetailContract.View> {
        void addComment(@NonNull String text);
        void toggleIssueState();
    }

}
