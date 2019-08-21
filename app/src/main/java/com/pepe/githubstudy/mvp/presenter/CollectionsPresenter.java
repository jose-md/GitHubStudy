package com.pepe.githubstudy.mvp.presenter;


import com.pepe.githubstudy.AppConfig;
import com.pepe.githubstudy.R;
import com.pepe.githubstudy.dao.DaoSession;
import com.pepe.githubstudy.http.core.HttpObserver;
import com.pepe.githubstudy.http.core.HttpResponse;
import com.pepe.githubstudy.mvp.contract.ICollectionsContract;
import com.pepe.githubstudy.mvp.model.Collection;
import com.pepe.githubstudy.mvp.presenter.base.BasePresenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Response;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ThirtyDegreesRay on 2017/12/25 15:16:34
 */

public class CollectionsPresenter extends BasePresenter<ICollectionsContract.View>
        implements ICollectionsContract.Presenter {

    private ArrayList<Collection> collections;

    @Inject
    public CollectionsPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        loadCollections(false);
    }

    @Override
    public void loadCollections(boolean isReload) {
        mView.showLoading();
        HttpObserver<ResponseBody> httpObserver = new HttpObserver<ResponseBody>() {
            @Override
            public void onError(Throwable error) {
                mView.hideLoading();
                mView.showLoadError(getErrorTip(error));
            }

            @Override
            public void onSuccess(HttpResponse<ResponseBody> response) {
                try {
                    parsePageData(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        generalRxHttpExecute(
                new IObservableCreator<ResponseBody>() {
                    @Override
                    public io.reactivex.Observable<Response<ResponseBody>> createObservable(boolean forceNetWork) {
                        return getGitHubWebPageService().getCollections(forceNetWork);
                    }
                },
                httpObserver, !isReload);

    }

    private void parsePageData(String page) {
        Observable.just(page)
                .map(new Function<String, Object>() {

                    @Override
                    public Object apply(String s) {
                        ArrayList<Collection> collections = new ArrayList<>();
                        try {
                            Document doc = Jsoup.parse(s, AppConfig.GITHUB_BASE_URL);
                            Elements elements = doc.getElementsByClass(
                                    "d-flex border-bottom border-gray-light pb-4 mb-5");
                            for (Element element : elements) {
                                Element titleElement = element.select("div > h2 > a").first();
                                Element descElement = element.select("div").last();
                                String id = titleElement.attr("href");
                                id = id.substring(id.lastIndexOf("/") + 1);
                                String title = titleElement.textNodes().get(0).toString();

                                List<TextNode> descTextNodes = descElement.textNodes();
                                int descIndex = descTextNodes.size() == 0 ? 0 : descTextNodes.size() - 1;
                                String desc = descTextNodes.get(descIndex).toString().trim();
                                Collection collection = new Collection(id, title, desc);
                                collections.add(collection);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return collections;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object results) throws Exception {
                        if (mView == null) {
                            return;
                        }
                        if (results != null) {
                            collections = (ArrayList<Collection>) results;
                            mView.hideLoading();
                            mView.showCollections(collections);
                        } else {
                            String errorTip = String.format(getString(R.string.github_page_parse_error),
                                    getString(R.string.repo_collections));
                            mView.showLoadError(errorTip);
                            mView.hideLoading();
                        }
                    }
                });
    }

}
