package com.application.channel.core

import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface

val deviceAvailableIpAddress: InetAddress
    get() {
        val networkInterfaces = NetworkInterface.getNetworkInterfaces()
        val validNetworkAddress = networkInterfaces.toList().flatMap { networkInterface ->
            if (networkInterface.isValid) {
                val inetAddressList = networkInterface.inetAddresses.toList()
                inetAddressList.filter { inetAddress -> inetAddress.isValid }
            } else emptyList()
        }
        return validNetworkAddress.firstOrNull() ?: throw RuntimeException("no network address available.")
    }

private val NetworkInterface.isValid: Boolean
    get() = !this.isLoopback &&
            !this.isPointToPoint &&
            this.isUp &&
            !this.isVirtual

private val InetAddress.isValid: Boolean
    get() = this is Inet4Address && this.isSiteLocalAddress && !this.isLoopbackAddress