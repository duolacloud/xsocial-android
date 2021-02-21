package com.duolacloud.xsocial.core.utils;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

public class SocializeNetUtils {
    private static final String TAG = "SocializeNetUtils";

    public SocializeNetUtils() {
    }

    public static boolean isConSpeCharacters(String var0) {
        return var0.replaceAll("[一-龥]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0;
    }

    public static byte[] getNetData(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        } else {
            ByteArrayOutputStream baos = null;
            InputStream is = null;

            String location;
            try {
                baos = new ByteArrayOutputStream();
                HttpURLConnection conn = (HttpURLConnection)(new URL(url)).openConnection();
                conn.setInstanceFollowRedirects(true);
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
                if (conn.getResponseCode() == 301) {
                    location = conn.getHeaderField("Location");
                    if (location.equals(url)) {
                        Log.e("xsocial", XSocialText.NET.NET_AGAIN_ERROR);
                        Object var158 = null;
                        return (byte[])var158;
                    }

                    byte[] bytes = getNetData(location);
                    return bytes;
                }

                is = conn.getInputStream();
                Log.i("xsocial", XSocialText.IMAGE.IMAGEURL + url);
                byte[] buf = new byte[4096];

                int len;
                while((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }

                byte[] result = baos.toByteArray();
                return result;
            } catch (Exception e) {
                Log.e("xsocial", XSocialText.NET.IMAGEDOWN, e);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        Log.e("xsocial", XSocialText.NET.TOOL, e);
                    } finally {
                        if (baos != null) {
                            try {
                                baos.close();
                            } catch (IOException e) {
                                Log.e("xsocial", XSocialText.NET.CLOSE, e);
                            }
                        }

                    }
                }

            }

            return null;
        }
    }

    public static boolean startWithHttp(String var0) {
        return var0.startsWith("http://") || var0.startsWith("https://");
    }

    public static Bundle parseUrl(String var0) {
        try {
            URL var1 = new URL(var0);
            Bundle var2 = decodeUrl(var1.getQuery());
            var2.putAll(decodeUrl(var1.getRef()));
            return var2;
        } catch (MalformedURLException e) {
            Log.e("xocial", XSocialText.NET.TOOL, e);
            return new Bundle();
        }
    }

    public static Bundle parseUri(String path) {
        try {
            URI uri = new URI(path);
            Bundle bundle = decodeUrl(uri.getQuery());
            return bundle;
        } catch (Exception e) {
            Log.e("xsocial", XSocialText.NET.TOOL, e);
            return new Bundle();
        }
    }

    public static Bundle decodeUrl(String var0) {
        Bundle var1 = new Bundle();
        if (var0 != null) {
            String[] var2 = var0.split("&");
            String[] var3 = var2;
            int var4 = var2.length;

            for(int i = 0; i < var4; ++i) {
                String var6 = var3[i];
                String[] var7 = var6.split("=");

                try {
                    var1.putString(URLDecoder.decode(var7[0], "UTF-8"), URLDecoder.decode(var7[1], "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    Log.e("xsocial", XSocialText.NET.TOOL, e);
                }
            }
        }

        return var1;
    }

    public static String request(String var0) {
        String var1 = "";

        try {
            URL var2 = new URL(var0);
            URLConnection var3 = var2.openConnection();
            if (var3 == null) {
                return var1;
            } else {
                var3.connect();
                InputStream var4 = var3.getInputStream();
                return var4 == null ? var1 : convertStreamToString(var4);
            }
        } catch (Exception e) {
            Log.e("xsocial", XSocialText.NET.TOOL, e);
            return var1;
        }
    }

    public static String convertStreamToString(InputStream var0) {
        BufferedReader var1 = new BufferedReader(new InputStreamReader(var0));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while((line = var1.readLine()) != null) {
                sb.append(line + "/n");
            }
        } catch (IOException e) {
            Log.e("xsocial", XSocialText.NET.TOOL, e);
        } finally {
            try {
                var0.close();
            } catch (IOException e) {
                Log.e("xsocial", XSocialText.NET.TOOL, e);
            }

        }

        return sb.toString();
    }
}
