package com.pepe.githubstudy.http.retrofit;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * @author 1one
 * @date 2019/8/7.
 */
public interface RetrofitApi {

    @FormUrlEncoded
    @POST()
    Call<ResponseBody> postMethod(@Url String url, @FieldMap Map<String,Object> params);

    @GET()
    Call<ResponseBody> getMethod(@Url String url, @QueryMap Map<String,Object> params);
}
