package com.duolacloud.xsocial.core.model;

public class XEmoji extends XImage {
    public static class Builder extends XImage.AbstractBuilder<XEmoji> {
        public Builder() {
            ref = new XEmoji();
        }
    }

    protected XEmoji() {}
}
