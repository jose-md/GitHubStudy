package com.pepe.githubstudy.http.retrofit;

import com.pepe.githubstudy.dareen.EngineCallback;
import com.pepe.githubstudy.dareen.IHttpRequest;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author 1one
 * @date 2019/8/7.
 */
public class RetrofitRequest implements IHttpRequest {
    @Override
    public void get(String url, Map<String, Object> params, final EngineCallback callback, boolean cache) {
        RetrofitClient.getServiceApi().getMethod(url, params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // 成功解析即可
                ResponseBody body = response.body();
                if (body == null) {
                    // 401
                    body = response.errorBody();
                }
                try {
                    callback.onSuccess(body.string());
                } catch (IOException e) {
                    callback.onFailure(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure(new RetrofitErrorException(t.getMessage()));
            }
        });
    }

    @Override
    public void getWithHeaders(String url, Map<String, Object> params, Map<String ,String> headers,final EngineCallback callback, boolean cache) {
        RetrofitClient.getServiceApi().getWithHeadersMethod(url, params,headers).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // 成功解析即可
                ResponseBody body = response.body();
                if (body == null) {
                    // 401
                    body = response.errorBody();
                }
                try {
                    callback.onSuccess(body.string());
                } catch (IOException e) {
                    callback.onFailure(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure(new RetrofitErrorException(t.getMessage()));
            }
        });
    }

    @Override
    public void post(String url, Map<String, Object> params, final EngineCallback callback, boolean cache) {
        RetrofitClient.getServiceApi().postMethod(url, params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // 成功解析即可
                ResponseBody body = response.body();
                if (body == null) {
                    // 401
                    body = response.errorBody();
                }
                try {
                    callback.onSuccess(body.string());
                } catch (IOException e) {
                    callback.onFailure(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure(new RetrofitErrorException(t.getMessage()));
            }
        });
    }

    @Override
    public void download(String url, Map<String, Object> params, EngineCallback callback) {

    }

    @Override
    public void upload(String url, Map<String, Object> params, EngineCallback callback) {

    }
}
