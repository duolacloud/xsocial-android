package com.duolacloud.xsocial.core.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;

public class ContextUtil {
    private static Context context;

    public static Context getContext() {
        if (context == null) {
            Log.e("xsocial", XSocialText.INTER.CONTEXT_ERROR);
        }

        return context;
    }

    public static File getDataFile(String var0) {
        return context != null ? context.getDatabasePath("share.db") : null;
    }

    public static void setContext(Context var0) {
        context = var0;
    }

    public static final String getPackageName() {
        return null == context ? "" : context.getPackageName();
    }

    public static final int getIcon() {
        return null == context ? 0 : context.getApplicationInfo().icon;
    }
}
