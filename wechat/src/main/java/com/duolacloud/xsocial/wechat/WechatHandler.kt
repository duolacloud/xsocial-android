package com.duolacloud.xsocial.wechat;

import android.app.Activity
import android.content.Context
import com.duolacloud.xsocial.wechat.utils.WechatUrlUtil
import com.duolacloud.xsocial.core.XSocialAuthHandler
import com.duolacloud.xsocial.core.XSocialErrorCode
import com.duolacloud.xsocial.core.XSocialAuthListener
import com.duolacloud.xsocial.core.XSocialShareListener
import com.duolacloud.xsocial.core.utils.XSocialText
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

class WechatHandler: XSocialAuthHandler {
    var wxApi: IWXAPI? = null

    private var mSocialAuthListener: XSocialAuthListener? = null

    private var mSocialShareListener: XSocialShareListener? = null

    private var context: Context? = null

    val wxEventHandler: IWXAPIEventHandler by lazy {
        object: IWXAPIEventHandler {
            @Override
            override fun onResp(resp: BaseResp) {
                when(resp.type) {
                    1 -> this@WechatHandler.onAuthCallback(resp as SendAuth.Resp)
                    2 -> this@WechatHandler.onShareCallback(resp as com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Resp)
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

    override fun getIdentifier() = "wechat:mobile"

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

    override fun authorize(activity: Activity?, params: MutableMap<String, Any>?, listener: XSocialAuthListener?) {
        this.mSocialAuthListener = listener

        if (!isAppInstalled) {
            val enableQrCodeAuth = params?.get("enableQrCodeAuth") as Boolean? ?: false
            if (!enableQrCodeAuth) {
                activity?.runOnUiThread {
                    this@WechatHandler.mSocialAuthListener?.onError(identifier, Throwable(XSocialErrorCode.NotInstall.message))
                }
                return
            }

            val qrCodeAuthListener = object : OAuthListener {
                override fun onAuthFinish(errCode: OAuthErrCode, authCode: String?) {
                    if (errCode == OAuthErrCode.WechatAuth_Err_OK) {
                        listener?.onComplete(identifier, mapOf("code" to authCode))
                        return
                    }
                }

                override fun onAuthGotQrcode(qrCode: String?, p1: ByteArray) {
                    listener?.onComplete(identifier, mapOf(
                        "step" to "onAuthGotQrcode",
                        "qrCode" to qrCode
                    ))
                }

                override fun onQrcodeScanned() {
                    listener?.onComplete(identifier, mapOf(
                        "step" to "onQrcodeScanned"
                    ))
                }
            }

            var nonceStr = params?.get("nonceStr") as String?
            var timeStamp = params?.get("timeStamp") as String?
            var signature = params?.get("signature") as String?

            qrCodeAuth.auth(appId, sScope, nonceStr, timeStamp, signature, qrCodeAuthListener)
            return
        }

        val req = SendAuth.Req()
        req.scope = sScope
        req.state = "none"

        wxApi?.sendReq(req)
    }

    private fun onAuthCallback(resp: SendAuth.Resp) {
        if (resp.errCode == 0) {
            mSocialAuthListener?.onComplete(identifier, mapOf("code" to resp.code))
        } else if (resp.errCode == -2) {
            mSocialAuthListener?.onCancel(identifier)
        } else if (resp.errCode == -6) {
            mSocialAuthListener?.onError(
                identifier,
                Throwable(
                    XSocialErrorCode.AuthorizeFailed.message + XSocialText.errorWithUrl(
                        XSocialText.AUTH.AUTH_DENIED,
                        WechatUrlUtil.WX_ERROR_SIGN
                    )
                )
            )
        } else {
            val err = "weixin auth error (${resp.errCode}):${resp.errStr}"
            mSocialAuthListener?.onError(identifier, Throwable(XSocialErrorCode.AuthorizeFailed.message + err))
        }
    }

    protected fun onShareCallback(resp: SendMessageToWX.Resp) {
        when (resp.errCode) {
            -6 -> mSocialShareListener?.onError(
                identifier,
                Throwable(
                    XSocialErrorCode.ShareFailed.message + XSocialText.errorWithUrl(
                        XSocialText.AUTH.AUTH_DENIED,
                        WechatUrlUtil.WX_ERROR_SIGN
                    )
                )
            )
            -5 -> mSocialShareListener?.onError(
                identifier,
                Throwable(XSocialErrorCode.ShareFailed.message + XSocialText.SHARE.VERSION_NOT_SUPPORT)
            )
            -4 -> mSocialShareListener?.onError(
                identifier,
                Throwable("${XSocialErrorCode.ShareFailed.message} code: ${resp.errCode} msg: ${resp.errStr}")
            )
            -3, -1 -> mSocialShareListener?.onError(
                identifier,
                Throwable(XSocialErrorCode.ShareFailed.message + resp.errStr)
            )
            -2 -> mSocialShareListener?.onCancel(identifier)
            0 -> {
                // val map = mutableMapOf("uid" to resp.openId)
                mSocialShareListener?.onResult(identifier)
            }
            else -> mSocialShareListener?.onError(
                identifier,
                Throwable("${XSocialErrorCode.ShareFailed.message} code: ${resp.errCode} msg: ${resp.errStr}")
            )
        }
    }
}
