

package com.pepe.githubstudy.mvp.contract;

import android.support.annotation.NonNull;


import com.pepe.githubstudy.mvp.contract.base.IBaseContract;
import com.pepe.githubstudy.mvp.contract.base.IBaseListContract;
import com.pepe.githubstudy.mvp.contract.base.IBasePagerContract;
import com.pepe.githubstudy.mvp.model.FileModel;
import com.pepe.githubstudy.mvp.model.FilePath;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/8/14 16:02:28
 */

public interface IRepoFilesContract {

    interface View extends IBaseContract.View, IBasePagerContract.View, IBaseListContract.View{
        void showFiles(ArrayList<FileModel> files);
        void showFilePath(ArrayList<FilePath> filePath);
    }

    interface Presenter extends IBasePagerContract.Presenter<IRepoFilesContract.View>{
        void loadFiles(boolean isReload);
        void loadFiles(@NonNull String path, boolean isReload);
        boolean goBack();
        void goHome();
    }

}
