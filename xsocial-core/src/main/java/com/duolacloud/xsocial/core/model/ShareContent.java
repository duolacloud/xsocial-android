package com.duolacloud.xsocial.core.model;

import android.text.TextUtils;

import java.io.File;

public class ShareContent {
    public static final int TEXT_STYLE = 1;
    public static final int IMAGE_STYLE = 2;
    public static final int TEXT_IMAGE_STYLE = 3;
    public static final int MUSIC_STYLE = 4;
    public static final int VIDEO_STYLE = 8;
    public static final int WEB_STYLE = 16;
    public static final int FILE_STYLE = 32;
    public static final int EMOJI_STYLE = 64;
    public static final int MINAPP_STYLE = 128;
    public static final int ERROR_STYLE = 0;

    public static class Builder {
        private ShareContent ref;
        public Builder() {
            ref = new ShareContent();
        }

        public Builder subject(String value) {
            ref.subject = value;
            return this;
        }

        public Builder text(String value) {
            ref.text = value;
            return this;
        }

        public Builder withMedia(XMediaObject value) {
            ref.media = value;
            return this;
        }

        public Builder withMedias(XImage... value) {
            ref.medias = value;
            return this;
        }

        public ShareContent build() {
            return ref;
        }
    }

    String subject = "";
    String text = "";
    XMediaObject media;
    XMediaObject extra;
    String mFollow;
    File file;
    File app;
    XImage[] medias;

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public XMediaObject getMedia() {
        return media;
    }

    public XMediaObject getExtra() {
        return extra;
    }

    public String getmFollow() {
        return mFollow;
    }

    public File getFile() {
        return file;
    }

    public File getApp() {
        return app;
    }

    public XImage[] getMedias() {
        return medias;
    }

    public ShareContent() {
    }

    public int getShareType() {
        if (this.media == null && this.extra == null && this.file == null) {
            return TextUtils.isEmpty(this.text) ? ERROR_STYLE : TEXT_STYLE;
        } else if (this.file != null) {
            return FILE_STYLE;
        } else {
            if (this.media != null) {
                if (this.media instanceof XEmoji) {
                    return EMOJI_STYLE;
                }

                if (this.media instanceof XImage) {
                    if (TextUtils.isEmpty(this.text)) {
                        return IMAGE_STYLE;
                    }

                    return TEXT_IMAGE_STYLE;
                }

                if (this.media instanceof XMusic) {
                    return MUSIC_STYLE;
                }

                if (this.media instanceof XVideo) {
                    return VIDEO_STYLE;
                }

                if (this.media instanceof XWeb) {
                    return WEB_STYLE;
                }

                if (this.media instanceof XMin) {
                    return MINAPP_STYLE;
                }
            }

            return ERROR_STYLE;
        }
    }
}
