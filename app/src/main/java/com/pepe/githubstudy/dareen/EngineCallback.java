package com.pepe.githubstudy.dareen;

/**
 * @author 1one
 * @date 2019/8/7.
 */
public interface EngineCallback {

    void onSuccess(String result);

    void onFailure(Exception e);
}
