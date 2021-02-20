package com.duolacloud.xsocial.core;

public interface XSocialShareListener {
    void onStart(String identifier);

    void onResult(String identifier);

    void onError(String identifier, Throwable e);

    void onCancel(String identifier);
}
