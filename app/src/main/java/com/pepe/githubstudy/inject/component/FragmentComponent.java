

package com.pepe.githubstudy.inject.component;


import com.pepe.githubstudy.inject.FragmentScope;
import com.pepe.githubstudy.inject.module.FragmentModule;
import com.pepe.githubstudy.ui.fragment.ActivityFragment;
import com.pepe.githubstudy.ui.fragment.BookmarksFragment;
import com.pepe.githubstudy.ui.fragment.CollectionsFragment;
import com.pepe.githubstudy.ui.fragment.CommitFilesFragment;
import com.pepe.githubstudy.ui.fragment.CommitsFragment;
import com.pepe.githubstudy.ui.fragment.IssueTimelineFragment;
import com.pepe.githubstudy.ui.fragment.IssuesFragment;
import com.pepe.githubstudy.ui.fragment.LabelManageFragment;
import com.pepe.githubstudy.ui.fragment.NotificationsFragment;
import com.pepe.githubstudy.ui.fragment.ProfileInfoFragment;
import com.pepe.githubstudy.ui.fragment.ReleasesFragment;
import com.pepe.githubstudy.ui.fragment.RepoFilesFragment;
import com.pepe.githubstudy.ui.fragment.RepoInfoFragment;
import com.pepe.githubstudy.ui.fragment.RepositoriesFragment;
import com.pepe.githubstudy.ui.fragment.TopicsFragment;
import com.pepe.githubstudy.ui.fragment.TraceFragment;
import com.pepe.githubstudy.ui.fragment.UserListFragment;
import com.pepe.githubstudy.ui.fragment.ViewerFragment;

import dagger.Component;

@FragmentScope
@Component(modules = FragmentModule.class, dependencies = AppComponent.class)
public interface FragmentComponent {
    void inject(RepositoriesFragment fragment);
    void inject(RepoInfoFragment fragment);
    void inject(RepoFilesFragment fragment);
    void inject(UserListFragment fragment);
    void inject(ViewerFragment fragment);
    void inject(ProfileInfoFragment fragment);
    void inject(ActivityFragment fragment);
    void inject(ReleasesFragment fragment);
    void inject(IssuesFragment fragment);
    void inject(IssueTimelineFragment fragment);
    void inject(CommitsFragment fragment);
    void inject(CommitFilesFragment fragment);
    void inject(NotificationsFragment fragment);
    void inject(BookmarksFragment fragment);
    void inject(TraceFragment fragment);
//    void inject(LanguagesEditorFragment fragment);
//    void inject(WikiFragment fragment);
    void inject(CollectionsFragment fragment);
    void inject(TopicsFragment fragment);
    void inject(LabelManageFragment fragment);
}
