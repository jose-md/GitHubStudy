package com.pepe.githubstudy.mvp.presenter;

import com.pepe.githubstudy.dao.Bookmark;
import com.pepe.githubstudy.dao.BookmarkDao;
import com.pepe.githubstudy.dao.DaoSession;
import com.pepe.githubstudy.dao.LocalRepo;
import com.pepe.githubstudy.dao.LocalUser;
import com.pepe.githubstudy.mvp.contract.IBookmarkContract;
import com.pepe.githubstudy.mvp.model.BookmarkExt;
import com.pepe.githubstudy.mvp.model.Repository;
import com.pepe.githubstudy.mvp.model.User;
import com.pepe.githubstudy.mvp.presenter.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * Created by ThirtyDegreesRay on 2017/11/22 15:59:45
 */

public class BookmarkPresenter extends BasePresenter<IBookmarkContract.View>
        implements IBookmarkContract.Presenter {

    private ArrayList<BookmarkExt> bookmarks;
    private BookmarkExt removedBookmark;
    private int removedPosition;

    @Inject
    public BookmarkPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        loadBookmarks(1);
    }

    @Override
    public void loadBookmarks(final int page) {
        mView.showLoading();
        final ArrayList<BookmarkExt> tempBookmarks = new ArrayList<>();
//        daoSession.rxTx()
//                .run(new Runnable() {
//                    @Override
//                    public void run() {
//                        List<Bookmark> bookmarkList = daoSession.getBookmarkDao().queryBuilder()
//                                .orderDesc(BookmarkDao.Properties.MarkTime)
//                                .offset((page - 1) * 30)
//                                .limit(page * 30)
//                                .list();
//                        for(Bookmark bookmark : bookmarkList){
//                            BookmarkExt ext = BookmarkExt.generate(bookmark);
//                            if("user".equals(bookmark.getType())){
//                                LocalUser localUser = daoSession.getLocalUserDao().load(ext.getUserId());
//                                ext.setUser(User.generateFromLocalUser(localUser));
//                            }else{
//                                LocalRepo localRepo = daoSession.getLocalRepoDao().load(ext.getRepoId());
//                                ext.setRepository(Repository.generateFromLocalRepo(localRepo));
//                            }
//                            tempBookmarks.add(ext);
//                        }
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(aVoid -> {
//                    if(bookmarks == null || page == 1){
//                        bookmarks = tempBookmarks;
//                    } else {
//                        bookmarks.addAll(tempBookmarks);
//                    }
//                    mView.showBookmarks(bookmarks);
//                    mView.hideLoading();
//                });

    }

    @Override
    public void removeBookmark(int position) {
        removedBookmark = bookmarks.remove(position);
        removedPosition = position;
        rxDBExecute(new Runnable() {
            @Override
            public void run() {
                daoSession.getBookmarkDao().deleteByKey(removedBookmark.getId());
            }
        });
    }

    @Override
    public void undoRemoveBookmark() {
        bookmarks.add(removedPosition, removedBookmark);
        mView.notifyItemAdded(removedPosition);
        rxDBExecute(new Runnable() {
            @Override
            public void run() {
                daoSession.getBookmarkDao().insert(removedBookmark);
            }
        });
    }

}
