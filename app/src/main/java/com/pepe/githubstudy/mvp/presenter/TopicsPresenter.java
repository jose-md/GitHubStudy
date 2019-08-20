package com.pepe.githubstudy.mvp.presenter;


import com.pepe.githubstudy.R;
import com.pepe.githubstudy.dao.DaoSession;
import com.pepe.githubstudy.http.GitHubWebPageService;
import com.pepe.githubstudy.http.HttpConfig;
import com.pepe.githubstudy.http.convert.ITopicsContract;
import com.pepe.githubstudy.http.core.HttpObserver;
import com.pepe.githubstudy.http.core.HttpResponse;
import com.pepe.githubstudy.mvp.model.Topic;
import com.pepe.githubstudy.mvp.presenter.base.BasePresenter;
import com.pepe.githubstudy.utils.LogUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by ThirtyDegreesRay on 2017/12/29 11:10:27
 */

public class TopicsPresenter extends BasePresenter<ITopicsContract.View>
        implements ITopicsContract.Presenter {

    private ArrayList<Topic> topics;

    @Inject
    public TopicsPresenter(DaoSession daoSession) {
        super(daoSession);
    }


    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        LogUtil.d("===> onViewInitialized" );
        loadTopics(false);
    }

    @Override
    public void loadTopics(boolean isReload) {
        LogUtil.d("===> loadTopics isReload = " + isReload);
        HttpObserver<ResponseBody> httpObserver = new HttpObserver<ResponseBody>() {
            @Override
            public void onError(Throwable error) {
                LogUtil.d("===> loadTopics onError");
                mView.hideLoading();
                mView.showLoadError(getErrorTip(error));
            }

            @Override
            public void onSuccess(HttpResponse<ResponseBody> response) {
                LogUtil.d("===> loadTopics onSuccess");
                try {
                    parsePageData(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        generalRxHttpExecute(new IObservableCreator() {
            @Override
            public Observable<Response<ResponseBody>> createObservable(boolean forceNetWork) {
                return getGitHubWebPageService().getTopics(forceNetWork);
            }
        }, httpObserver, !isReload,mView.getProgressDialog(getLoadTip()));
    }

    private void parsePageData(String page) {
        Observable.just(page)
                .map(new Function<String, ArrayList<Topic>>() {
                    @Override
                    public ArrayList<Topic> apply(String s) throws Exception {
                        ArrayList<Topic> topics = new ArrayList<>();
                        try {
                            Document doc = Jsoup.parse(s, HttpConfig.GITHUB_BASE_URL);
                            //top three topics
                            topics.addAll(getTopTopics(doc));
                            topics.addAll(getFeaturedTopics(doc));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return topics;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<Topic>>() {
                    @Override
                    public void accept(ArrayList<Topic> results) throws Exception {
                        if(mView == null) {
                            return;
                        }
                        if(results.size() != 0){
                            topics = results;
                            mView.hideLoading();
                            mView.showTopics(topics);
                        } else {
                            String errorTip = String.format(getString(R.string.github_page_parse_error),
                                    getString(R.string.topics));
                            mView.showLoadError(errorTip);
                            mView.hideLoading();
                        }
                    }
                });
    }

    private ArrayList<Topic> getTopTopics(Document doc) throws Exception {
        ArrayList<Topic> topTopics = new ArrayList<>();
        Elements elements = doc.getElementsByClass("col-12 col-sm-6 col-md-4 mb-4");
        for (Element element : elements) {
            Element idElement = element.select("a").first();
            Element imageElement = element.select("a > img").first();
            Element titleElement = element.select("a > p").get(0);
            Element descElement = element.select("a > p").get(1);

            String id = idElement.attr("href");
            id = id.substring(id.lastIndexOf("/") + 1);
            String name = titleElement.textNodes().get(0).text();
            String desc = descElement.textNodes().get(0).text();
            String image = imageElement == null ? null : imageElement.attr("src");

            Topic topic = new Topic()
                    .setId(id)
                    .setName(name)
                    .setDesc(desc)
                    .setImage(image);
            topTopics.add(topic);
        }
        return topTopics;
    }

    private ArrayList<Topic> getFeaturedTopics(Document doc) throws Exception {
        ArrayList<Topic> topTopics = new ArrayList<>();
        Elements topElements = doc.getElementsByClass("py-4 border-bottom");
        for (Element element : topElements) {
            Element idElement = element.select("a").first();
            Element imageElement = element.select("a > img").first();
            Element titleElement = element.select("a > div > div > p").get(0);
            Element descElement = element.select("a > div > div > p").get(1);

            String id = idElement.attr("href");
            id = id.substring(id.lastIndexOf("/") + 1);
            String name = titleElement.textNodes().get(0).text();
            String desc = descElement.textNodes().get(0).text();
            String image = imageElement == null ? null : imageElement.attr("src");

            Topic topic = new Topic()
                    .setId(id)
                    .setName(name)
                    .setDesc(desc)
                    .setImage(image);
            topTopics.add(topic);
        }
        return topTopics;
    }
}
