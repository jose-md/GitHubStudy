

package com.pepe.githubstudy.inject.component;


import com.pepe.githubstudy.inject.ActivityScope;
import com.pepe.githubstudy.inject.module.ActivityModule;
import com.pepe.githubstudy.ui.activity.CommitDetailActivity;
import com.pepe.githubstudy.ui.activity.EditIssueActivity;
import com.pepe.githubstudy.ui.activity.IssueDetailActivity;
import com.pepe.githubstudy.ui.activity.IssuesActivity;
import com.pepe.githubstudy.ui.activity.LoginActivity;
import com.pepe.githubstudy.ui.activity.MainActivity;
import com.pepe.githubstudy.ui.activity.ProfileActivity;
import com.pepe.githubstudy.ui.activity.RepositoryActivity;
import com.pepe.githubstudy.ui.activity.SearchActivity;
import com.pepe.githubstudy.ui.activity.SettingsActivity;
import com.pepe.githubstudy.ui.activity.SplashActivity;
import com.pepe.githubstudy.ui.activity.TrendingActivity;

import dagger.Component;

@ActivityScope
@Component(modules = ActivityModule.class, dependencies = AppComponent.class)
public interface ActivityComponent {
    void inject(SplashActivity activity);
    void inject(LoginActivity activity);
    void inject(MainActivity activity);
    void inject(SettingsActivity activity);
    void inject(RepositoryActivity activity);
    void inject(ProfileActivity activity);
    void inject(SearchActivity activity);
//    void inject(ReleaseInfoActivity activity);
    void inject(IssuesActivity activity);
    void inject(IssueDetailActivity activity);
    void inject(EditIssueActivity activity);
    void inject(CommitDetailActivity activity);
    void inject(TrendingActivity activity);
}
