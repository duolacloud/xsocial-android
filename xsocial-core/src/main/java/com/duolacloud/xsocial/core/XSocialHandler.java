package com.duolacloud.xsocial.core;

import android.app.Activity;
import android.content.Context;

import com.duolacloud.xsocial.core.model.ShareContent;

import java.util.Map;

public interface XSocialHandler {
    void init(Context context, Map<String, String> params);

    String getPlatform();

    boolean isAppInstalled();

    void authorize(Activity activity, String way, Map<String, Object> params, XSocialAuthListener listener);

    boolean share(Activity activity, String way, ShareContent content, XSocialShareListener listener);
}
