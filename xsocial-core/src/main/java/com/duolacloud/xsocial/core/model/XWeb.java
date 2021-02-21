package com.duolacloud.xsocial.core.model;

import com.duolacloud.xsocial.core.utils.SocializeProtocolConstants;

import java.util.HashMap;
import java.util.Map;

public class XWeb extends BaseMediaObject {
    public static class Builder extends BaseMediaObject.Builder<XWeb> {
        public Builder() {
            ref = new XWeb();
        }
    }

    private XWeb() {}

    @Override
    public XMediaObject.MediaType getMediaType() {
        return XMediaObject.MediaType.WEBPAGE;
    }

    @Override
    public Map<String, Object> toUrlExtraParams() {
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
        return "XWEB[media_url=" + this.url + ", title=" + this.title + ", description=" + this.description + "]";
    }
}
