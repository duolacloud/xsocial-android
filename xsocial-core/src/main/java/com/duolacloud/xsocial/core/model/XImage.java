package com.duolacloud.xsocial.core.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

import com.duolacloud.xsocial.core.utils.MediaUtils;
import com.duolacloud.xsocial.core.utils.SocializeProtocolConstants;
import com.duolacloud.xsocial.core.utils.SocializeUtils;
import com.duolacloud.xsocial.core.utils.XSocialText;

import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class XImage extends BaseMediaObject {
    public static int MAX_WIDTH = 768;
    public static int MAX_HEIGHT = 1024;
    public static int FILE_IMAGE = 1;
    public static int URL_IMAGE = 2;
    public static int RES_IMAGE = 3;
    public static int BITMAP_IMAGE = 4;
    public static int BINARY_IMAGE = 5;

    protected Context context;
    protected XImageMark imageMark;

    private XImage.ConfiguredConvertor convertor = null;
    public boolean isLoadImgByCompress = true;
    public XImage.CompressStyle compressStyle;
    public Bitmap.CompressFormat compressFormat;
    private boolean hasWaterMark;

    private int imageStyle;

    public static class Builder extends AbstractBuilder<XImage> {
        public Builder() {
            ref = new XImage();
        }
    }

    public static class AbstractBuilder<T extends XImage> extends BaseMediaObject.Builder<T> {
        private Object data;

        public AbstractBuilder context(Context context) {
            ref.context = context;
            return this;
        }

        public AbstractBuilder data(int resId) {
            this.data = resId;
            return this;
        }

        public AbstractBuilder data(String url) {
            this.data = url;
            return this;
        }

        public AbstractBuilder data(byte[] data) {
            this.data = data;
            return this;
        }

        public AbstractBuilder data(File file) {
            this.data = file;
            return this;
        }

        public AbstractBuilder data(Bitmap bitmap) {
            this.data = bitmap;
            return this;
        }

        public AbstractBuilder imageMark(XImageMark imageMark) {
            ref.imageMark = imageMark;
            return this;
        }

        @Override
        public T build() {
            ref.init(ref.context, data, ref.imageMark);
            return super.build();
        }
    }

    protected XImage() {
        super();

        this.compressStyle = XImage.CompressStyle.SCALE;
        this.compressFormat = Bitmap.CompressFormat.JPEG;
        this.imageStyle = 0;
    }

    protected void init(Context context, Object data, XImageMark imageMark) {
        if (imageMark != null) {
            this.hasWaterMark = true;
            this.imageMark = imageMark;
            this.imageMark.setContext(context);
        }

        if (data instanceof File) {
            this.imageStyle = FILE_IMAGE;
            this.convertor = new XImage.FileConvertor((File)data);
        } else if (data instanceof String) {
            this.imageStyle = URL_IMAGE;
            this.convertor = new XImage.UrlConvertor((String)data);
        } else {
            Bitmap bitmap;
            if (data instanceof Integer) {
                this.imageStyle = RES_IMAGE;
                bitmap = null;
                if (this.isHasWaterMark()) {
                    bitmap = this.aa(context, (Integer)data);
                }

                if (bitmap != null) {
                    this.convertor = new XImage.BitmapConvertor(bitmap);
                } else {
                    this.convertor = new XImage.ResConvertor(context.getApplicationContext(), (Integer)data);
                }
            } else if (data instanceof byte[]) {
                this.imageStyle = BINARY_IMAGE;
                bitmap = null;
                if (this.isHasWaterMark()) {
                    bitmap = this.init((byte[])((byte[])data));
                }

                if (bitmap != null) {
                    this.convertor = new XImage.BitmapConvertor(bitmap);
                } else {
                    this.convertor = new XImage.BinaryConvertor((byte[])((byte[])data));
                }
            } else if (data instanceof Bitmap) {
                this.imageStyle = BITMAP_IMAGE;
                bitmap = null;
                if (this.isHasWaterMark()) {
                    bitmap = this.compound((Bitmap)data, true);
                }

                if (bitmap == null) {
                    bitmap = (Bitmap)data;
                }

                this.convertor = new XImage.BitmapConvertor(bitmap);
            } else if (data != null) {
                Log.e("XSocial", XSocialText.IMAGE.UNKNOW_UMIMAGE + data.getClass().getSimpleName());
            } else {
                Log.e("XSocial", XSocialText.IMAGE.UNKNOW_UMIMAGE + "null");
            }
        }

    }

    public byte[] toByte() {
        return this.asBinImage();
    }

    public final Map<String, Object> toUrlExtraParams() {
        HashMap extraParams = new HashMap();
        if (this.isUrlMedia()) {
            extraParams.put(SocializeProtocolConstants.PROTOCOL_KEY_FURL, this.url);
            extraParams.put(SocializeProtocolConstants.PROTOCOL_KEY_FTYPE, this.getMediaType());
        }

        return extraParams;
    }

    public XMediaObject.MediaType getMediaType() {
        return XMediaObject.MediaType.IMAGE;
    }

    public int getImageStyle() {
        return this.imageStyle;
    }

    public File asFileImage() {
        return this.convertor == null ? null : this.convertor.asFile();
    }

    public String asUrlImage() {
        return this.convertor == null ? null : this.convertor.asUrl();
    }

    private byte[] imageData;
    public byte[] asBinImage() {
        if (this.convertor == null) return null;

        if (imageData != null) {
            return imageData;
        }

        imageData = this.convertor.asBinary();
        return imageData;
    }

    public Bitmap asBitmap() {
        return this.convertor == null ? null : this.convertor.asBitmap();
    }

    private Bitmap compound(Bitmap bitmap, boolean isSquare) {
        if (this.imageMark == null) {
            return bitmap;
        } else if (bitmap == null) {
            return null;
        } else {
            try {
                if (isSquare) {
                    bitmap = this.square(bitmap);
                }

                return this.imageMark.compound(bitmap);
            } catch (Exception e) {
                Log.e("xsocial", e.getMessage(), e);
                return null;
            }
        }
    }

    /**
     * a
     * @param context
     * @param var2
     * @return
     */
    private Bitmap aa(Context context, int var2) {
        if (var2 != 0 && context != null && this.imageMark != null) {
            InputStream is = null;

            try {
                BitmapFactory.Options opitons = new BitmapFactory.Options();
                opitons.inJustDecodeBounds = true;
                is = context.getResources().openRawResource(var2);
                BitmapFactory.decodeStream(is, (Rect)null, opitons);
                this.init((Closeable)is);
                int var5 = (int)this.init((float)opitons.outWidth, (float)opitons.outHeight, (float)MAX_WIDTH, (float)MAX_HEIGHT);
                if (var5 > 0) {
                    opitons.inSampleSize = var5;
                }

                opitons.inJustDecodeBounds = false;
                is = context.getResources().openRawResource(var2);
                Bitmap bitmap = BitmapFactory.decodeStream(is, (Rect)null, opitons);
                Bitmap crop = this.compound(bitmap, false);
                return crop;
            } catch (Exception e) {
                Log.e("xsocial", e.getMessage(), e);
            } finally {
                this.init((Closeable)is);
            }

            return null;
        } else {
            return null;
        }
    }

    private void init(Closeable var1) {
        try {
            if (var1 != null) {
                var1.close();
            }
        } catch (Exception e) {
            Log.e("xsocial", e.getMessage(), e);
        }

    }

    private Bitmap init(byte[] var1) {
        if (var1 != null && this.imageMark != null) {
            try {
                BitmapFactory.Options var2 = new BitmapFactory.Options();
                var2.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(var1, 0, var1.length, var2);
                int var3 = (int)this.init((float)var2.outWidth, (float)var2.outHeight, (float)MAX_WIDTH, (float)MAX_HEIGHT);
                if (var3 > 0) {
                    var2.inSampleSize = var3;
                }

                var2.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeByteArray(var1, 0, var1.length, var2);
                return this.compound(bitmap, false);
            } catch (Exception e) {
                Log.e("xsocial", e.getMessage(), e);
                return null;
            }
        } else {
            return null;
        }
    }

    private Bitmap square(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float sx = this.init((float)width, (float)height, (float)MAX_WIDTH, (float)MAX_HEIGHT);
        if (sx < 0.0F) {
            return bitmap;
        } else {
            sx = 1.0F / sx;
            Matrix matrix = new Matrix();
            matrix.postScale(sx, sx);
            Bitmap crop = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
            this.b(crop);
            return crop;
        }
    }

    private float init(float var1, float var2, float var3, float var4) {
        if (var1 <= var4 && var2 <= var4) {
            return -1.0F;
        } else {
            float var5 = var1 / var3;
            float var6 = var2 / var4;
            return var5 > var6 ? var5 : var6;
        }
    }

    private void b(Bitmap bitmap) {
        try {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        } catch (Exception e) {
            Log.e("xsocial", e.getMessage(), e);
        }

    }

    public boolean isHasWaterMark() {
        return this.hasWaterMark;
    }

    interface IImageConvertor {
        File asFile();

        String asUrl();

        byte[] asBinary();

        Bitmap asBitmap();
    }

    abstract static class ConfiguredConvertor implements XImage.IImageConvertor {
        ConfiguredConvertor() {
        }
    }

    class ResConvertor extends XImage.ConfiguredConvertor {
        private Context context;
        private int resId = 0;

        public ResConvertor(Context var2, int resId) {
            this.context = var2;
            this.resId = resId;
        }

        public File asFile() {
            return SocializeUtils.assertBinaryInvalid(this.asBinary()) ? MediaUtils.b(this.asBinary()) : null;
        }

        public String asUrl() {
            return null;
        }

        public byte[] asBinary() {
            return MediaUtils.a(this.context, this.resId, XImage.this.isLoadImgByCompress, XImage.this.compressFormat);
        }

        public Bitmap asBitmap() {
            return SocializeUtils.assertBinaryInvalid(this.asBinary()) ? MediaUtils.a(this.asBinary()) : null;
        }
    }

    class BinaryConvertor extends XImage.ConfiguredConvertor {
        private byte[] data;

        public BinaryConvertor(byte[] var2) {
            this.data = var2;
        }

        public File asFile() {
            return SocializeUtils.assertBinaryInvalid(this.asBinary()) ? MediaUtils.b(this.asBinary()) : null;
        }

        public String asUrl() {
            return null;
        }

        public byte[] asBinary() {
            return this.data;
        }

        public Bitmap asBitmap() {
            return SocializeUtils.assertBinaryInvalid(this.asBinary()) ? MediaUtils.a(this.asBinary()) : null;
        }
    }

    class UrlConvertor extends XImage.ConfiguredConvertor {
        private String url;

        public UrlConvertor(String url) {
            this.url = url;
        }

        public File asFile() {
            return SocializeUtils.assertBinaryInvalid(this.asBinary()) ? MediaUtils.b(this.asBinary()) : null;
        }

        public String asUrl() {
            return this.url;
        }

        public byte[] asBinary() {
            return MediaUtils.getMediaData(this.url);
        }

        public Bitmap asBitmap() {
            return SocializeUtils.assertBinaryInvalid(this.asBinary()) ? MediaUtils.a(this.asBinary()) : null;
        }
    }

    class FileConvertor extends XImage.ConfiguredConvertor {
        private File file;

        public FileConvertor(File file) {
            this.file = file;
        }

        public File asFile() {
            return this.file;
        }

        public String asUrl() {
            return null;
        }

        public byte[] asBinary() {
            return MediaUtils.a(this.file, XImage.this.compressFormat);
        }

        public Bitmap asBitmap() {
            return SocializeUtils.assertBinaryInvalid(this.asBinary()) ? MediaUtils.a(XImage.this.asBinImage()) : null;
        }
    }

    class BitmapConvertor extends XImage.ConfiguredConvertor {
        private Bitmap bitmap;

        public BitmapConvertor(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public File asFile() {
            byte[] var1 = MediaUtils.a(this.bitmap, XImage.this.compressFormat);
            return SocializeUtils.assertBinaryInvalid(this.asBinary()) ? MediaUtils.b(var1) : null;
        }

        public String asUrl() {
            return null;
        }

        public byte[] asBinary() {
            return MediaUtils.a(this.bitmap, XImage.this.compressFormat);
        }

        public Bitmap asBitmap() {
            return this.bitmap;
        }
    }

    public enum CompressStyle {
        SCALE,
        QUALITY;
    }
}
