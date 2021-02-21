package com.duolacloud.xsocial.core.model;


import com.duolacloud.xsocial.core.utils.SocializeProtocolConstants;

import java.util.HashMap;
import java.util.Map;

public class XVideo extends BaseMediaObject {
    private String lowBandUrl;
    private String lowBandDataUrl;
    private String highBandDataUrl;
    private String h5url;
    private int duration;

    public static class Builder extends BaseMediaObject.Builder<XVideo> {
        public Builder() {
            ref = new XVideo();
        }

        public Builder lowBandUrl(String lowBandUrl) {
            ref.lowBandUrl = lowBandUrl;
            return this;
        }

        public Builder h5Url(String h5url) {
            ref.h5url = h5url;
            return this;
        }

        public Builder highBandDataUrl(String highBandDataUrl) {
            ref.highBandDataUrl = highBandDataUrl;
            return this;
        }

        public Builder lowBandDataUrl(String lowBandDataUrl) {
            ref.lowBandDataUrl = lowBandDataUrl;
            return this;
        }

        public Builder duration(int duration) {
            ref.duration = duration;
            return this;
        }
    }

    private XVideo() { }

    public int getDuration() {
        return this.duration;
    }

    public String getLowBandUrl() {
        return this.lowBandUrl;
    }

    public String getLowBandDataUrl() {
        return this.lowBandDataUrl;
    }

    public String getHighBandDataUrl() {
        return this.highBandDataUrl;
    }

    public String getH5Url() {
        return this.h5url;
    }

    @Override
    public XMediaObject.MediaType getMediaType() {
        return XMediaObject.MediaType.VIDEO;
    }

    @Override
    public final Map<String, Object> toUrlExtraParams() {
        HashMap extraParams = new HashMap();
        if (this.isUrlMedia()) {
            extraParams.put(SocializeProtocolConstants.PROTOCOL_KEY_FURL, this.url);
            extraParams.put(SocializeProtocolConstants.PROTOCOL_KEY_FTYPE, this.getMediaType());
        }

        return extraParams;
    }

    @Override
    public byte[] toByte() {
        return this.thumb != null ? this.thumb.toByte() : null;
    }

    @Override
    public String toString() {
        return "XVideo [media_url=" + this.url + ", title=" + this.title + ", thumb=" + this.url + "]";
    }
}
