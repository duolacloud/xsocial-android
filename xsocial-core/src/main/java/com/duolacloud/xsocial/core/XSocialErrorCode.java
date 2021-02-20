package com.duolacloud.xsocial.core;

public enum XSocialErrorCode {
    UnKnowCode(2000),
    AuthorizeFailed(2002),
    ShareFailed(2003),
    RequestForUserProfileFailed(2004),
    ShareDataNil(2004),
    ShareDataTypeIllegal(2004),
    NotInstall(2008);

    private final int code;

    XSocialErrorCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        if (this == UnKnowCode) {
            return this.getCode() + "未知错误----";
        } else if (this == AuthorizeFailed) {
            return this.getCode() + "授权失败----";
        } else if (this == ShareFailed) {
            return this.getCode() + "分享失败----";
        } else if (this == RequestForUserProfileFailed) {
            return this.getCode() + "获取用户资料失败----";
        } else if (this == ShareDataNil) {
            return this.getCode() + "分享内容为空";
        } else if (this == ShareDataTypeIllegal) {
            return this.getCode() + "分享内容不合法----";
        } else {
            return this == NotInstall ? this.getCode() + "没有安装应用" : "unkown";
        }
    }

    private String getCode() {
        return "错误码：" + this.code + " 错误信息：";
    }
}
