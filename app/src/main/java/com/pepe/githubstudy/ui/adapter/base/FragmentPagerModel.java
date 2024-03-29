

package com.pepe.githubstudy.ui.adapter.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;


import com.pepe.githubstudy.R;
import com.pepe.githubstudy.mvp.model.Issue;
import com.pepe.githubstudy.mvp.model.Repository;
import com.pepe.githubstudy.mvp.model.SearchModel;
import com.pepe.githubstudy.mvp.model.User;
import com.pepe.githubstudy.mvp.model.filter.TrendingSince;
import com.pepe.githubstudy.ui.fragment.ActivityFragment;
import com.pepe.githubstudy.ui.fragment.CommitsFragment;
import com.pepe.githubstudy.ui.fragment.IssuesFragment;
import com.pepe.githubstudy.ui.fragment.MarkdownEditorFragment;
import com.pepe.githubstudy.ui.fragment.MarkdownPreviewFragment;
import com.pepe.githubstudy.ui.fragment.NotificationsFragment;
import com.pepe.githubstudy.ui.fragment.ProfileInfoFragment;
import com.pepe.githubstudy.ui.fragment.RepoFilesFragment;
import com.pepe.githubstudy.ui.fragment.RepoInfoFragment;
import com.pepe.githubstudy.ui.fragment.RepositoriesFragment;
import com.pepe.githubstudy.ui.fragment.UserListFragment;
import com.pepe.githubstudy.ui.fragment.base.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ThirtyDegreesRay on 2017/8/15 21:10:14
 */

public class FragmentPagerModel {

    private String title;
    private BaseFragment fragment;

    public FragmentPagerModel(String title, BaseFragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public BaseFragment getFragment() {
        return fragment;
    }

    public static List<FragmentPagerModel> createRepoPagerList(@NonNull Context context
            , @NonNull final Repository repository, @NonNull ArrayList<Fragment> fragments) {

        return setPagerFragmentFlag(Arrays.asList(
                new FragmentPagerModel(context.getString(R.string.info),
                        getFragment(fragments, 0, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return RepoInfoFragment.create(repository);
                            }
                        })),
                new FragmentPagerModel(context.getString(R.string.files),
                        getFragment(fragments, 1, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return RepoFilesFragment.create(repository);
                            }
                        })),
                new FragmentPagerModel(context.getString(R.string.commits),
                        getFragment(fragments, 2,
                                new FragmentCreator() {
                                    @Override
                                    public Fragment createFragment() {
                                        return CommitsFragment.createForRepo(repository.getOwner().getLogin(),
                                                repository.getName(), repository.getDefaultBranch());
                                    }
                                })),
                new FragmentPagerModel(context.getString(R.string.activity),
                        getFragment(fragments, 3,
                                new FragmentCreator() {
                                    @Override
                                    public Fragment createFragment() {
                                        return ActivityFragment.create(ActivityFragment.ActivityType.Repository,
                                                repository.getOwner().getLogin(), repository.getName());
                                    }
                                }))
        ));
    }

    public static List<FragmentPagerModel> createProfilePagerList(Context context, final User user
            , @NonNull ArrayList<Fragment> fragments) {
        List<FragmentPagerModel> list = new ArrayList<>();
        list.add(new FragmentPagerModel(context.getString(R.string.info),
                getFragment(fragments, 0, new FragmentCreator() {
                    @Override
                    public Fragment createFragment() {
                        return ProfileInfoFragment.create(user);
                    }
                })));
        list.add(new FragmentPagerModel(context.getString(R.string.activity),
                getFragment(fragments, 1, new FragmentCreator() {
                    @Override
                    public Fragment createFragment() {
                        return ActivityFragment.create(ActivityFragment.ActivityType.User, user.getLogin(), null);
                    }
                })));
        if (user.isUser()) {
            list.add(new FragmentPagerModel(context.getString(R.string.starred),
                    getFragment(fragments, 2, new FragmentCreator() {
                        @Override
                        public Fragment createFragment() {
                            return RepositoriesFragment.create(RepositoriesFragment.RepositoriesType.STARRED, user.getLogin());
                        }
                    })));
        }
        return setPagerFragmentFlag(list);
    }

    public static List<FragmentPagerModel> createSearchPagerList(@NonNull Context context
            , @NonNull final ArrayList<SearchModel> searchModels, @NonNull ArrayList<Fragment> fragments) {
        return setPagerFragmentFlag(Arrays.asList(
                new FragmentPagerModel(context.getString(R.string.repositories),
                        getFragment(fragments, 0, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return RepositoriesFragment.createForSearch(searchModels.get(0));
                            }
                        })),
                new FragmentPagerModel(context.getString(R.string.users),
                        getFragment(fragments, 1, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return  UserListFragment.createForSearch(searchModels.get(1));
                            }
                        }))
        ));
    }

    public static List<FragmentPagerModel> createTrendingPagerList(
            @NonNull Context context, @NonNull ArrayList<Fragment> fragments) {
        return setPagerFragmentFlag(Arrays.asList(
                new FragmentPagerModel(context.getString(R.string.daily),
                        getFragment(fragments, 0, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return RepositoriesFragment.createForTrending(TrendingSince.Daily);
                            }
                        })),
                new FragmentPagerModel(context.getString(R.string.weekly),
                        getFragment(fragments, 1, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return RepositoriesFragment.createForTrending(TrendingSince.Weekly);
                            }
                        })),
                new FragmentPagerModel(context.getString(R.string.monthly),
                        getFragment(fragments, 2, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return RepositoriesFragment.createForTrending(TrendingSince.Monthly);
                            }
                        }))
        ));
    }

    public static List<FragmentPagerModel> createRepoIssuesPagerList(@NonNull Context context
            , @NonNull final String userId, @NonNull final String repoName, @NonNull ArrayList<Fragment> fragments) {
        return setPagerFragmentFlag(Arrays.asList(
                new FragmentPagerModel(context.getString(R.string.open),
                        getFragment(fragments, 0, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return IssuesFragment.createForRepo(Issue.IssueState.open, userId, repoName);
                            }
                        })),
                new FragmentPagerModel(context.getString(R.string.closed),
                        getFragment(fragments, 1, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return IssuesFragment.createForRepo(Issue.IssueState.closed, userId, repoName);
                            }
                        }))
        ));
    }

    public static List<FragmentPagerModel> createUserIssuesPagerList(@NonNull Context context
            , @NonNull ArrayList<Fragment> fragments) {
        return setPagerFragmentFlag(Arrays.asList(
                new FragmentPagerModel(context.getString(R.string.open),
                        getFragment(fragments, 0, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return IssuesFragment.createForUser(Issue.IssueState.open);
                            }
                        })),
                new FragmentPagerModel(context.getString(R.string.closed),
                        getFragment(fragments, 1, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return IssuesFragment.createForUser(Issue.IssueState.closed);
                            }
                        }))
        ));
    }

    public static List<FragmentPagerModel> createMarkdownEditorPagerList(@NonNull Context context
            , final String text, @NonNull ArrayList<Fragment> fragments, final ArrayList<String> mentionUsers) {
        return setPagerFragmentFlag(Arrays.asList(
                new FragmentPagerModel(context.getString(R.string.write),
                        getFragment(fragments, 0, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return MarkdownEditorFragment.create(text, mentionUsers);
                            }
                        })),
                new FragmentPagerModel(context.getString(R.string.preview),
                        getFragment(fragments, 1, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return MarkdownPreviewFragment.create();
                            }
                        }))
        ));
    }

    public static List<FragmentPagerModel> createNotificationsPagerList(
            @NonNull Context context, @NonNull ArrayList<Fragment> fragments) {
        return setPagerFragmentFlag(Arrays.asList(
                new FragmentPagerModel(context.getString(R.string.unread),
                        getFragment(fragments, 0, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return NotificationsFragment.create(NotificationsFragment.NotificationsType.Unread);
                            }
                        })),
                new FragmentPagerModel(context.getString(R.string.participating),
                        getFragment(fragments, 1, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return NotificationsFragment.create(NotificationsFragment.NotificationsType.Participating);
                            }
                        })),
                new FragmentPagerModel(context.getString(R.string.all),
                        getFragment(fragments, 2, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return NotificationsFragment.create(NotificationsFragment.NotificationsType.All);
                            }
                        }))
        ));
    }

//    public static List<FragmentPagerModel> createTracePagerList(
//            @NonNull Context context, @NonNull ArrayList<Fragment> fragments) {
//        return setPagerFragmentFlag(Arrays.asList(
//                new FragmentPagerModel(context.getString(R.string.repositories),
//                        getFragment(fragments, 0, () -> RepositoriesFragment.createForTrace())),
//                new FragmentPagerModel(context.getString(R.string.users),
//                        getFragment(fragments, 1, () -> UserListFragment.createForTrace()))
//        ));
//    }

//    public static List<FragmentPagerModel> createBookmarksPagerList(
//            @NonNull Context context, @NonNull ArrayList<Fragment> fragments) {
//        return setPagerFragmentFlag(Arrays.asList(
//                new FragmentPagerModel(context.getString(R.string.repositories),
//                        getFragment(fragments, 0, () -> RepositoriesFragment.createForBookmark())),
//                new FragmentPagerModel(context.getString(R.string.users),
//                        getFragment(fragments, 1, () -> UserListFragment.createForBookmark()))
//        ));
//    }

    private static List<FragmentPagerModel> setPagerFragmentFlag(List<FragmentPagerModel> list) {
        for (FragmentPagerModel model : list) {
            model.getFragment().setPagerFragment(true);
        }
        return list;
    }

    private static BaseFragment getFragment(ArrayList<Fragment> fragments
            , int position, FragmentCreator fragmentCreator) {
        Fragment fragment = fragments.get(position);
        if (fragment == null) {
            fragment = fragmentCreator.createFragment();
//            Logger.d("create fragment " + fragment + (Math.random() * 1000 * 1000));
        } else {
//            Logger.d("reuse fragment" + fragment + (Math.random() * 1000 * 1000));
        }
        return (BaseFragment) fragment;
    }

    interface FragmentCreator<F extends Fragment> {
        F createFragment();
    }

}
