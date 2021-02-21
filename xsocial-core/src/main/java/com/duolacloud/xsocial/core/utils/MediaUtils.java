package com.duolacloud.xsocial.core.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import com.duolacloud.xsocial.core.model.XImage;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MediaUtils {
    private static byte[] b(Bitmap var0, Bitmap.CompressFormat var1) {
        ByteArrayOutputStream var2 = null;
        if (var0 != null && !var0.isRecycled()) {
            try {
                var2 = new ByteArrayOutputStream();
                int var3 = var0.getRowBytes() * var0.getHeight() / 1024;
                int var4 = 100;
                if ((float)var3 > com.duolacloud.xsocial.core.b.b.c.g) {
                    var4 = (int)(com.duolacloud.xsocial.core.b.b.c.g / (float)var3 * (float)var4);
                }

                if (var0 != null) {
                    var0.compress(var1, var4, var2);
                }

                byte[] var5 = var2.toByteArray();
                byte[] var6 = var5;
                return var6;
            } catch (Exception e) {
                Log.e("xsocial", XSocialText.IMAGE.BITMAOTOBINARY, e);
            } finally {
                if (var2 != null) {
                    try {
                        var2.close();
                    } catch (IOException e) {
                        Log.e("xsocial", XSocialText.IMAGE.CLOSE, e);
                    }
                }

            }

            return new byte[1];
            // return DefaultClass.getBytes();
        } else {
            return null;
        }
    }

    private static BitmapFactory.Options d(byte[] var0) {
        BitmapFactory.Options var1 = new BitmapFactory.Options();
        var1.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(var0, 0, var0.length, var1);
        int var2 = (int)Math.ceil((double)(var1.outWidth / XImage.MAX_WIDTH));
        int var3 = (int)Math.ceil((double)(var1.outHeight / XImage.MAX_HEIGHT));
        if (var3 > 1 && var2 > 1) {
            if (var3 > var2) {
                var1.inSampleSize = var3;
            } else {
                var1.inSampleSize = var2;
            }
        } else if (var3 > 2) {
            var1.inSampleSize = var3;
        } else if (var2 > 2) {
            var1.inSampleSize = var2;
        }

        var1.inJustDecodeBounds = false;
        return var1;
    }

    public static byte[] compressMedia(XImage image, int maxLength) {
        if (image == null) {
            return new byte[1];
        } else if (image.asBinImage() != null && getScale(image) >= maxLength) {
            if (image.compressStyle != XImage.CompressStyle.QUALITY) {
                try {
                    byte[] binImage = image.asBinImage();
                    if (binImage == null) {
                        return new byte[1];
                    } else if (binImage.length <= 0) {
                        return image.asBinImage();
                    } else {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(binImage, 0, binImage.length);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        baos.write(binImage, 0, binImage.length);

                        while(baos.toByteArray().length > maxLength) {
                            double var5 = Math.sqrt(1.0D * (double)binImage.length / (double)maxLength);
                            int width = (int)((double)bitmap.getWidth() / var5);
                            int height = (int)((double)bitmap.getHeight() / var5);
                            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                            baos.reset();
                            if (bitmap != null) {
                                bitmap.compress(image.compressFormat, 100, baos);
                                binImage = baos.toByteArray();
                            }
                        }

                        if (baos.toByteArray().length > maxLength) {
                            return null;
                        } else {
                            return binImage;
                        }
                    }
                } catch (Throwable t) {
                    Log.e("xsocial", t.getMessage(), t);
                    return new byte[1];
                }
            } else {
                return a(image.asBinImage(), maxLength, image.compressFormat);
            }
        } else {
            return image.asBinImage();
        }
    }

    public static byte[] getMediaData(String url) {
        return SocializeNetUtils.getNetData(url);
    }

    public static Bitmap a(byte[] var0) {
        return var0 != null ? BitmapFactory.decodeByteArray(var0, 0, var0.length) : null;
    }

    public static File b(byte[] var0) {
        try {
            File file = com.duolacloud.xsocial.core.b.b.b.a().b();
            file = a(var0, file);
            return file;
        } catch (IOException e) {
            Log.e("xsocial", XSocialText.IMAGE.BINARYTOFILE, e);
            return null;
        }
    }

    private static File a(byte[] var0, File var1) {
        BufferedOutputStream var2 = null;
        File var3 = var1;

        try {
            FileOutputStream var4 = new FileOutputStream(var3);
            var2 = new BufferedOutputStream(var4);
            var2.write(var0);
        } catch (Exception var13) {
            Log.e("xsocial", XSocialText.IMAGE.GET_FILE_FROM_BINARY, var13);
        } finally {
            if (var2 != null) {
                try {
                    var2.close();
                } catch (IOException e) {
                    Log.e(XSocialText.IMAGE.CLOSE, e.getMessage(), e);
                }
            }

        }

        return var1;
    }

    public static byte[] a(Bitmap var0, Bitmap.CompressFormat var1) {
        return b(var0, var1);
    }

    private static Bitmap a(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config var3 = (drawable.getOpacity() != PixelFormat.OPAQUE) ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, var3);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    public static byte[] a(Context context, int var1, boolean var2, Bitmap.CompressFormat var3) {
        ByteArrayOutputStream var4 = new ByteArrayOutputStream();
        if (!var2) {
            Resources var10 = context.getResources();
            Drawable var11;
            if (Build.VERSION.SDK_INT >= 21) {
                var11 = var10.getDrawable(var1, (Resources.Theme)null);
            } else {
                var11 = var10.getDrawable(var1);
            }

            Bitmap var12 = a(var11);
            if (var12 != null) {
                var12.compress(var3, 100, var4);
            }

            return var4.toByteArray();
        } else {
            byte[] var5 = new byte[0];

            try {
                BitmapFactory.Options var6 = new BitmapFactory.Options();
                var6.inPreferredConfig = Bitmap.Config.RGB_565;
                InputStream var7 = context.getResources().openRawResource(var1);
                Bitmap var8 = BitmapFactory.decodeStream(var7, (Rect)null, var6);
                if (var8 != null) {
                    var8.compress(var3, 100, var4);
                }

                var5 = var4.toByteArray();
            } catch (Error e) {
                Log.e("xsocial", XSocialText.IMAGE.TOOBIG, e);
            }

            return var5;
        }
    }

    public static byte[] a(File var0, Bitmap.CompressFormat var1) {
        return b(var0, var1);
    }

    public static String c(byte[] var0) {
        return com.duolacloud.xsocial.core.b.b.d.a(var0);
    }

    public static int getScale(XImage image) {
        return image.getImageStyle() == XImage.FILE_IMAGE ? a(image.asFileImage()) : e(image.asBinImage());
    }

    private static byte[] b(File var0, Bitmap.CompressFormat var1) {
        if (var0 != null && var0.getAbsoluteFile().exists()) {
            byte[] var2 = com.duolacloud.xsocial.core.b.b.b.a().a(var0);
            if (SocializeUtils.assertBinaryInvalid(var2)) {
                String var3 = com.duolacloud.xsocial.core.b.b.d.a(var2);
                return com.duolacloud.xsocial.core.b.b.d.m[1].equals(var3) ? var2 : a(var2, var1);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private static byte[] a(byte[] var0, Bitmap.CompressFormat var1) {
        Bitmap var2 = null;
        ByteArrayOutputStream var3 = null;
        byte[] var4 = null;

        try {
            BitmapFactory.Options var5 = d(var0);
            var2 = BitmapFactory.decodeByteArray(var0, 0, var0.length, var5);
            var3 = new ByteArrayOutputStream();
            if (var2 != null) {
                var2.compress(var1, 100, var3);
                var2.recycle();
                System.gc();
            }

            var4 = var3.toByteArray();
        } catch (Exception e) {
            Log.e("xsocial", XSocialText.IMAGE.FILE_TO_BINARY_ERROR, e);
        } finally {
            if (var3 != null) {
                try {
                    var3.close();
                } catch (IOException e) {
                    Log.e("xsocial", XSocialText.IMAGE.CLOSE, e);
                }
            }

        }

        return var4;
    }

    public static byte[] a(byte[] var0, int var1, Bitmap.CompressFormat var2) {
        boolean var3 = false;
        if (var0 != null && var0.length >= var1) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeByteArray(var0, 0, var0.length);
            int var6 = 1;
            double var7 = 1.0D;

            while(true) {
                while(!var3 && var6 <= 10) {
                    var7 = Math.pow(0.8D, (double)var6);
                    int var9 = (int)(100.0D * var7);
                    if (bitmap != null) {
                        bitmap.compress(var2, var9, baos);
                    }

                    if (baos != null && baos.size() < var1) {
                        var3 = true;
                    } else {
                        baos.reset();
                        ++var6;
                    }
                }

                if (baos != null) {
                    byte[] var10 = baos.toByteArray();
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                    }

                    if (var10 != null && var10.length <= 0) {
                        Log.e("xsocial", XSocialText.IMAGE.THUMB_ERROR);
                    }

                    return var10;
                }
                break;
            }
        }

        return var0;
    }

    private static int e(byte[] var0) {
        return var0 != null ? var0.length : 0;
    }

    private static int a(File var0) {
        if (var0 != null) {
            FileInputStream var1 = null;

            try {
                var1 = new FileInputStream(var0);
                return var1.available();
            } catch (Throwable e) {
                Log.e(XSocialText.IMAGE.GET_IMAGE_SCALE_ERROR, e.getMessage(), e);
            }
        }

        return 0;
    }

    static {
        com.duolacloud.xsocial.core.b.b.a.a();
    }
}
