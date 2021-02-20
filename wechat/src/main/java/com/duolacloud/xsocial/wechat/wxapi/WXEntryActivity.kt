package com.duolacloud.xsocial.wechat.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.duolacloud.xsocial.wechat.WechatHandler
import com.duolacloud.xsocial.core.XSocialAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler


open class WXEntryActivity : Activity(), IWXAPIEventHandler {
    private var mWechatHandler: WechatHandler? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mWechatHandler = XSocialAPI.getHandler<WechatHandler>("wechat:mobile")
        mWechatHandler?.wxApi?.handleIntent(intent, this)
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        mWechatHandler?.wxApi?.handleIntent(intent, this)
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    override fun onResp(resp: BaseResp?) {
        if (mWechatHandler != null && resp != null) {
            try {
                mWechatHandler?.wxEventHandler?.onResp(resp)
            } catch (e: Exception) {
                Log.e("xsocial-wechat", "", e)
            }
        }

        finish()
    }

    override fun onReq(req: BaseReq) {
        mWechatHandler?.wxEventHandler?.onReq(req)
        this.finish()
    }
}