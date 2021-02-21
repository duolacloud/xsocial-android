package com.duolacloud.xsocial.core;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.duolacloud.xsocial.core.model.ShareContent;
import com.duolacloud.xsocial.core.utils.ContextUtil;
import com.duolacloud.xsocial.core.utils.XSocialText;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class XSocialAPI {
    private static ExecutorService mNetExecutor = Executors.newFixedThreadPool(5);

    final static Map<String, XSocialHandler> handlerDict = new HashMap<>();

    private static XSocialOptions _options;

    public static <T extends XSocialHandler> T getHandler(String identifier) {
        return (T) handlerDict.get(identifier);
    }

    public static void init(XSocialOptions options) {
        _options = options;

        ContextUtil.setContext(_options.getContext().getApplicationContext());

        handlerDict.clear();

        for (Map.Entry<String, Map<String, String>> entry : options.getSocialParams().entrySet()) {
            try {
                String platform = entry.getKey();
                Map<String, String> params = entry.getValue();

                String className = platform.substring(0, 1).toUpperCase() + platform.substring(1) + "Handler";
                className = "com.duolacloud.xsocial." + platform + "." + className;
                Class<XSocialHandler> cls = (Class<XSocialHandler>) Class.forName(className);
                XSocialHandler handler = (XSocialHandler)cls.newInstance();
                handler.init(_options.getContext(), params);
                handlerDict.put(platform, handler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void authorize(Activity activity, String identifier, Map<String, Object> params, XSocialAuthListener listener) {
        String[] items = identifier.split(":");
        if (items.length != 2) {
            listener.onError(identifier, new Throwable("identifier 格式错误: " + identifier));
        }

        String platform = items[0];
        String way = items[1];

        XSocialHandler handler = handlerDict.get(platform);
        if (handler == null)  {
            listener.onError(identifier, new Throwable(XSocialText.AUTH.NOT_SUPPORT_PLATFROM + ", 找不到 " + platform + " 对应的实现, 也许是包没有加载"));
            return;
        }

        activity.runOnUiThread(() -> {
            listener.onStart(identifier);
        });

        mNetExecutor.execute(() -> {
            handler.authorize(activity, way, params, listener);
        });
    }

    public static void share(Activity activity, String identifier, final ShareContent shareContent, final XSocialShareListener listener) {
        String[] items = identifier.split(":");
        if (items.length != 2) {
            listener.onError(identifier, new Throwable("identifier 格式错误: " + identifier));
        }

        String platform = items[0];
        String way = items[1];


        final WeakReference<Activity> ref = new WeakReference(activity);
        if (ref.get() == null || ((Activity)ref.get()).isFinishing()) {
            Log.e("xsocial", XSocialText.CHECK.ACTIVITYNULL);
            return;
        }

        XSocialHandler handler = handlerDict.get(platform);
        if (handler == null)  {
            listener.onError(identifier, new Throwable(", 找不到 " + platform + " 对应的实现, 也许是包没有加载"));
            return;
        }

        activity.runOnUiThread(() -> {
            listener.onStart(identifier);
        });

        mNetExecutor.execute(() -> {
            handler.share(activity, way, shareContent, listener);
        });
    }

}
