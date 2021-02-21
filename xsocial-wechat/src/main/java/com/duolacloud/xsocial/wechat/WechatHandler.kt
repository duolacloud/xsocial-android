package com.duolacloud.xsocial.wechat;

import android.app.Activity
import android.content.Context
import com.duolacloud.xsocial.core.*
import com.duolacloud.xsocial.core.model.ShareContent
import com.duolacloud.xsocial.core.utils.XSocialText
import com.duolacloud.xsocial.wechat.convertor.WechatShareContentConvertor
import com.duolacloud.xsocial.wechat.utils.WechatConsts
import com.duolacloud.xsocial.wechat.utils.WechatUrlUtil
import com.tencent.mm.opensdk.diffdev.DiffDevOAuthFactory
import com.tencent.mm.opensdk.diffdev.OAuthErrCode
import com.tencent.mm.opensdk.diffdev.OAuthListener
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory

const val sScope = "snsapi_userinfo,snsapi_friend,snsapi_message"

class MobileHandler: XSocialBaseHandler() {
    var wxApi: IWXAPI? = null

    private var mSocialAuthListener: XSocialAuthListener? = null

    private var mSocialShareListener: XSocialShareListener? = null

    private var context: Context? = null

    val wxEventHandler: IWXAPIEventHandler by lazy {
        object: IWXAPIEventHandler {
            @Override
            override fun onResp(resp: BaseResp) {
                when(resp.type) {
                    1 -> this@MobileHandler.onAuthCallback(resp as SendAuth.Resp)
                    2 -> this@MobileHandler.onShareCallback(resp as com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Resp)
                }
            }

            @Override
            override fun onReq(req: BaseReq?) {
            }
        };
    }

    private val qrCodeAuth by lazy {
        DiffDevOAuthFactory.getDiffDevOAuth()
    }

    private var registered: Boolean = false
        get() = field

    private var appId: String? = null

    override fun getPlatform() = "wechat"

    @Override
    override fun isAppInstalled(): Boolean = wxApi?.isWXAppInstalled ?: false

    @Override
    override fun init(context: Context?, params: MutableMap<String, String>?) {
        this.context = context

        if (wxApi != null) return

        appId = params?.get("appId")

        val api = WXAPIFactory.createWXAPI(context?.applicationContext, appId)
        registered = api.registerApp(appId)
        wxApi = api
    }

    @Override
    override fun authorize(activity: Activity?, way: String, params: MutableMap<String, Any>?, listener: XSocialAuthListener?) {
        this.mSocialAuthListener = listener

        if (way == "qrcode") {
            val qrCodeAuthListener = object : OAuthListener {
                override fun onAuthFinish(errCode: OAuthErrCode, authCode: String?) {
                    if (errCode == OAuthErrCode.WechatAuth_Err_OK) {
                        listener?.onComplete(platform, mapOf("code" to authCode))
                        return
                    }
                }

                override fun onAuthGotQrcode(qrCode: String?, p1: ByteArray) {
                    listener?.onComplete(
                        platform, mapOf(
                            "step" to "onAuthGotQrcode",
                            "qrCode" to qrCode
                        ))
                }

                override fun onQrcodeScanned() {
                    listener?.onComplete(
                        platform, mapOf(
                            "step" to "onQrcodeScanned"
                        ))
                }
            }

            var nonceStr = params?.get("nonceStr") as String?
            var timeStamp = params?.get("timeStamp") as String?
            var signature = params?.get("signature") as String?

            qrCodeAuth.auth(appId, sScope, nonceStr, timeStamp, signature, qrCodeAuthListener)
        }

        // 默认用 mobile 登录方式

        if (!isAppInstalled) {
            activity?.runOnUiThread {
                this@MobileHandler.mSocialAuthListener?.onError(platform, Throwable(XSocialErrorCode.NotInstall.message))
            }
            return
        }

        val req = SendAuth.Req()
        req.scope = sScope
        req.state = "none"

        wxApi?.sendReq(req)
    }

    private fun onAuthCallback(resp: SendAuth.Resp) {
        if (resp.errCode == 0) {
            mSocialAuthListener?.onComplete(platform, mapOf("code" to resp.code))
        } else if (resp.errCode == -2) {
            mSocialAuthListener?.onCancel(platform)
        } else if (resp.errCode == -6) {
            mSocialAuthListener?.onError(
                platform,
                Throwable(
                    XSocialErrorCode.AuthorizeFailed.message + XSocialText.errorWithUrl(
                        XSocialText.AUTH.AUTH_DENIED,
                        WechatUrlUtil.WX_ERROR_SIGN
                    )
                )
            )
        } else {
            val err = "weixin auth error (${resp.errCode}):${resp.errStr}"
            mSocialAuthListener?.onError(platform, Throwable(XSocialErrorCode.AuthorizeFailed.message + err))
        }
    }

    override fun share(activity: Activity?, way: String, shareContent: ShareContent?, listener: XSocialShareListener?): Boolean {
        if (shareContent == null) {
            listener?.onError("${platform}:${way}", Throwable(XSocialErrorCode.ShareDataNil.message))
            return false
        }

        if (!isAppInstalled) {
            activity?.runOnUiThread{
                listener?.onError("${platform}:${way}", Throwable(XSocialErrorCode.NotInstall.message))
            }
            return false
        }

        if (!isAbleShareEmoji(way, shareContent)) {
            activity?.runOnUiThread {
                listener?.onError("${platform}:${way}", Throwable(XSocialErrorCode.ShareDataTypeIllegal.message + WechatConsts.WX_CIRCLE_NOT_SUPPORT_EMOJ))
            }
            return false
        } else if (!this.isAbleShareMin(way, shareContent)) {
            activity?.runOnUiThread {
                listener?.onError("${platform}:${way}", Throwable(XSocialErrorCode.ShareDataTypeIllegal.message + WechatConsts.WX_CIRCLE_NOT_SUPPORT_MIN))
            }
            return false
        } else {
            mSocialShareListener = listener
            return shareTo(activity, way, shareContent)
        }
    }

    private fun shareTo(activity: Activity?, way: String, shareContent: ShareContent): Boolean {
        val req = SendMessageToWX.Req()
        req.transaction = this.buildTransaction(SimpleShareContentConvertor.getShareTypeStr(shareContent.shareType))
        req.message = WechatShareContentConvertor.convert(shareContent, mShareConfig?.compressListener)
        when (way) {
            "weixin" -> req.scene = 0
            "circle" -> req.scene = 1
            "favorite" -> req.scene = 2
            else -> req.scene = 2
        }

        return if (req.message == null) {
            activity?.runOnUiThread{
                this@MobileHandler.mSocialShareListener?.onError(
                    "${platform}:${way}",
                    Throwable(XSocialErrorCode.UnKnowCode.message.toString() + "message = null")
                )
            }
            false
        } else if (req.message.mediaObject == null) {
            activity?.runOnUiThread {
                this@MobileHandler.mSocialShareListener?.onError(
                    "${platform}:${way}",
                    Throwable(XSocialErrorCode.UnKnowCode.message.toString() + "mediaobject = null")
                )
            }
            false
        } else {
            val r = wxApi?.sendReq(req) ?: false
            if (!r) {
                activity?.runOnUiThread {
                    this@MobileHandler.mSocialShareListener?.onError(
                        "${platform}:${way}",
                        Throwable(XSocialErrorCode.UnKnowCode.message + XSocialText.SHARE.SHARE_CONTENT_FAIL)
                    )
                }
            }
            r
        }
    }

    private fun isAbleShareMin(way: String, shareContent: ShareContent): Boolean {
        return shareContent.shareType !== 128 || way !== "wechat_circle" && way !== "wechat_favorite"
    }

    private fun isAbleShareEmoji(
        way: String,
        shareContent: ShareContent
    ): Boolean {
        return shareContent.shareType !== 64 || way !== "wechat_circle" && way !== "wechat_favorite"
    }

    protected fun onShareCallback(resp: SendMessageToWX.Resp) {
        when (resp.errCode) {
            -6 -> mSocialShareListener?.onError(
                platform,
                Throwable(
                    XSocialErrorCode.ShareFailed.message + XSocialText.errorWithUrl(
                        XSocialText.AUTH.AUTH_DENIED,
                        WechatUrlUtil.WX_ERROR_SIGN
                    )
                )
            )
            -5 -> mSocialShareListener?.onError(
                platform,
                Throwable(XSocialErrorCode.ShareFailed.message + XSocialText.SHARE.VERSION_NOT_SUPPORT)
            )
            -4 -> mSocialShareListener?.onError(
                platform,
                Throwable("${XSocialErrorCode.ShareFailed.message} code: ${resp.errCode} msg: ${resp.errStr}")
            )
            -3, -1 -> mSocialShareListener?.onError(
                platform,
                Throwable(XSocialErrorCode.ShareFailed.message + resp.errStr)
            )
            -2 -> mSocialShareListener?.onCancel(platform)
            0 -> {
                // val map = mutableMapOf("uid" to resp.openId)
                mSocialShareListener?.onResult(platform)
            }
            else -> mSocialShareListener?.onError(
                platform,
                Throwable("${XSocialErrorCode.ShareFailed.message} code: ${resp.errCode} msg: ${resp.errStr}")
            )
        }
    }

    private fun buildTransaction(type: String?): String? {
        return if (type == null) System.currentTimeMillis()
            .toString() else type + System.currentTimeMillis()
    }
}
