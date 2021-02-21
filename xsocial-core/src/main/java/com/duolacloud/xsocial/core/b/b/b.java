package com.duolacloud.xsocial.core.b.b;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.duolacloud.xsocial.core.utils.ContextUtil;
import com.duolacloud.xsocial.core.utils.SocializeUtils;
import com.duolacloud.xsocial.core.utils.XSocialText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class b {
    private String a = "";
    private static b b = new b();

    private b() {
        try {
            this.a = ContextUtil.getContext().getCacheDir().getCanonicalPath();
        } catch (IOException e) {
            Log.e("xsocial", e.getMessage(), e);
        }

    }

    public static b a() {
        return b == null ? new b() : b;
    }

    public File b() throws IOException {
        File var1 = new File(this.c(), this.d());
        if (var1.exists()) {
            var1.delete();
        }

        var1.createNewFile();
        return var1;
    }

    public File c() throws IOException {
        String var1;
        if (Environment.getExternalStorageDirectory() != null && !TextUtils.isEmpty(Environment.getExternalStorageDirectory().getCanonicalPath())) {
            var1 = Environment.getExternalStorageDirectory().getCanonicalPath();
        } else if (!TextUtils.isEmpty(this.a)) {
            var1 = this.a;
            Log.e("xsocial", XSocialText.CACHE.SD_NOT_FOUNT);
        } else {
            var1 = "";
            Log.e("xsocial", XSocialText.CACHE.SD_NOT_FOUNT);
        }

        File var2 = new File(var1 + "/xsocial_cache/");
        if (var2 != null && !var2.exists()) {
            var2.mkdirs();
        }

        return var2;
    }

    public byte[] a(File var1) {
        Object var2 = null;
        FileInputStream var3 = null;
        ByteArrayOutputStream var4 = null;

        byte[] var6;
        try {
            var3 = new FileInputStream(var1);
            var4 = new ByteArrayOutputStream();
            byte[] var5 = new byte[4096];

            int var19;
            while((var19 = var3.read(var5)) != -1) {
                var4.write(var5, 0, var19);
            }

            byte[] var18 = var4.toByteArray();
            return var18;
        } catch (Throwable t) {
            Log.e("xsocial", XSocialText.IMAGE.READ_IMAGE_ERROR, t);
            var6 = new byte[1];
        } finally {
            try {
                if (var3 != null) {
                    var3.close();
                }

                if (var4 != null) {
                    var4.close();
                }
            } catch (IOException e) {
                Log.e("xsocial", XSocialText.IMAGE.CLOSE, e);
            }

        }

        return var6;
    }

    public String d() {
        long var1 = System.currentTimeMillis();
        String var3 = SocializeUtils.hexdigest(String.valueOf(var1));
        return var3;
    }
}
