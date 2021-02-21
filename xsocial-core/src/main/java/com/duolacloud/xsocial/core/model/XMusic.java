package com.duolacloud.xsocial.core.model;

import android.os.Parcel
import com.duolacloud.xsocial.core.utils.SocializeProtocolConstants

class XMusic: BaseMediaObject {
    // f
    private var lowBandDataUrl: String? = null
        get() = field
        set(value) { field = value }

    // g
    var highBandDataUrl: String? = null
        get() = field
        set(value) { field = value }

    // h
    var h5Url: String? = null
        get() = field
        set(value) { field = value }

    // i
    var lowBandUrl: String? = null
        get() = field
        set(value) { field = value }

    // j
    var duration: Int? = null
        get() = field
        set(value) { field = value }

    // k
    var targetUrl: String? = null
        get() = field
        set(value) { field = value }


    constructor(v: String): super(v)

    fun getMediaType(): XMediaObject.MediaType = XMediaObject.MediaType.MUSIC

    protected constructor(parcel: Parcel): super(parcel)

    fun toUrlExtraParams(): Map<String, Any> {
        val extraParams = mutableMapOf<String, Any>()
        if (this.isUrlMedia) {
            extraParams[SocializeProtocolConstants.PROTOCOL_KEY_FURL] = this.url
            extraParams[SocializeProtocolConstants.PROTOCOL_KEY_FTYPE] = this.getMediaType()
            extraParams[SocializeProtocolConstants.PROTOCOL_KEY_TITLE] = this.title
        }

        return extraParams
    }

    fun toByte(): ByteArray? = this.thumb?.toByte()

    override fun toString() = "UMusic [title=${title}, media_url=${url}, title=${title}, qzone_thumb=]"

    override fun getThumbImage(): XImage? = thumb
}
