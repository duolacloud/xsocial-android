package com.duolacloud.xsocial.core.b.b;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class a {
    public a() {
    }

    public static void a() {
        boolean var0 = Environment.getExternalStorageDirectory() != null && !TextUtils.isEmpty(Environment.getExternalStorageDirectory().getPath());
        if (var0) {
            c.d = Environment.getExternalStorageDirectory().getPath() + File.separator + "xsocial_cache" + File.separator;
        } else {
            c.d = Environment.getDataDirectory().getPath() + File.separator + "xsocial_cache" + File.separator;
        }

        File var1 = new File(c.d);
        if (!var1.exists()) {
            boolean var2 = var1.mkdir();
        }

        try {
            a(c.d);
        } catch (Exception e) {
            Log.e("xsocial", e.getMessage(), e);
        }

    }

    private static void a(String path) {
        File dir = new File(path);
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            int var3 = 0;

            int i;
            for(i = 0; i < files.length; ++i) {
                var3 = (int)((long)var3 + files[i].length());
            }

            if (var3 > 0 || 40 > c()) {
                i = files.length;
                Arrays.sort(files, new AComparator());

                for(int j = 0; j < i; ++j) {
                    files[j].delete();
                }
            }

        }
    }

    private static int c() {
        StatFs var0 = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double var1 = (double)var0.getAvailableBlocks() * (double)var0.getBlockSize() / 1048576.0D;
        return (int)var1;
    }

    public static void b() {
        a();
    }

    private static class AComparator implements Comparator<File> {
        private AComparator() {
        }

        @Override
        public int compare(File o1, File o2) {
            if (o1.lastModified() > o2.lastModified()) {
                return 1;
            } else {
                return o1.lastModified() == o2.lastModified() ? 0 : -1;
            }
        }
    }
}
