package com.duolacloud.xsocial.core.utils;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocializeUtils {
    protected static final String TAG = "SocializeUtils";
    private static final char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static Set<Uri> deleteUris = new HashSet();
    private static Pattern mDoubleByte_Pattern = null;
    private static int smDip = 0;

    public SocializeUtils() {
    }

    public static void safeCloseDialog(Dialog var0) {
        try {
            if (var0 != null && var0.isShowing()) {
                var0.dismiss();
                var0 = null;
            }
        } catch (Exception e) {
            Log.e("xsocial", e.getMessage(), e);
        }
    }

    public static void safeShowDialog(Dialog dialog) {
        try {
            if (dialog != null && !dialog.isShowing()) {
                dialog.show();
            }
        } catch (Exception e) {
            Log.e("xsocial", e.getMessage(), e);
        }

    }

    public static Bundle parseUrl(String var0) {
        try {
            URL var1 = new URL(var0);
            Bundle var2 = decodeUrl(var1.getQuery());
            var2.putAll(decodeUrl(var1.getRef()));
            return var2;
        } catch (MalformedURLException var3) {
            return new Bundle();
        }
    }

    public static Bundle decodeUrl(String var0) {
        Bundle var1 = new Bundle();
        if (var0 != null) {
            String[] var2 = var0.split("&");
            String[] var3 = var2;
            int var4 = var2.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String var6 = var3[var5];
                String[] var7 = var6.split("=");
                var1.putString(URLDecoder.decode(var7[0]), URLDecoder.decode(var7[1]));
            }
        }

        return var1;
    }

    public static int countContentLength(String var0) {
        var0 = var0.trim();
        boolean var1 = false;
        int var2 = 0;
        Pattern var3 = getDoubleBytePattern();

        for(Matcher var4 = var3.matcher(var0); var4.find(); ++var2) {
        }

        int var5 = var0.length() - var2;
        if (var5 % 2 != 0) {
            var2 += (var5 + 1) / 2;
        } else {
            var2 += var5 / 2;
        }

        return var2;
    }

    private static Pattern getDoubleBytePattern() {
        if (mDoubleByte_Pattern == null) {
            mDoubleByte_Pattern = Pattern.compile("[^\\x00-\\xff]");
        }

        return mDoubleByte_Pattern;
    }

    public static int[] getFloatWindowSize(Context var0) {
        if (var0 == null) {
            return new int[2];
        } else {
            int[] var1 = new int[]{580, 350};
            return var1;
        }
    }

    public static boolean isFloatWindowStyle(Context var0) {
        if (var0 == null) {
            return false;
        } else {
            if (smDip == 0) {
                WindowManager windowManager = (WindowManager)var0.getSystemService(Context.WINDOW_SERVICE);
                Display var2 = windowManager.getDefaultDisplay();
                int var3 = var2.getWidth();
                int var4 = var2.getHeight();
                int var5 = var3 > var4 ? var4 : var3;
                DisplayMetrics var6 = new DisplayMetrics();
                windowManager.getDefaultDisplay().getMetrics(var6);
                smDip = (int)((float)var5 / var6.density + 0.5F);
            }

            int var7 = var0.getResources().getConfiguration().screenLayout;
            var7 &= 15;
            if (var7 >= 3 && smDip >= 550) {
                return true;
            }

            return false;
        }
    }

    public static Uri insertImage(Context var0, String var1) {
        if (!TextUtils.isEmpty(var1) && (new File(var1)).exists()) {
            try {
                String var2 = MediaStore.Images.Media.insertImage(var0.getContentResolver(), var1, "xsocial_social_shareimg", (String)null);
                if (TextUtils.isEmpty(var2)) {
                    return null;
                } else {
                    Uri var3 = Uri.parse(var2);
                    return var3;
                }
            } catch (Throwable t) {
                Log.e("xsocial", t.getMessage(), t);
                return null;
            }
        } else {
            return null;
        }
    }

    public static int dip2Px(Context var0, float var1) {
        float var2 = var0.getResources().getDisplayMetrics().density;
        return (int)(var1 * var2 + 0.5F);
    }

    public static Map<String, String> jsonToMap(String var0) {
        HashMap var1 = new HashMap();

        try {
            JSONObject var2 = new JSONObject(var0);
            Iterator var3 = var2.keys();

            while(var3.hasNext()) {
                String var4 = (String)var3.next();
                var1.put(var4, var2.get(var4) + "");
            }
        } catch (Exception e) {
            Log.e("xsocial", e.getMessage(), e);
        }

        return var1;
    }

    public static byte[] file2bytes(File var0) {
        byte[] var1 = null;

        try {
            FileInputStream var2 = new FileInputStream(var0);
            ByteArrayOutputStream var3 = new ByteArrayOutputStream();
            byte[] var4 = new byte[1024];

            int var5;
            while((var5 = var2.read(var4)) != -1) {
                var3.write(var4, 0, var5);
            }

            var2.close();
            var3.close();
            var1 = var3.toByteArray();
        } catch (FileNotFoundException e) {
            Log.e("xsocial", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("xsocial", e.getMessage(), e);
        }

        return var1;
    }

    public static Map<String, String> bundleTomap(Bundle var0) {
        if (var0 != null && !var0.isEmpty()) {
            Set var1 = var0.keySet();
            HashMap var2 = new HashMap();

            String var4;
            for(Iterator var3 = var1.iterator(); var3.hasNext(); var2.put(var4, var0.getString(var4))) {
                var4 = (String)var3.next();
                if (var4.equals("com.sina.weibo.intent.extra.USER_ICON")) {
                    var2.put("icon_url", var0.getString(var4));
                }
            }

            return var2;
        } else {
            return null;
        }
    }

    public static Bundle mapToBundle(Map<String, String> var0) {
        Bundle var1 = new Bundle();
        Iterator var2 = var0.keySet().iterator();

        while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var1.putString(var3, (String)var0.get(var3));
        }

        return var1;
    }

    public static boolean assertBinaryInvalid(byte[] var0) {
        return var0 != null && var0.length > 0;
    }

    public static boolean isToday(long var0) {
        Calendar var2 = Calendar.getInstance();
        Date var3 = new Date(System.currentTimeMillis());
        var2.setTime(var3);
        Calendar var4 = Calendar.getInstance();
        Date var5 = new Date(var0);
        var4.setTime(var5);
        if (var4.get(1) == var2.get(1)) {
            int var6 = var4.get(6) - var2.get(6);
            if (var6 == 0) {
                return true;
            }
        }

        return false;
    }

    public static String hexdigest(String var0) {
        String var1 = null;

        try {
            var1 = md5(var0.getBytes());
        } catch (Exception e) {
            Log.e("xsocial", e.getMessage(), e);
        }

        return var1;
    }

    public static String md5(byte[] var0) {
        String var1 = null;

        try {
            MessageDigest var2 = MessageDigest.getInstance("MD5");
            var2.update(var0);
            byte[] var3 = var2.digest();
            char[] var4 = new char[32];
            int var5 = 0;

            for(int var6 = 0; var6 < 16; ++var6) {
                byte var7 = var3[var6];
                var4[var5++] = hexDigits[var7 >>> 4 & 15];
                var4[var5++] = hexDigits[var7 & 15];
            }

            var1 = new String(var4);
        } catch (Exception e) {
            Log.e("xsocial", e.getMessage(), e);
        }

        return var1;
    }
}
