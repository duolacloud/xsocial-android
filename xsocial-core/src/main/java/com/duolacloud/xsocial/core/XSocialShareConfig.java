package com.duolacloud.xsocial.core;

public class XSocialShareConfig {
    private CompressListener compressListener;

    public static class Builder {
        private XSocialShareConfig ref;

        public Builder() {
            ref = new XSocialShareConfig();
        }

        public Builder compressListener(CompressListener compressListener) {
            ref.compressListener = compressListener;
            return this;
        }

        XSocialShareConfig build() {
            return ref;
        }
    }

    public CompressListener getCompressListener() {
        return compressListener;
    }
}
