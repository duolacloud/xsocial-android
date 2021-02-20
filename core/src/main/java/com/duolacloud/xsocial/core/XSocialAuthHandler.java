package com.duolacloud.xsocial.core;

import android.app.Activity;
import android.content.Context;

import java.util.Map;

public interface XSocialAuthHandler {
    void init(Context context, Map<String, String> params);

    String getIdentifier();

    boolean isAppInstalled();

    void authorize(Activity activity, Map<String, Object> params, XSocialAuthListener listener);
}
