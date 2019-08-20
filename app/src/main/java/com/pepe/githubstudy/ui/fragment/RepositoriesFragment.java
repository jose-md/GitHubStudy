package com.pepe.githubstudy.ui.fragment;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.pepe.githubstudy.mvp.model.SearchModel;
import com.pepe.githubstudy.utils.BundleHelper;

/**
 * @author 1one
 * @date 2019/8/8.
 */
public class RepositoriesFragment extends Fragment {

    public enum RepositoriesType{
        OWNED, PUBLIC, STARRED, TRENDING, SEARCH, FORKS, TRACE, BOOKMARK, COLLECTION, TOPIC
    }

    public static RepositoriesFragment create(@NonNull RepositoriesType type,
                                              @NonNull String user){
        RepositoriesFragment fragment = new RepositoriesFragment();
        fragment.setArguments(BundleHelper.builder().put("type", type)
                .put("user", user).build());
        return fragment;
    }

    public static RepositoriesFragment createForSearch(@NonNull SearchModel searchModel){
        RepositoriesFragment fragment = new RepositoriesFragment();
        fragment.setArguments(
                BundleHelper.builder()
                        .put("type", RepositoriesType.SEARCH)
                        .put("searchModel", searchModel)
                        .build()
        );
        return fragment;
    }
}
