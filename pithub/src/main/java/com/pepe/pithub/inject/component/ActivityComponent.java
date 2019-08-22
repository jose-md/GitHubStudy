

package com.pepe.pithub.inject.component;



import com.pepe.pithub.inject.ActivityScope;
import com.pepe.pithub.inject.module.ActivityModule;
import com.pepe.pithub.ui.activity.MainActivity;
import com.pepe.pithub.ui.activity.SplashActivity;

import dagger.Component;

@ActivityScope
@Component(modules = ActivityModule.class, dependencies = AppComponent.class)
public interface ActivityComponent {
    void inject(SplashActivity activity);
//    void inject(LoginActivity activity);
    void inject(MainActivity activity);
//    void inject(SettingsActivity activity);
//    void inject(RepositoryActivity activity);
//    void inject(ProfileActivity activity);
//    void inject(SearchActivity activity);
//    void inject(ReleaseInfoActivity activity);
//    void inject(IssuesActivity activity);
//    void inject(IssueDetailActivity activity);
//    void inject(EditIssueActivity activity);
//    void inject(CommitDetailActivity activity);
//    void inject(TrendingActivity activity);
}
