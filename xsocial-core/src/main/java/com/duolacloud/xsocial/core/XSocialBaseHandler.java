package com.duolacloud.xsocial.core;

public abstract class XSocialBaseHandler implements XSocialHandler {
    protected XSocialShareConfig mShareConfig;

    public XSocialShareConfig getmShareConfig() {
        return mShareConfig;
    }

    public void setmShareConfig(XSocialShareConfig mShareConfig) {
        this.mShareConfig = mShareConfig;
    }
}
