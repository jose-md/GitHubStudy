package com.pepe.githubstudy.mvp.presenter;


import com.pepe.githubstudy.dao.DaoSession;
import com.pepe.githubstudy.dao.LocalRepo;
import com.pepe.githubstudy.dao.LocalUser;
import com.pepe.githubstudy.dao.Trace;
import com.pepe.githubstudy.dao.TraceDao;
import com.pepe.githubstudy.mvp.contract.ITraceContract;
import com.pepe.githubstudy.mvp.model.Repository;
import com.pepe.githubstudy.mvp.model.TraceExt;
import com.pepe.githubstudy.mvp.model.User;
import com.pepe.githubstudy.mvp.presenter.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by ThirtyDegreesRay on 2017/11/23 10:55:30
 */

public class TracePresenter extends BasePresenter<ITraceContract.View>
        implements ITraceContract.Presenter {

    private ArrayList<TraceExt> traceList;
    private TraceExt removedTrace;
    private int removedPosition;

    @Inject
    public TracePresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        loadTraceList(1);
    }

    @Override
    public void loadTraceList(final int page) {
        mView.showLoading();
        final ArrayList<TraceExt> tempTraceList = new ArrayList<>();
        daoSession.rxTx()
                .run(new Runnable() {
                    @Override
                    public void run() {
                        List<Trace> traceListDb = daoSession.getTraceDao().queryBuilder()
                                .orderDesc(TraceDao.Properties.LatestTime)
                                .offset((page - 1) * 30)
                                .limit(page * 30)
                                .list();
                        for (Trace trace : traceListDb) {
                            TraceExt ext = TraceExt.generate(trace);
                            if ("user".equals(trace.getType())) {
                                LocalUser localUser = daoSession.getLocalUserDao().load(ext.getUserId());
                                ext.setUser(User.generateFromLocalUser(localUser));
                            } else {
                                LocalRepo localRepo = daoSession.getLocalRepoDao().load(ext.getRepoId());
                                ext.setRepository(Repository.generateFromLocalRepo(localRepo));
                            }
                            tempTraceList.add(ext);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (traceList == null || page == 1) {
                            traceList = tempTraceList;
                        } else {
                            traceList.addAll(tempTraceList);
                        }
                        mView.showTraceList(traceList);
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void removeTrace(int position) {
        removedTrace = traceList.remove(position);
        removedPosition = position;
        rxDBExecute(new Runnable() {
            @Override
            public void run() {
                daoSession.getTraceDao().deleteByKey(removedTrace.getId());
            }
        });
    }

    @Override
    public void undoRemoveTrace() {
        traceList.add(removedPosition, removedTrace);
        mView.notifyItemAdded(removedPosition);
        rxDBExecute(new Runnable() {
            @Override
            public void run() {
                daoSession.getTraceDao().insert(removedTrace);
            }
        });
    }

    @Override
    public int getFirstItemByDate(long dateTime) {
        for (TraceExt trace : traceList) {
            if (trace.getLatestDate().getTime() == dateTime) {
                return traceList.indexOf(trace);
            }
        }
        return 0;
    }

}
