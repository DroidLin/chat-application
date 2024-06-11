import com.application.channel.message.map
import org.json.JSONObject

/**
 * @author liuzhongao
 * @since 2024/6/1 14:39
 */

fun main() {
    val sourceMap = mapOf(
        "type" to 1,
        "ext" to mapOf(
            "content" to ""
        ),
        "receiver" to mapOf(
            "sessionId" to "",
            "accountId" to "",
        ),
        "sender" to mapOf(
            "sessionId" to "",
            "accountId" to "",
        ),
        "timestamp" to System.currentTimeMillis()
    )
    val transformMap = JSONObject(sourceMap).map
    println(sourceMap == transformMap)
}