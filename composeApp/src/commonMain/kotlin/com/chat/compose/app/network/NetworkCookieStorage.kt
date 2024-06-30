package com.chat.compose.app.network

import com.application.channel.message.map
import com.chat.compose.app.storage.MutableMapStorage
import com.chat.compose.app.util.PreferenceCenter
import io.ktor.client.plugins.cookies.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.util.date.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.json.JSONObject
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.min

/**
 * @author liuzhongao
 * @since 2024/6/30 13:21
 */
class NetworkCookieStorage : CookiesStorage {

    private val cookiesPreference by PreferenceCenter.cookieStorage

    private val container: MutableList<Cookie> = mutableListOf()
    private val oldestCookie: AtomicLong = AtomicLong(0L)
    private val mutex = Mutex()

    init {
        cookiesPreference.all.values.forEach { value ->
            if (value !is String) return@forEach
            val cookie = parseCookie(value)
            if (cookie != null) {
                container.add(cookie)
            }
        }
    }

    override suspend fun get(requestUrl: Url): List<Cookie> = mutex.withLock {
        val now = getTimeMillis()
        if (now >= oldestCookie.get()) cleanup(now)

        return@withLock container.filter { it.matches(requestUrl) }
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie): Unit = mutex.withLock {
        with(cookie) {
            if (name.isBlank()) return@withLock
        }

        container.removeAll { it.name == cookie.name && it.matches(requestUrl) }
        container.add(cookie.fillDefaults(requestUrl))
        cookie.expires?.timestamp?.let { expires ->
            if (oldestCookie.get() > expires) {
                oldestCookie.set(expires)
            }
        }
        updateCookies()
    }

    override fun close() {
    }

    private fun cleanup(timestamp: Long) {
        container.removeAll { cookie ->
            val expires = cookie.expires?.timestamp ?: return@removeAll false
            expires < timestamp
        }

        val newOldest = container.fold(Long.MAX_VALUE) { acc, cookie ->
            cookie.expires?.timestamp?.let { min(acc, it) } ?: acc
        }

        oldestCookie.set(newOldest)
        updateCookies()
    }

    private fun updateCookies() {
        container.forEach { cookie ->
            cookiesPreference.put(cookie.key, cookie.toJsonString())
        }
        cookiesPreference.flush()
    }
}

internal fun Cookie.matches(requestUrl: Url): Boolean {
    val domain = domain?.toLowerCasePreservingASCIIRules()?.trimStart('.')
        ?: error("Domain field should have the default value")

    val path = with(path) {
        val current = path ?: error("Path field should have the default value")
        if (current.endsWith('/')) current else "$path/"
    }

    val host = requestUrl.host.toLowerCasePreservingASCIIRules()
    val requestPath = let {
        val pathInRequest = requestUrl.encodedPath
        if (pathInRequest.endsWith('/')) pathInRequest else "$pathInRequest/"
    }

    if (host != domain && (hostIsIp(host) || !host.endsWith(".$domain"))) {
        return false
    }

    if (path != "/" &&
        requestPath != path &&
        !requestPath.startsWith(path)
    ) {
        return false
    }

    return !(secure && !requestUrl.protocol.isSecure())
}

internal fun Cookie.fillDefaults(requestUrl: Url): Cookie {
    var result = this

    if (result.path?.startsWith("/") != true) {
        result = result.copy(path = requestUrl.encodedPath)
    }

    if (result.domain.isNullOrBlank()) {
        result = result.copy(domain = requestUrl.host)
    }

    return result
}

internal val Cookie.key: String
    get() = "${this.name}/${this.domain}"

internal fun Cookie.toJsonString(): String {
    val jsonObject = JSONObject()

    jsonObject.put("name", this.name)
    jsonObject.put("value", this.value)
    jsonObject.put("maxAge", this.maxAge)
    jsonObject.put("expires", this.expires?.timestamp ?: 0)
    jsonObject.put("domain", this.domain)
    jsonObject.put("path", this.path)
    jsonObject.put("secure", this.secure)
    jsonObject.put("httpOnly", this.httpOnly)
    jsonObject.put("extensions", JSONObject(this.extensions))

    return jsonObject.toString()
}

fun parseCookie(jsonString: String?): Cookie? {
    if (jsonString.isNullOrBlank()) return null
    val jsonObject = runCatching { JSONObject(jsonString) }.getOrNull() ?: return null

    val name = jsonObject.optString("name")
    val value = jsonObject.optString("value")
    val maxAge = jsonObject.optInt("maxAge")
    val expires = jsonObject.optLong("expires")
    val domain = jsonObject.optString("domain")
    val path = jsonObject.optString("path")
    val secure = jsonObject.optBoolean("secure")
    val httpOnly = jsonObject.optBoolean("httpOnly")
    val extensions = jsonObject.optJSONObject("extensions")?.map ?: emptyMap()

    return Cookie(
        name = name,
        value = value,
        maxAge = maxAge,
        expires = GMTDate(expires),
        domain = domain,
        secure = secure,
        httpOnly = httpOnly,
        extensions = extensions as Map<String, String?>,
        path = path,
    )
}