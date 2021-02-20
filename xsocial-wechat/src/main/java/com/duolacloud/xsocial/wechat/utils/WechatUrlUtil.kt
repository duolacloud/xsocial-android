package com.duolacloud.xsocial.wechat.utils

object WechatUrlUtil {
    val WX_NO_LINK = makeUrl("66786")
    val WX_CIRCLE_NOCONTENT = makeUrl("66797")
    val WX_40125 = makeUrl("66802")
    val WX_NO_CALLBACK = makeUrl("66791")
    val WX_ERROR_SIGN = makeUrl("66787")
    val WX_HIT_PUSH = makeUrl("66795")

    fun makeUrl(code: String): String = "https://developer.duolacloud.com/docs/xsocial/detail/${code}?channel=sdk"
}