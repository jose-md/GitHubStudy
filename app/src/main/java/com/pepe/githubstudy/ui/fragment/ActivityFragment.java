package com.pepe.githubstudy.ui.fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.pepe.githubstudy.utils.BundleHelper;

/**
 * @author 1one
 * @date 2019/8/8.
 */
public class ActivityFragment extends Fragment {

    public enum ActivityType {
        News, User, Repository, PublicNews
    }

    public static ActivityFragment create(@NonNull ActivityType type, @NonNull String user) {
        return create(type, user, null);
    }

    public static ActivityFragment create(@NonNull ActivityType type, @NonNull String user,
                                          @Nullable String repo) {
        ActivityFragment fragment = new ActivityFragment();
        fragment.setArguments(BundleHelper.builder().put("type", type)
                .put("user", user).put("repo", repo).build());
        return fragment;
    }

}
