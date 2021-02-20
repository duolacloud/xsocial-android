## XSocial 社会化插件

可以结合 xauth, auth0, Authing 等IDaaS 身份云服务进行集成

Umeng SDK 有安全隐患.
Umeng SDK 需要 app把 appId, appSecret都暴露在app前端代码中，并且 token 交换的 accessToken 也有可能被劫持.

### 使用示范
1. 初始化
```kotlin
XSocialAPI.init(XSocialOptions.Builder(this)
            .registerSocialConnection("wechat:mobile", mapOf(
                "appId" to "wxf90dd55bad731b4f"
            ))
            .build()
        )
```
2. 社会化登录获取 token
```kotlin
XSocialAPI.authorize(this@FirstFragment.activity, "wechat:mobile", null, object: XSocialAuthListener {
    override fun onCancel(identifier: String?) {
        Log.i("a", "onCancel ${identifier}")
    }

    override fun onError(identifier: String?, e: Throwable?) {
        Log.e("a", "onError ${identifier}", e)
    }

    override fun onStart(identifier: String?) {
        Log.i("a", "onStart ${identifier}")
    }
   
    override fun onComplete(
        identifier: String?,
        data: MutableMap<String, String>?
    ) {
        Log.i("a", "${data}")
    }
})
```

### TODO
* 实现微信社会化功能(分享)
* 实现支付社会化功能(登录，分享)
* 实现抖音社会化功能(登录, 分享)