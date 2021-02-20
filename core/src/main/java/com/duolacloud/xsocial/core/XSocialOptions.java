package com.duolacloud.xsocial.core;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class XSocialOptions {
    private Context context;
    private Map<String, Map<String, String>> socialParams = new HashMap<>();

    private XSocialOptions() {};

    public static class Builder {
        XSocialOptions ref;
        public Builder(Context context) {
            ref = new XSocialOptions();
            ref.context = context;
        }

        public Builder registerSocialConnection(String identifier, Map<String, String> params) {
            ref.socialParams.put(identifier, params);
            return this;
        }

        public XSocialOptions build() {
            return ref;
        }
    }

    public Context getContext() {
        return context;
    }

    public Map<String, Map<String, String>> getSocialParams() {
        return socialParams;
    }
}
