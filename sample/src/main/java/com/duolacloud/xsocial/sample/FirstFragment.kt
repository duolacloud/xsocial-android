package com.duolacloud.xsocial.sample

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.duolacloud.xsocial.core.XSocialAPI
import com.duolacloud.xsocial.core.XSocialAuthListener
import com.duolacloud.xsocial.core.XSocialShareListener
import com.duolacloud.xsocial.core.model.ShareContent
import com.duolacloud.xsocial.core.model.XImage
import com.duolacloud.xsocial.core.model.XWeb

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
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
        }


        view.findViewById<Button>(R.id.button_share).setOnClickListener {
            val shareContent = ShareContent.Builder()
                .subject("哈哈")
                .withMedia(XWeb.Builder()
                    .title("百度")
                    .url("https://www.baidu.com")
                    .thumb(XImage.Builder().data("https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1022846348,828107064&fm=26&gp=0.jpg").build())
                    .description("描述")
                    .build()
                )
                .build()

            // friend circle favorite
            XSocialAPI.share(this@FirstFragment.activity, "wechat:friend", shareContent, object: XSocialShareListener {
                override fun onCancel(identifier: String?) {
                    Log.i("a", "onCancel $identifier")
                }

                override fun onError(identifier: String?, e: Throwable?) {
                    Log.e("a", "onError $identifier", e)
                }

                override fun onResult(identifier: String?) {
                    Log.i("a", "onResult $identifier")
                }

                override fun onStart(identifier: String?) {
                    Log.i("a", "onStart $identifier")
                }
            })
        }
    }
}