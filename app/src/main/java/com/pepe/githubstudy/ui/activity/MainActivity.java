package com.pepe.githubstudy.ui.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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
import com.pepe.githubstudy.mvp.presenter.MainPresenter;
import com.pepe.githubstudy.ui.activity.base.BaseActivity;
import com.pepe.githubstudy.ui.fragment.ActivityFragment;
import com.pepe.githubstudy.ui.fragment.BookmarksFragment;
import com.pepe.githubstudy.ui.fragment.CollectionsFragment;
import com.pepe.githubstudy.ui.fragment.RepositoriesFragment;
import com.pepe.githubstudy.ui.fragment.TopicsFragment;
import com.pepe.githubstudy.ui.fragment.TraceFragment;
import com.pepe.githubstudy.utils.LogUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

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
        MainPresenter presenter = new MainPresenter();
        presenter.setView(this);
        mPresenter = presenter;
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
        ((MainPresenter) mPresenter).getUserInfo(USER_NAME, new HttpCallBack<UserInfo>() {
            @Override
            public void onFailure(Exception e) {
                LogUtil.d("error : " + e.getMessage());
            }

            @Override
            protected void onSuccess(UserInfo userInfo) {
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
                mail.setText(joinTime );
            }
        });
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

}
