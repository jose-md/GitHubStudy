package com.pepe.githubstudy.http;

import android.content.Context;

import java.util.Map;

/**
 * @author 1one
 * @date 2019/8/7.
 */
public interface IHttpRequest {

    void get(String url, Map<String, Object> params,
             final EngineCallback callback, final boolean cache);

    void post(String url, Map<String, Object> params,
              final EngineCallback callback, final boolean cache);

    void download(String url, Map<String, Object> params,
                  final EngineCallback callback);

    void upload(String url, Map<String, Object> params,
                final EngineCallback callback);
}
