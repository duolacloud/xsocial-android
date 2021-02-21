package com.duolacloud.xsocial.wechat.convertor;

import android.text.TextUtils
import com.duolacloud.xsocial.core.CompressListener
import com.duolacloud.xsocial.core.SimpleShareContentConvertor
import com.duolacloud.xsocial.core.model.*
import com.duolacloud.xsocial.core.utils.SocializeUtils
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.modelmsg.*

object WechatShareContentConvertor {
    fun convert(content: ShareContent, compressListener: CompressListener?): WXMediaMessage? {
        val shareType = content.shareType
        return if (shareType != 2 && shareType != 3) {
            when(shareType) {
                4 -> buildMusicParams(content, compressListener)
                16 -> buildUrlParams(content, compressListener)
                8 -> buildVideoParams(content, compressListener)
                64 -> buildEmojiParams(content, compressListener)
                32 -> buildFileParams(content)
                128 -> buildMinApp(content, compressListener)
                else -> buildTextParams(content)
            }
        } else {
            buildImageParams(content)
        }
    }

    private fun buildEmojiParams(content: ShareContent, compressListener: CompressListener?): WXMediaMessage? {
        val emoji = content.media as XEmoji?
        emoji ?: return null

        val path = emoji?.asFileImage()?.toString()

        val wxEmojiObject = WXEmojiObject()
        wxEmojiObject.emojiPath = path
        val msg = WXMediaMessage().apply {
            mediaObject = wxEmojiObject
            thumbData = SimpleShareContentConvertor.objectSetThumb(emoji, compressListener)
        }
        return msg;
    }

    private fun buildMusicParams(content: ShareContent, compressListener: CompressListener?): WXMediaMessage? {
        val music = content.media as XMusic?
        music ?: return null

        val musicObj = WXMusicObject()
        musicObj.musicUrl = SimpleShareContentConvertor.getMusicTargetUrl(music)
        musicObj.musicDataUrl = music.toUrl()
        if (!TextUtils.isEmpty(music.lowBandDataUrl)) {
            musicObj.musicLowBandDataUrl = music.lowBandDataUrl
        }

        if (!TextUtils.isEmpty(music.lowBandUrl)) {
            musicObj.musicLowBandUrl = music.lowBandUrl
        }

        val msg = WXMediaMessage()
        msg.mediaObject = musicObj
        msg.title = SimpleShareContentConvertor.objectSetTitle(music)
        msg.description = SimpleShareContentConvertor.objectSetDescription(music)
        msg.mediaObject = musicObj
        msg.thumbData = SimpleShareContentConvertor.objectSetThumb(music, compressListener)
        return msg
    }

    private fun buildFileParams(content: ShareContent): WXMediaMessage? {
        content.file ?: return null

        val textObj = WXFileObject()
        textObj.fileData = SocializeUtils.file2bytes(content.file)
        val msg = WXMediaMessage()
        msg.mediaObject = textObj
        msg.description = content.text
        msg.title = content.subject
        return msg
    }

    private fun buildMinApp(content: ShareContent, compressListener: CompressListener?): WXMediaMessage? {
        var min = content.media as XMin?
        min ?: return null

        val minObj = WXMiniProgramObject()
        minObj.webpageUrl = min.toUrl()
        minObj.userName = min.userName
        minObj.path = min.path
        minObj.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;

        val msg = WXMediaMessage()
        msg.title = SimpleShareContentConvertor.objectSetTitle(min)
        msg.description = SimpleShareContentConvertor.objectSetDescription(min)
        msg.thumbData = SimpleShareContentConvertor.objectSetMInAppThumb(min, compressListener)
        msg.mediaObject = minObj
        return msg
    }

    private fun buildTextParams(content: ShareContent): WXMediaMessage? {
        val textObj = WXTextObject()
        textObj.text = SimpleShareContentConvertor.objectSetText(content.text)

        val msg = WXMediaMessage()
        msg.mediaObject = textObj
        msg.description = SimpleShareContentConvertor.objectSetText(content.text, 1024)
        return msg
    }

    private fun buildImageParams(content: ShareContent): WXMediaMessage? {
        val img = content.media as XImage?
        img ?: return null

        val imgObj = WXImageObject()

        val msg = WXMediaMessage()
        imgObj.imageData = img.asBinImage()
        if (SimpleShareContentConvertor.canFileValid(img)) {
            imgObj.imagePath = img.asFileImage().toString();
            imgObj.imageData = null;
        } else {
            imgObj.imageData = SimpleShareContentConvertor.getStrictImageData(img);
        }

        msg.thumbData = SimpleShareContentConvertor.getImageThumb(img)
        msg.mediaObject = imgObj
        return msg
    }

    private fun buildVideoParams(content: ShareContent, compressListener: CompressListener?): WXMediaMessage? {
        val video = content.media as XVideo?
        video ?: return null

        val videoObj = WXVideoObject()
        videoObj.videoUrl = video.toUrl()
        if (!TextUtils.isEmpty(video.lowBandUrl)) {
            videoObj.videoLowBandUrl = video.lowBandUrl
        }

        val msg = WXMediaMessage()
        msg.mediaObject = videoObj
        msg.title = SimpleShareContentConvertor.objectSetTitle(video)
        msg.description = SimpleShareContentConvertor.objectSetDescription(video)
        msg.thumbData = SimpleShareContentConvertor.objectSetThumb(video, compressListener)
        return msg
    }

    private fun buildUrlParams(content: ShareContent, compressListener: CompressListener?): WXMediaMessage? {
        val web = content.media as XWeb?
        web ?: return null;

        val webObj = WXWebpageObject()
        webObj.webpageUrl = web.toUrl()
        val msg = WXMediaMessage()
        msg.title = SimpleShareContentConvertor.objectSetTitle(web)
        msg.description = SimpleShareContentConvertor.objectSetDescription(web);
        msg.mediaObject = webObj
        msg.thumbData = SimpleShareContentConvertor.objectSetThumb(web, compressListener)
        return msg;
    }
}
