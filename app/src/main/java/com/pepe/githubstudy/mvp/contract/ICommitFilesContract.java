

package com.pepe.githubstudy.mvp.contract;


import com.pepe.githubstudy.mvp.contract.base.IBaseContract;
import com.pepe.githubstudy.mvp.model.CommitFile;
import com.pepe.githubstudy.mvp.model.CommitFilesPathModel;
import com.pepe.githubstudy.ui.adapter.base.DoubleTypesModel;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/10/18 15:21:28
 */

public interface ICommitFilesContract {

    interface View extends IBaseContract.View{

    }

    interface Presenter extends IBaseContract.Presenter<ICommitFilesContract.View>{
        ArrayList<DoubleTypesModel<CommitFilesPathModel, CommitFile>> getSortedList(ArrayList<CommitFile> commitFiles);
    }

}
