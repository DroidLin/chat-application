package com.application.channel.message.meta

import com.application.channel.message.*
import com.application.channel.message.meta.Messages.decodeToBasicMessage
import org.json.JSONObject

/**
 * @author liuzhongao
 * @since 2024/5/30 21:08
 */
class ImageMessage internal constructor(
    override val uuid: String,
    override val sessionType: SessionType,
    override val sender: Account,
    override val receiver: Account,
    override val timestamp: Long,
    override val ext: Map<String, Any?>
) : Message {

    constructor(message: Message) : this(
        uuid = message.uuid,
        sessionType = message.sessionType,
        sender = message.sender,
        receiver = message.receiver,
        timestamp = message.timestamp,
        ext = message.ext
    )

    override val msgType: MsgType = MsgType.Image

    val imageContent: ImageContent = run {
        val content = this.ext[KEY_CONTENT] as? Map<*, *>
        val cover = content?.get(KEY_COVER) as? String
        val images = (content?.get(KEY_IMAGES) as? List<Any?>)
            ?.mapNotNull { imageMap -> (imageMap as? Map<*, *>)?.get(KEY_URL) as? String }
            ?.map { url -> Image(url) }
        ImageContent(cover, images)
    }

    class Parser : MessageParser {
        override fun parse(jsonObject: JSONObject): Message? {
            val basicMessage = jsonObject.decodeToBasicMessage() ?: return null
            return this.parse(basicMessage)
        }

        override fun parse(message: Message): Message? {
            if (message.msgType != MsgType.Image) return null
            return ImageMessage(message)
        }
    }
}


data class ImageContent(
    val cover: String? = null,
    val images: List<Image>? = null,
)

data class Image(val url: String)