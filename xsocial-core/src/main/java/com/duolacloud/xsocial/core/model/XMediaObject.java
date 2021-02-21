package com.duolacloud.xsocial.core.model;

import java.util.Map;

public interface XMediaObject {
    String toUrl();

    XMediaObject.MediaType getMediaType();

    boolean isUrlMedia();

    Map<String, Object> toUrlExtraParams();

    byte[] toByte();

    enum MediaType {
        IMAGE("0"),
        VIDEO("1"),
        MUSIC("2"),
        TEXT("3"),
        TEXT_IMAGE("4"),
        WEBPAGE("5"),
        MIN("6");

        private String type;

        MediaType(String type) {
            this.type = type;
        }
    }
}
