package com.duolacloud.xsocial.core;

import java.util.Map;

public interface XSocialAuthListener {
    void onStart(String identifier);

    void onComplete(String identifier, Map<String, String> data);

    void onError(String identifier, Throwable e);

    void onCancel(String identifier);
}
