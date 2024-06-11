import com.application.channel.message.decrypt
import com.application.channel.message.encrypt

/**
 * @author liuzhongao
 * @since 2024/6/1 11:03
 */


fun main() {
    val key = "lza91.ooaiqoweqw".toByteArray()
    val data = "Hello world!".toByteArray()

    val encryptResult = encrypt(data, key)
    println(encryptResult.decodeToString())
    val decryptResult = decrypt(encryptResult, key)
    println(decryptResult.decodeToString())
    println(data.contentEquals(decryptResult))
}