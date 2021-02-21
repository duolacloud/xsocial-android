package com.duolacloud.xsocial.core.model;

import android.os.Parcel;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseMediaObject implements XMediaObject {
    public static abstract class Builder<T extends BaseMediaObject> {
        protected T ref;

        public Builder thumb(XImage thumb) {
            ref.thumb = thumb;
            return this;
        }

        public Builder extra(String key, Object value) {
            ref.extra.put(key, value);
            return this;
        }

        public Builder description(String description) {
            ref.description = description;
            return this;
        }

        public Builder title(String title) {
            ref.title = title;
            return this;
        }

        public Builder url(String url) {
            ref.url = url;
            return this;
        }

        public T build() {
            return ref;
        }
    }

    protected String url = "";
    protected String title = "";
    protected Map<String, Object> extra = new HashMap();
    protected String description = "";
    protected XImage thumb;

    public BaseMediaObject() {
    }

    public String getDescription() {
        return this.description;
    }

    public Map<String, Object> getExtra() {
        return this.extra;
    }

    public XImage getThumb() {
        return this.thumb;
    }

    public String getTitle() {
        return this.title;
    }

    protected BaseMediaObject(Parcel parcel) {
        if (parcel != null) {
            this.url = parcel.readString();
            this.title = parcel.readString();
        }

    }

    @Override
    public String toUrl() {
        return this.url;
    }

    @Override
    public boolean isUrlMedia() {
        return !TextUtils.isEmpty(this.url);
    }

    @Override
    public String toString() {
        return "BaseMediaObject [media_url=" + this.url + ", title=" + this.title + "]";
    }
}
