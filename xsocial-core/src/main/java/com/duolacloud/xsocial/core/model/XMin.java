package com.duolacloud.xsocial.core.model;

import com.duolacloud.xsocial.core.utils.SocializeProtocolConstants;

import java.util.HashMap;
import java.util.Map;

public class XMin extends BaseMediaObject {
    public static class Builder extends BaseMediaObject.Builder<XMin> {
        public Builder() {
            ref = new XMin();
        }

        public Builder userName(String username) {
            ref.username = username;
            return this;
        }

        public Builder path(String path) {
            ref.path = path;
            return this;
        }
    }

    private String username;
    private String path;

    private XMin() {}

    public String getUserName() {
        return this.username;
    }

    public String getPath() {
        return this.path;
    }

    @Override
    public XMediaObject.MediaType getMediaType() {
        return XMediaObject.MediaType.MIN;
    }

    @Override
    public Map<String, Object> toUrlExtraParams() {
        HashMap extraParams = new HashMap();
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
}
