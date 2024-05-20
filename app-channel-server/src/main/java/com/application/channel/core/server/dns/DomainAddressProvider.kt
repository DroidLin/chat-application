package com.application.channel.core.server.dns

import java.io.File
import java.io.FileInputStream
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author liuzhongao
 * @since 2024/5/19 21:09
 */
interface DomainAddressProvider {

    fun getDomainIPAddress(domain: String): ByteArray?
}

fun DomainAddressProvider(filePath: String): DomainAddressProvider {
    return IniFileDomainAddressProvider(filePath)
}

private class IniFileDomainAddressProvider(filePath: String) : DomainAddressProvider {

    private val logger = Logger.getLogger("AddressProvider", "")
    private val _innerMap = ConcurrentHashMap<String, ByteArray>()
    private val file = File(filePath)

    private var lastModifiedTimestamp: Long = this.file.lastModified()

    init {
        this.checkModification(force = true)
    }

    override fun getDomainIPAddress(domain: String): ByteArray? {
        this.checkModification(force = false)
        this.logger.log(Level.INFO, "access ipAddress for: $domain")
        return this._innerMap[domain]
    }

    private fun checkModification(force: Boolean = false) {
        if (force || this.checkIsModified()) {
            synchronized(this) {
                if (force || this.checkIsModified()) {
                    val map = this.parseAddress(domainAddressPairList = this.read())
                        .associate { pair -> pair.domain to pair.ipAddress }
                    this._innerMap.clear()
                    this._innerMap.putAll(map)
                    this.lastModifiedTimestamp = this.file.lastModified()
                }
            }
        }
    }

    private fun checkIsModified(): Boolean = this.lastModifiedTimestamp != this.file.lastModified()

    private fun read(): List<String> {
        return if (this.file.isFile && this.file.exists()) {
            FileInputStream(this.file).use { inputStream ->
                inputStream.bufferedReader().lineSequence().toList()
            }
        } else emptyList<String>()
    }

    private fun parseAddress(domainAddressPairList: List<String>): List<DomainAddressPair> {
        this.logger.log(Level.INFO, domainAddressPairList.joinToString())
        return domainAddressPairList.mapNotNull { value -> this.parseAddress(value) }
    }

    private fun parseAddress(value: String): DomainAddressPair? {
        return if (value.contains('=')) {
            val equalsSplitList = value.split('=')
            return DomainAddressPair(
                domain = "${equalsSplitList[0]}.",
                ipAddress = equalsSplitList[1].split('.').mapNotNull { it.toIntOrNull()?.toByte() }.toByteArray()
            )
        } else null
    }

    private data class DomainAddressPair(val domain: String, val ipAddress: ByteArray) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as DomainAddressPair

            if (domain != other.domain) return false
            if (!ipAddress.contentEquals(other.ipAddress)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = domain.hashCode()
            result = 31 * result + ipAddress.contentHashCode()
            return result
        }
    }
}