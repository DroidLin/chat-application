import com.application.channel.core.model.DnsServerInitConfig
import com.application.channel.core.server.dns.DnsServer

/**
 * @author liuzhongao
 * @since 2024/5/19 21:59
 */
fun main() {
    val initConfig = DnsServerInitConfig(
        port = 53,
        dnsFilePath = "/Users/liuzhongao/Desktop/test_dns.ini"
    )
    val dnsServer = DnsServer(initConfig)
}