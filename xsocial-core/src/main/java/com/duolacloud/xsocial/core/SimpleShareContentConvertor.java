package com.duolacloud.xsocial.core;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.duolacloud.xsocial.core.model.BaseMediaObject;
import com.duolacloud.xsocial.core.model.XImage;
import com.duolacloud.xsocial.core.model.XMusic;
import com.duolacloud.xsocial.core.utils.ContextUtil;
import com.duolacloud.xsocial.core.utils.MediaUtils;
import com.duolacloud.xsocial.core.utils.XSocialText;

public class SimpleShareContentConvertor {
    public static final int THUMB_LIMIT = 24576;
    public static final int WX_THUMB_LIMIT = 18432;
    public static final int WX_MIN_LIMIT = 131072;

    public static String getShareTypeStr(int shareType) {
        switch(shareType) {
            case 1:
                return "text";
            case 2:
                return "image";
            case 3:
                return "textandimage";
            case 4:
                return "music";
            case 8:
                return "video";
            case 16:
                return "web";
            case 32:
                return "file";
            case 64:
                return "emoji";
            case 128:
                return "minapp";
            default:
                return "error";
        }
    }

    public static String objectSetTitle(BaseMediaObject mediaObject) {
        if (TextUtils.isEmpty(mediaObject.getTitle())) {
            return "这里是标题";
        } else {
            String title = mediaObject.getTitle();
            if (title.length() > 512) {
                title = title.substring(0, 512);
            }

            return title;
        }
    }

    public static String objectSetDescription(BaseMediaObject mediaObject) {
        if (TextUtils.isEmpty(mediaObject.getDescription())) {
            return "这里是描述";
        } else {
            String description = mediaObject.getDescription();
            if (description.length() > 1024) {
                description = description.substring(0, 1024);
            }

            return description;
        }
    }

    public static String objectSetText(String text, int length) {
        if (TextUtils.isEmpty(text)) {
            return "这里是描述";
        } else {
            if (text.length() > length) {
                text = text.substring(0, length);
            }

            return text;
        }
    }

    public static String objectSetText(String text) {
        return objectSetText(text, 10240);
    }

    public static byte[] objectSetThumb(BaseMediaObject mediaObject, CompressListener compressListener) {
        if (mediaObject.getThumb() == null) {
            return getIconBytes();
        } else {
            byte[] bytes;
            if (compressListener != null) {
                XImage thumb = mediaObject.getThumb();
                if (thumb == null) {
                    return new byte[1];
                }

                byte[] binImage = thumb.asBinImage();
                bytes = binImage;
                if (binImage == null || MediaUtils.getScale(thumb) > THUMB_LIMIT) {
                    bytes = compressListener.compress(binImage);
                }
            } else {
                bytes = MediaUtils.compressMedia(mediaObject.getThumb(), THUMB_LIMIT);
                if (bytes == null || bytes.length <= 0) {
                    Log.e("xsocial", XSocialText.IMAGE.SHARECONTENT_THUMB_ERROR);
                    bytes = getIconBytes();
                }
            }

            return bytes;
        }
    }

    public static byte[] objectSetMInAppThumb(BaseMediaObject mediaObject, CompressListener compressListener) {
        if (mediaObject.getThumb() == null) {
            return new byte[1];
        } else {
            byte[] var5;
            if (compressListener != null) {
                XImage var3 = mediaObject.getThumb();
                if (var3 == null) {
                    return new byte[1];
                }

                byte[] var4 = var3.asBinImage();
                var5 = var4;
                if (var4 == null || MediaUtils.getScale(var3) > WX_MIN_LIMIT) {
                    var5 = compressListener.compress(var4);
                }
            } else {
                var5 = MediaUtils.a(mediaObject.getThumb().asBinImage(), WX_MIN_LIMIT, Bitmap.CompressFormat.JPEG);
                if (var5 == null || var5.length <= 0) {
                    Log.e("xsocial", XSocialText.IMAGE.SHARECONTENT_THUMB_ERROR);
                }
            }

            return var5;
        }
    }

    public static String getMusicTargetUrl(XMusic music) {
        return TextUtils.isEmpty(music.getTargetUrl()) ? music.toUrl() : music.getTargetUrl();
    }

    public static byte[] getImageThumb(XImage image) {
        if (image.getThumb() == null) {
            return getIconBytes();
        } else {
            byte[] bytes = MediaUtils.compressMedia(image.getThumb(), WX_THUMB_LIMIT);
            if (bytes == null || bytes.length <= 0) {
                Log.e("xsocial", XSocialText.IMAGE.SHARECONTENT_THUMB_ERROR);
                bytes = getIconBytes();
            }

            return bytes;
        }
    }

    private static byte[] getIconBytes() {
        byte[] bytes = new byte[1];
        if (ContextUtil.getIcon() != 0) {
            XImage image = new XImage.Builder().context(ContextUtil.getContext()).data(ContextUtil.getIcon()).build();
            bytes = MediaUtils.compressMedia(image, WX_THUMB_LIMIT);

            if (bytes == null || bytes.length <= 0) {
                Log.e("xsocial", XSocialText.IMAGE.SHARECONTENT_THUMB_ERROR);
            }
        }

        return bytes;
    }

    public static byte[] getImageData(XImage image) {
        return image.asBinImage();
    }

    public static byte[] getStrictImageData(XImage image) {
        if (getXImageScale(image) > 491520) {
            byte[] var2 = MediaUtils.compressMedia(image, 491520);
            if (var2 != null && var2.length > 0) {
                return var2;
            } else {
                Log.e("xsocial", XSocialText.IMAGE.SHARECONTENT_THUMB_ERROR);
                return null;
            }
        } else {
            return getImageData(image);
        }
    }

    public static int getXImageScale(XImage image) {
        return MediaUtils.getScale(image);
    }

    public String subString(String var1, int var2) {
        return TextUtils.isEmpty(var1) && var1.length() > var2 ? var1.substring(0, var2) : var1;
    }

    public static boolean canFileValid(XImage image) {
        return image.asFileImage() != null;
    }
}
