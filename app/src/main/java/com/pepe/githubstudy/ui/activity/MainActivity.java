package com.pepe.githubstudy.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.pepe.githubstudy.R;
import com.pepe.githubstudy.bean.UserInfo;
import com.pepe.githubstudy.http.HttpCallBack;
import com.pepe.githubstudy.mvp.contract.IMainContract;
import com.pepe.githubstudy.mvp.presenter.MainPresenter;
import com.pepe.githubstudy.ui.activity.base.BaseActivity;
import com.pepe.githubstudy.ui.fragment.ActivityFragment;
import com.pepe.githubstudy.ui.fragment.BookmarksFragment;
import com.pepe.githubstudy.ui.fragment.CollectionsFragment;
import com.pepe.githubstudy.ui.fragment.RepositoriesFragment;
import com.pepe.githubstudy.ui.fragment.TopicsFragment;
import com.pepe.githubstudy.ui.fragment.TraceFragment;
import com.pepe.githubstudy.utils.LogUtil;
import com.pepe.githubstudy.utils.ViewUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class MainActivity extends BaseActivity<MainPresenter> implements IMainContract.View {

    public static final String USER_NAME = "494778200pepe";
    private Context mContext = MainActivity.this;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.frame_layout_content)
    FrameLayout frameLayoutContent;
    @BindView(R.id.nav_view_start)
    @Nullable
    protected NavigationView navViewStart;
    @BindView(R.id.nav_view_end)
    @Nullable
    protected NavigationView navViewEnd;
    @BindView(R.id.drawer_layout)
    @Nullable
    protected DrawerLayout drawerLayout;

    private AppCompatImageView toggleAccountBn;

    private final Map<Integer, String> TAG_MAP = new HashMap<>();

    private final List<Integer> FRAGMENT_NAV_ID_LIST = Arrays.asList(
            R.id.nav_news, R.id.nav_owned, R.id.nav_starred, R.id.nav_bookmarks,
            R.id.nav_trace, R.id.nav_public_news, R.id.nav_collections, R.id.nav_topics
    );

    private final List<String> FRAGMENT_TAG_LIST = Arrays.asList(
            ActivityFragment.ActivityType.News.name(),
            RepositoriesFragment.RepositoriesType.OWNED.name(),
            RepositoriesFragment.RepositoriesType.STARRED.name(),
            BookmarksFragment.class.getSimpleName(),
            TraceFragment.class.getSimpleName(),
            ActivityFragment.ActivityType.PublicNews.name(),
            CollectionsFragment.class.getSimpleName(),
            TopicsFragment.class.getSimpleName()
    );

    private final List<Integer> FRAGMENT_TITLE_LIST = Arrays.asList(
            R.string.news, R.string.my_repos, R.string.starred_repos, R.string.bookmarks,
            R.string.trace, R.string.public_news, R.string.repo_collections, R.string.topics
    );

    {
        for (int i = 0; i < FRAGMENT_NAV_ID_LIST.size(); i++) {
            TAG_MAP.put(FRAGMENT_NAV_ID_LIST.get(i), FRAGMENT_TAG_LIST.get(i));
        }
    }

    int selectedPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setPresenter() {
        mPresenter = new MainPresenter();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    ImageView avatar;
    TextView name;
    TextView mail;

    @Override
    protected void initActivity() {
        super.initActivity();
        navViewStart.setItemIconTintList(null);
        selectedPage = R.id.nav_news;
        navViewStart.setCheckedItem(selectedPage);

        avatar = navViewStart.getHeaderView(0).findViewById(R.id.avatar);
        name = navViewStart.getHeaderView(0).findViewById(R.id.name);
        mail = navViewStart.getHeaderView(0).findViewById(R.id.mail);

        toggleAccountBn = navViewStart.getHeaderView(0).findViewById(R.id.toggle_account_bn);
        toggleAccountBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAccountLay();
            }
        });
        mPresenter.getUserInfo(USER_NAME);
        initStartDrawerView();
    }

    @Override
    public void getUserInfo(UserInfo userInfo) {
        String avatar_url = userInfo.getAvatar_url();
        LogUtil.d("avatar_url = " + avatar_url);
        LogUtil.d("thread name = " + Thread.currentThread().getName());
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(6);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
        Glide.with(mContext).load(avatar_url).into(avatar);
        name.setText(USER_NAME);
        String joinTime = getString(R.string.joined_at).concat(" ")
                .concat(userInfo.getCreated_at());
        LogUtil.d("joinTime = " + joinTime);
        mail.setText(joinTime);
    }

    private boolean isManageAccount = false;

    private void toggleAccountLay() {
        isManageAccount = !isManageAccount;
        toggleAccountBn.setImageResource(isManageAccount ? R.drawable.ic_arrow_drop_up : R.drawable.ic_arrow_drop_down);
        invalidateMainMenu();
    }

    private void invalidateMainMenu() {
        if (navViewStart == null) {
            return;
        }
        Menu menu = navViewStart.getMenu();

        menu.setGroupVisible(R.id.my_account, isManageAccount);
        menu.setGroupVisible(R.id.manage_accounts, isManageAccount);

        menu.setGroupVisible(R.id.my, !isManageAccount);
        menu.setGroupVisible(R.id.repositories, !isManageAccount);
        menu.setGroupVisible(R.id.search, !isManageAccount);
        menu.setGroupVisible(R.id.setting, !isManageAccount);
    }

    private void initStartDrawerView() {
        if (navViewStart == null) {
            return;
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        navViewStart.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return MainActivity.this.onNavigationItemSelected(item, true);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LogUtil.d("onOptionsItemSelected  item.getItemId() = " + item.getItemId());
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        if (drawerLayout != null && item.getItemId() == getEndDrawerToggleMenuItemId()) {
            openDrawer(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean onNavigationItemSelected(@NonNull final MenuItem item, final boolean isStartDrawer) {
        NavigationView navView = navViewStart;
        boolean isMultiSelect = false;
        if (navView == null) {
            return true;
        }
        if (isMultiSelect) {
            ViewUtils.selectMenuItem(navView.getMenu(), item.getItemId(), true);
        }
        closeDrawer();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onNavItemSelected(item, isStartDrawer);
            }
        }, 250);
        return !isMultiSelect;
    }

    protected void onNavItemSelected(@NonNull MenuItem item, boolean isStartDrawer) {
        int id = item.getItemId();
        updateFragmentByNavId(id);
    }

    protected final void openDrawer(boolean isStartDrawer) {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(isStartDrawer ? GravityCompat.START : GravityCompat.END);
        }
    }


    protected final void closeDrawer() {
        if (drawerLayout != null) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            }
        }
    }

    private final int SETTINGS_REQUEST_CODE = 100;

    private void updateFragmentByNavId(int id) {
        if (FRAGMENT_NAV_ID_LIST.contains(id)) {
            updateTitle(id);
            loadFragment(id);
            updateFilter(id);
            return;
        }
        switch (id) {
            case R.id.nav_profile:
//                ProfileActivity.show(getActivity(), AppData.INSTANCE.getLoggedUser().getLogin(),
//                        AppData.INSTANCE.getLoggedUser().getAvatarUrl());
                break;
            case R.id.nav_issues:
//                IssuesActivity.showForUser(getActivity());
                break;
            case R.id.nav_notifications:
//                NotificationsActivity.show(getActivity());
                break;
            case R.id.nav_trending:
//                TrendingActivity.show(getActivity());
                break;
            case R.id.nav_search:
                SearchActivity.show(getActivity());
                break;
            case R.id.nav_settings:
                SettingsActivity.show(getActivity(), SETTINGS_REQUEST_CODE);
                break;
            case R.id.nav_about:
                AboutActivity.show(getActivity());
                break;
            case R.id.nav_logout:
//                logout();
                break;
            case R.id.nav_add_account:
                showLoginPage();
                break;
            default:
                break;
        }
    }

    private void updateTitle(int itemId) {
        int titleId = FRAGMENT_TITLE_LIST.get(FRAGMENT_NAV_ID_LIST.indexOf(itemId));
        setToolbarTitle(getString(titleId));
    }

    private void loadFragment(int itemId) {
        selectedPage = itemId;
        String fragmentTag = TAG_MAP.get(itemId);
        Fragment showFragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        boolean isExist = true;
        if (showFragment == null) {
            isExist = false;
            showFragment = getFragment(itemId);
        }
        if (showFragment.isVisible()) {
            return;
        }

        Fragment visibleFragment = getVisibleFragment();
        if (isExist) {
            showAndHideFragment(showFragment, visibleFragment);
        } else {
            addAndHideFragment(showFragment, visibleFragment, fragmentTag);
        }
    }

    private void updateFilter(int itemId) {
        if (itemId == R.id.nav_owned) {
            updateEndDrawerContent(R.menu.menu_repositories_filter);
//            RepositoriesFilter.initDrawer(navViewEnd, RepositoriesFragment.RepositoriesType.OWNED);
        } else if (itemId == R.id.nav_starred) {
            updateEndDrawerContent(R.menu.menu_repositories_filter);
//            RepositoriesFilter.initDrawer(navViewEnd, RepositoriesFragment.RepositoriesType.STARRED);
        } else {
            removeEndDrawer();
        }
        invalidateOptionsMenu();
    }

    @NonNull
    private Fragment getFragment(int itemId) {
        switch (itemId) {
//            case R.id.nav_news:
//                return ActivityFragment.create(ActivityFragment.ActivityType.News,
//                        AppData.INSTANCE.getLoggedUser().getLogin());
//            case R.id.nav_public_news:
//                return ActivityFragment.create(ActivityFragment.ActivityType.PublicNews,
//                        AppData.INSTANCE.getLoggedUser().getLogin());
//            case R.id.nav_owned:
//                return RepositoriesFragment.create(RepositoriesFragment.RepositoriesType.OWNED,
//                        AppData.INSTANCE.getLoggedUser().getLogin());
//            case R.id.nav_starred:
//                return RepositoriesFragment.create(RepositoriesFragment.RepositoriesType.STARRED,
//                        AppData.INSTANCE.getLoggedUser().getLogin());
//            case R.id.nav_bookmarks:
//                return BookmarksFragment.create();
//            case R.id.nav_trace:
//                return TraceFragment.create();
//            case R.id.nav_collections:
//                return CollectionsFragment.create();
//            case R.id.nav_topics:
//                return TopicsFragment.create();
        }
        return null;
    }

    private void showAndHideFragment(@NonNull Fragment showFragment, @Nullable Fragment hideFragment) {
        if (hideFragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .show(showFragment)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .show(showFragment)
                    .hide(hideFragment)
                    .commit();
        }

    }

    private void addAndHideFragment(@NonNull Fragment showFragment,
                                    @Nullable Fragment hideFragment, @NonNull String addTag) {
        if (hideFragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frame_layout_content, showFragment, addTag)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frame_layout_content, showFragment, addTag)
                    .hide(hideFragment)
                    .commit();
        }
    }

    protected void removeStartDrawer() {
        removeDrawer(navViewStart);
    }

    protected void removeEndDrawer() {
        removeDrawer(navViewEnd);
    }

    protected void updateStartDrawerContent(int menuId) {
        updateDrawerContent(navViewStart, menuId);
    }

    protected void updateEndDrawerContent(int menuId) {
        updateDrawerContent(navViewEnd, menuId);
    }

    private void removeDrawer(NavigationView navView) {
        if (drawerLayout != null && navView != null) {
            drawerLayout.removeView(navView);
        }
    }

    private void updateDrawerContent(NavigationView navView, int menuId) {
        if (drawerLayout != null && navView != null) {
            navView.getMenu().clear();
            navView.inflateMenu(menuId);
            if (drawerLayout.indexOfChild(navView) == -1) {
                drawerLayout.addView(navView);
            }
        }
    }

    @Override
    public void restartApp() {
        getActivity().finishAffinity();
        Intent intent = new Intent(getActivity(), SplashActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort, menu);
        MenuItem menuItem = menu.findItem(R.id.nav_sort);
        menuItem.setVisible(selectedPage == R.id.nav_owned || selectedPage == R.id.nav_starred);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        invalidateMainMenu();
        return super.onPrepareOptionsMenu(menu);
    }


    protected boolean isEndDrawerMultiSelect() {
        return false;
    }

    protected int getEndDrawerToggleMenuItemId() {
        return -1;
    }
}
