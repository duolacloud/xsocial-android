package com.duolacloud.xsocial.core;

import android.app.Activity;
import java.util.HashMap;
import java.util.Map;

public class XSocialAPI {
    final static Map<String, XSocialAuthHandler> handlerDict = new HashMap<>();

    private static XSocialOptions _options;

    public static <T extends XSocialAuthHandler> T getHandler(String identifier) {
        return (T) handlerDict.get(identifier);
    }

    public static void init(XSocialOptions options) {
        _options = options;

        handlerDict.clear();

        for (Map.Entry<String, Map<String, String>> entry : options.getSocialParams().entrySet()) {
            try {
                String identifier = entry.getKey();
                Map<String, String> params = entry.getValue();
                String[] items = identifier.split(":");
                if (items.length == 0) {
                    continue;
                }

                String platform = items[0];
                String className = platform.substring(0, 1).toUpperCase() + platform.substring(1) + "Handler";
                className = "com.duolacloud.xsocial." + platform + "." + className;
                Class<XSocialAuthHandler> cls = (Class<XSocialAuthHandler>) Class.forName(className);
                XSocialAuthHandler handler = (XSocialAuthHandler)cls.newInstance();
                handler.init(_options.getContext(), params);
                handlerDict.put(identifier, handler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void authorize(Activity activity, String identifier, Map<String, Object> params, XSocialAuthListener listener) {
        XSocialAuthHandler handler = handlerDict.get(identifier);
        if (handler == null)  {
            listener.onError(identifier, new Throwable("没有找到 " + identifier + " 对应的实现, 也许是包没有加载"));
            return;
        }

        activity.runOnUiThread(() -> {
            listener.onStart(identifier);
        });

        handler.authorize(activity, params, listener);
    }
}
