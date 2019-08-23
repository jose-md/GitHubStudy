

package com.pepe.githubstudy.mvp.contract;

import android.support.annotation.NonNull;


import com.pepe.githubstudy.mvp.contract.base.IBaseContract;
import com.pepe.githubstudy.mvp.model.Issue;
import com.pepe.githubstudy.mvp.model.Label;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/10/14 16:48:20
 */

public interface IEditIssueContract {

    interface View extends IBaseContract.View{
        void showNewIssue(@NonNull Issue issue);
        void onLoadLabelsComplete(ArrayList<Label> labels);
    }

    interface Presenter extends IBaseContract.Presenter<IEditIssueContract.View>{
        void commitIssue(@NonNull String title, @NonNull String comment);
        void loadLabels();
        void clearAllLabelsData();
    }

}
