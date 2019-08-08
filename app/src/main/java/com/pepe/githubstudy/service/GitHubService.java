package com.pepe.githubstudy.service;


import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * @author 1one
 * @date 2019/8/6.
 */
public interface GitHubService {

    @GET
    <T> Observable<T> get(@Url String url, @QueryMap Map<String,String> map);

    // https://api.github.com/users/494778200pepe/repos
    @GET("users/{username}/repos")
    Call<String> getRepo(@Path("username")String username);
}
