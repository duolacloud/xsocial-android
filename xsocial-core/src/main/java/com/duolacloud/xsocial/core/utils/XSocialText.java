package com.duolacloud.xsocial.core.utils;

public class XSocialText {
    public static String FULL = "完整版";
    public static String SIMPLE = "精简版";
    public static String MAN = "男";
    public static String WOMAN = "女";
    public static String SOLVE = "解决方案--";

    public XSocialText() {
    }

    public static String errorWithUrl(String code, String url) {
        return code + "\n解决方案：" + url;
    }

    public static String resError(String var0, String var1, String var2) {
        return "缺少资源文件:packageName=" + var0 + "\n type=" + var1 + "\n name=" + var2 + "请去sdk下载包中拷贝";
    }

    public static String netMethodError(String var0) {
        return "验证请求方式失败[" + var0 + "]";
    }

    public static String supportStyle(boolean var0, String var1) {
        StringBuilder var2 = new StringBuilder("该平台");
        var2.append(var0 ? "只" : "不").append("支持");
        if (var1.contains("image")) {
            var2.append("图片");
        }

        if (var1.contains("netimg")) {
            var2.append("网络图片");
        }

        if (var1.contains("localimg")) {
            var2.append("本地图片");
        }

        if (var1.contains("url")) {
            var2.append("链接");
        }

        if (var1.contains("video")) {
            var2.append("视频");
        }

        if (var1.contains("timg")) {
            var2.append("图文");
        }

        if (var1.contains("text")) {
            var2.append("纯文本");
        }

        return var2.append("分享").toString();
    }

    public static String tencentEmpty(boolean var0) {
        String var1 = var0 ? SIMPLE : FULL;
        return "您使用的是" + var1 + "但是你的AndroidManifest配置不正确，或者配置的不是" + var1 + "的路径，请参照线上报错必看文档";
    }

    public static String urlEmpty(int var0) {
        StringBuilder var1 = new StringBuilder("错误:设置视频");
        if (var0 == 0) {
            var1.append("视频");
        }

        if (var0 == 1) {
            var1.append("音乐");
        }

        if (var0 == 2) {
            var1.append("网页");
        }

        return var1.append("分享类型，链接不能为空").toString();
    }

    public static class INTER {
        public static String NULLJAR = "[SIG10000]没有加入界面jar,请添加shareview";
        public static String MOREERROR = "[SIG10001]调用系统分享菜单错误，请换个手机测试";
        public static String CONTEXT_ERROR = "[SIG10002]ContextUtil中context为空，请确保程序初始化完毕，且程序中不要使用ContextUtil.setContext（null）方法";
        public static String HANDLERID = "handler id:";
        public static String PINFO = "分享平台相关信息:";

        public INTER() {
        }

        public static String getVersion(String var0) {
            return "平台(" + var0 + "):";
        }
    }

    public static class SHAREBOARD {
        public static String NULLNAME = "[SSB10000]获取平台名字为空，请检查代码";
        public static String NULLJAR = "[SSB10001]您的工程需要依赖v4或我们提供umeng_shareboard_widget包，请参考线上文档";

        public SHAREBOARD() {
        }
    }

    public static class CACHE {
        private static String id = "[SCC10000]";
        public static String CACHEFILE;
        public static String CLOSE;
        public static String SD_NOT_FOUNT;
        public static String MSANDCACHE_FOUNT;

        public CACHE() {
        }

        static {
            CACHEFILE = id + "缓存文件错误，请勿担心，不影响正常使用";
            CLOSE = id + "关闭流异常";
            SD_NOT_FOUNT = "[SCC10001]没有找到存储设备，存储图片";
            MSANDCACHE_FOUNT = "[SCC10002]mSandCache 找不到";
        }
    }

    public static class WX {
        public static String WX_NO_CALLBACK = "为什么微信没有回调？";
        public static String WX_CIRCLE_NOCONTENT = "为什么微信朋友圈链接不显示描述文字？";
        public static String WX_40125 = "为什么微信提示40125/invalid APPsecret？";
        public static String WX_HIT_PUSH = "为什么微信分享提示hit push hold？";
        public static String WX_NO_LINK = "为什么微信登陆提示该链接无法访问？";
        public static String WX_ERROR_SIGN = "为什么微信授权一直等待不能成功？";
        public static String WX_NOTHUMB_EMOJ = "微信表情分享没有设置缩略图";
        public static String WX_CIRCLE_NOT_SUPPORT_EMOJ = "微信朋友圈不支持表情分享...";
        public static String WX_CIRCLE_NOT_SUPPORT_MIN = "微信朋友圈不支持小程序分享...";
        public static String WX_NOACTIVITY = "[SX10000]您没有配置WXEntryActivity或没有配置不正确";
        public static String WX_ERRORACTIVITY = "[SX10000]请让您的WXEntryActivity继承com.umeng.socialize.weixin.view.WXCallbackActivity";
        public static String WX_ERRORMANIFEST = "[SX10000]请在AndroidManifest文件中配置WXEntryActivity";

        public WX() {
        }
    }

    public static class SINA {
        public static String SINA_THUMB_ERROR = "[SS10007]新浪多媒体分享，需要添加缩略图，请检查是否添加正确";
        public static String SINASTARTERROR = "[SS10008]启动新浪客户端错误";
        public static String SINA_AID_ERROR = "[SS10009]新浪AID获取失败";
        public static String SINA_MUL_IMAGE = "新浪多图分享必须加文字";
        public static String SINA_SIGN_ERROR = "[SS10001]新浪签名或回调地址错误，请去后台检查";
        public static String SINA_GET_ERROR = "[SS10011]返回结果为空，请检查网络，或token是否过期,请检查Appkey是否审核通过";
        public static String SINA_UPLOAD_ERROR = "[SS10012]新浪上传图片失败";
        public static String SINA_CALLBACKACTIVITY = "[SS10013]请在AndroidManifest文件中配置com.umeng.socialize.media.WBShareCallBackActivity";
        public static String SINA_WEBACTIVITY = "[SS10013]请在AndroidManifest文件中配置com.sina.weibo.sdk.web.WeiboSdkWebActivity";
        public static String SINA_TRANSACTIVITY = "[SS10013]请在AndroidManifest文件中配置com.sina.weibo.sdk.share.WbShareTransActivity";

        public SINA() {
        }

        public static String getError(String var0) {
            return "[SS10010]没有在AndroidManifest.xml中检测到" + var0 + "请添加" + var0;
        }
    }

    public static class TWITTER {
        public static String TWITTER_AUTH_FAIL = "为什么提示我们无法验证您的凭据？";
        public static String TWITTER_NULL_SESSION = "当前无活动会话，请确保已经完成授权申请。";

        public TWITTER() {
        }
    }

    public static class FACEBOOK {
        public static String FACEBOOK_GRAY = "为什么发布按钮为灰色？";
        public static String FACEBOOK_CANCEL_SUCCESS = "为什么facebook分享取消却回调成功？";
        public static String FACEBOOK_AUTH_FAIL = "为什么facebook分享失败？";
        public static String FACEBOOK_NET_SUPPORT = "facebook网页链接，缩略图必须为网络图片";
        public static String NOFACEBOOKACTIVITY = "[SF10000]您没有在AndroidManifest.xml中配置com.umeng.facebook.FacebookActivity";
        public static String NOFACEBOOKCONTENTPROVIDER = "[SF10000]您没有在AndroidManifest.xml中配置com.umeng.facebook.FacebookContentProvider";
        public static String NOMETA = "[SF10000]您没有在AndroidManifest中配置facebook的appid";
        public static String ERRORMETA = "[SF10000]没有找到facebook_app_id，facebook的id必须写在string文件中且名字必须用facebook_app_id";

        public FACEBOOK() {
        }
    }

    public static class QQ {
        public static String NOCALLBACK = "为什么我的QQ没有回调？";
        public static String ERRORCALLBACK = "为什么qq分享成功却回调取消？";
        public static String NOREPLY = "为什么qq分享成功却没有收到消息？";
        public static String NOPERMISSION = "为什么qq分享失败，缺少权限？";
        public static String TENCENTFAIL = "为什么qq tencent初始化失败？";
        public static String QQ100044 = "为什么qq提示100044?";
        public static String OPENIDANDTOKEN = "[SQ20009]initOpenidAndToken error";
        public static String ERRORINFO = "错误信息:";
        public static String QQ_TENCENT_ERROR = "[SQ20007]Tencent变量初始化失败，请检查你的app id跟AndroidManifest.xml文件中AuthActivity的scheme是否填写正确";
        public static String QQ_ERROR = "[SQ20007]mTencent信息不正确，请确认appid";
        public static String QQ_PERMISSION = "[SQ10004]QQ图片存储失败，请检查图片或者检查是否有读写权限";
        public static String QQ_MUSIC = "QQ分享音乐,除了设置播放音乐的url,还可以设置一个跳转的url,可以通过UMusic().setmTargetUrl(xxx)设定";
        public static String ERRORDATA = "[SQ10011]qq配置不正确，AndroidManifest中AuthActivity的data中要加入自己的qq应用id";
        public static String NOAPPID = "[SQ10000]qq配置不正确，没有检测到qq的id配置";
        public static String ADDPERMISSION = "[SQ10004]qq 权限配置不正确，缺少android.permission.WRITE_EXTERNAL_STORAGE";

        public QQ() {
        }

        public static String getError(String var0) {
            return "[SQ10011]没有在AndroidManifest.xml中检测到" + var0 + "请添加" + var0;
        }
    }

    public static class DROPBOX {
        public static String NEEDACTIVITY = "[SDB10001]dropbox请传入Activity";

        public DROPBOX() {
        }
    }

    public static class SHARE {
        public static String SHAREVIEWV = "shareview版本号:";
        public static String VERSION = "API版本号:";
        public static String INFO = "以下为分享的内容:";
        public static String SHARESTYLE = "分享类型:";
        public static String SHAREPLAT = "分享平台:";
        public static String SHARETEXT = "分享文本:";
        public static String URLIMAGE = "图片链接:";
        public static String LOCALIMAGE = "本地图片:";
        public static String URLTHUMB = "缩略图链接:";
        public static String LOCALTHUMB = "本地缩略图:";
        public static String VIDEOURL = "视频链接:";
        public static String VIDEOTITLE = "视频标题:";
        public static String VIDEODES = "视频描述:";
        public static String URLURL = "链接:";
        public static String URLTITLE = "url标题:";
        public static String URLDES = "url描述:";
        public static String MUSICURL = "音乐链接:";
        public static String MUSICTITLE = "音乐标题:";
        public static String MUSICDES = "音乐描述:";
        public static String FILENAME = "文件名:";
        public static String ERRORTYPE = "[SST10000]不支持的分享类型";
        public static String YNOTE_WARN = "有道云笔记只支持文本，图片，图文分享";
        public static String FOURSQUARE_WARN = "Foursquare只支持纯图片分享";
        public static String FLICKER_WARN = "Flickr只支持图片分享";
        public static String VERSION_NOT_SUPPORT = "[SST10001]分享内容有误，请查看log或当前使用的客户端版本不支持分享或授权";
        public static String SHARE_CONTENT_FAIL = "[SST10002]当前分享类型内容有误，缺少设置参数或内容不合规，请查看友盟log";
        public static String SHARE_TO = "分享到";
        public static String EMPTY_WEB_URL = "[SST10003]您分享的是网页类型但是网址为空";
        public static String EMPTY_TEXT = "[SST10004]分享的文本不能为空";
        public static String LONG_TEXT = "[SST10005]分享的文本过长";
        public static String LONG_URL = "[SST10006]分享的链接过长";
        public static String SHARE_STYLE = "分享类型--";
        public static String WEB_HTTP = "[SST10007]分享的链接需要以http开头";
        public static String CONTEXT_EMPTY = "请输入内容....";
        public static String CONTEXT_LONG = "超出最大字数限制....";

        public SHARE() {
        }
    }

    public static class AUTH {
        public static String ALL_AUTH_EVERY = "我想每次获取用户资料都授权怎么办？";
        public static String ALL_AUTH_TWICE = "为什么我每次获取用户资料都跳转两次授权界面？";
        public static String NOT_SUPPORT_PLATFROM = "[SA10000]平台不支持授权,无法完成操作";
        public static String AUTH_DENIED = "[SA10001]权限验证失败，请检查你的签名以及该平台Appkey权限.";
        public static String GET_FAIL_BY_EMPTY = "[SA10002]获取用户资料为空，uid为空或token为空";
        public static String DATA_EMPTY = "[SA10003]返回数据为空";

        public AUTH() {
        }
    }

    public static class NET {
        private static String id = "[SN10000]";
        public static String PARSEERROR;
        public static String TOOL;
        public static String IMAGEDOWN;
        public static String CLOSE;
        public static String CREATE;
        public static String NET_AGAIN_ERROR;
        public static String JSONNULL;
        public static String MACNULL;
        public static String POSTJSON;
        public static String JSONRESULT;
        public static String SHARESELFOK;
        public static String SHARESELFFAIL;
        public static String NET_INAVALIBLE;
        public static String BODYNULL;

        public NET() {
        }

        public static String getURLERROR(String var0) {
            return id + "baseurl=" + var0 + " 该url不正确";
        }

        public static String assertURL(String var0, String var1) {
            return "urlPath=" + var0 + "  SocializeNetUtils url=" + var1;
        }

        static {
            PARSEERROR = id + "返回字符串不是json，可能是由于服务器不通造成";
            TOOL = id + "工具类内的错误，不影响用户使用";
            IMAGEDOWN = "[SN10004]图片下载错误,请检查网址";
            CLOSE = id + "关闭流异常";
            CREATE = id + "构建response出错,不影响用户使用，但需要查明原因";
            NET_AGAIN_ERROR = "[SN10001]重定向问题：url和重定向url相同";
            JSONNULL = "[SN10002]后台返回json为空";
            MACNULL = "[SN10003]由于权限问题，没有获取到mac";
            POSTJSON = " post json：";
            JSONRESULT = " return json：";
            SHARESELFOK = " sharelog success";
            SHARESELFFAIL = " sharelog fail";
            NET_INAVALIBLE = "抱歉,您的网络不可用...";
            BODYNULL = "body is null";
        }
    }

    public static class CHECK {
        public static String ALL_NO_ONACTIVITY = "[SCH10000]您的activity中没有重写onActivityResult方法";
        public static String ACTIVITYNULL = "[SCH10001]传入Activity为空";
        public static String ROUTERNULL = "[SCH10002]Router为空请检查是否程序被杀死，或释放了我们所有的引用";
        public static String SDKVERSION = "您使用的SDK版本为：";
        public static String APPKEY_NOT_FOUND = "[SCH10003]请配置您的友盟Appkey";
        public static String NO_SUPPORT_INSTALL = "[SCH10004]该平台不支持查询安装";
        public static String NO_SUPPORT_SDKL = "[SCH10004]该平台不支持查询sdk支持";
        public static String NO_SUPPORT_AUTH = "[SCH10004]该平台不支持查询是否授权";
        public static String LISTENRNULL = "[SCH10005]监听器为空";
        public static String ALIPAYERROR = "[SCH10006]没有配置支付宝回调activity或配置不正确";
        public static String ALIPAYSUCCESS = "支付宝配置正确";
        public static String NOINT = "你的初始化未完成，请在Application的onCreate中调用 UMConfigure.init(this,appkey,channel,UMConfigure.DEVICE_TYPE_PHONE,\"\");/";
        public static String SHARE_CONTENT_IS_EMPTY = "分享对象不能为空!";

        public CHECK() {
        }

        public static String getActivityResult(int var0, int var1) {
            return "onActivityResult =" + var0 + "  resultCode=" + var1;
        }

        public static String noKey(String identifier) {
            return "您没有配置" + identifier + "的appkey,请在Application中调用PlatformConfig.setxxx进行配置";
        }

        public static String noJar(String identifier) {
            return "[SC10006]您没有配置 " + identifier + " 的jar包";
        }

        public static String checkSuccess(String identifier, String packageName) {
            return "[SC20008]您本次打包的签名:" + identifier + "\n包名:" + packageName;
        }
    }

    public static class IMAGE {
        private static String id = "[SI10000]";
        public static String BINARYTOFILE;
        public static String BITMAOTOBINARY;
        public static String CLOSE;
        public static String GET_FILE_FROM_BINARY;
        public static String TOOBIG;
        public static String FILE_TO_BINARY_ERROR;
        public static String GET_IMAGE_SCALE_ERROR;
        public static String READ_IMAGE_ERROR;
        public static String CHECK_FORMAT_ERROR;
        public static String THUMB_ERROR;
        public static String SHARECONTENT_THUMB_ERROR;
        public static String SHARECONTENT_IMAGE_ERROR;
        public static String IMAGEURL;
        public static String UNKNOW_UMIMAGE;

        public IMAGE() {
        }

        static {
            BINARYTOFILE = id + "字节流转文件错误";
            BITMAOTOBINARY = id + "bitmap转字节流错误";
            CLOSE = id + "流关闭错误";
            GET_FILE_FROM_BINARY = id + "字节流输出文件出错";
            TOOBIG = id + "加载文件过大";
            FILE_TO_BINARY_ERROR = id + "文件转入字节流，先要进行压缩出错";
            GET_IMAGE_SCALE_ERROR = id + "获取文件大小出错";
            READ_IMAGE_ERROR = id + "读取图片文件出错";
            CHECK_FORMAT_ERROR = id + "checkFormat出错";
            THUMB_ERROR = "[SI10001]您的原始图片太大,导致缩略图压缩过后还大于32KB,请将分享到微信的图片进行适当缩小.";
            SHARECONTENT_THUMB_ERROR = "[SI10002]缩略图参数有误，您没有设置缩略图，或者设置的缩略图内容有误，不能被解析";
            SHARECONTENT_IMAGE_ERROR = "[SI10003]图片参数有误，您没有设置缩略图，或者设置的缩略图内容有误，不能被解析";
            IMAGEURL = "图片下载链接:";
            UNKNOW_UMIMAGE = "[SI10004]不支持的UMImage构建类型，您传入的类型为:";
        }
    }
}
