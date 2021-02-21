package com.duolacloud.xsocial.core.model;

import android.os.Parcel;
import com.duolacloud.xsocial.core.utils.SocializeProtocolConstants;

import java.util.HashMap;
import java.util.Map;

public class XMusic extends BaseMediaObject {
    public static class Builder extends BaseMediaObject.Builder<XMusic> {
        public Builder() {
            ref = new XMusic();
        }

        public Builder targetUrl(String targetUrl) {
            ref.targetUrl = targetUrl;
            return this;
        }

        public Builder duration(int duration) {
            ref.duration = duration;
            return this;
        }

        public Builder lowBandUrl(String lowBandUrl) {
            ref.lowBandUrl = lowBandUrl;
            return this;
        }
        public Builder lowBandDataUrl(String lowBandDataUrl) {
            ref.lowBandDataUrl = lowBandDataUrl;
            return this;
        }

        public Builder highBandDataUrl(String highBandDataUrl) {
            ref.highBandDataUrl = highBandDataUrl;
            return this;
        }

        public Builder h5Url(String h5Url) {
            ref.h5Url = h5Url;
            return this;
        }
    }

    private String lowBandDataUrl;

    private String highBandDataUrl;

    private String h5Url;

    private String lowBandUrl;

    // j
    private int duration;

    // k
    private String targetUrl;

    @Override
    public XMediaObject.MediaType getMediaType() {
        return XMediaObject.MediaType.MUSIC;
    }

    private XMusic() {}

    protected XMusic(Parcel parcel) {
        super(parcel);
    }

    public String getLowBandDataUrl() {
        return lowBandDataUrl;
    }

    public String getHighBandDataUrl() {
        return highBandDataUrl;
    }

    public String getH5Url() {
        return h5Url;
    }

    public String getLowBandUrl() {
        return lowBandUrl;
    }

    public int getDuration() {
        return duration;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public Map<String, Object> toUrlExtraParams() {
        Map<String, Object> extraParams = new HashMap<>();
        if (this.isUrlMedia()) {
            extraParams.put(SocializeProtocolConstants.PROTOCOL_KEY_FURL, this.url);
            extraParams.put(SocializeProtocolConstants.PROTOCOL_KEY_FTYPE, this.getMediaType());
            extraParams.put(SocializeProtocolConstants.PROTOCOL_KEY_TITLE, this.title);
        }

        return extraParams;
    }

    @Override
    public byte[] toByte() {
        return this.thumb != null ? this.thumb.toByte() : null;
    }

    @Override
    public String toString() {
        return "XMusic [media_url=" + url + ", title=" + title + "]";
    }
}
