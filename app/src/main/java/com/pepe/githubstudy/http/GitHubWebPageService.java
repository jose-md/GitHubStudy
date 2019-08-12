package com.pepe.githubstudy.http;

import android.support.annotation.NonNull;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ThirtyDegreesRay on 2017/12/25 15:30:56
 */

public interface GitHubWebPageService {



    @NonNull
    @GET("topics")
    Observable<Response<ResponseBody>> getTopics(
            @Header("forceNetWork") boolean forceNetWork
    );
}
