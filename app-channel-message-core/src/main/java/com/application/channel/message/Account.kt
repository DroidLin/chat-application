package com.application.channel.message

import org.json.JSONObject

/**
 * @author liuzhongao
 * @since 2024/5/6 23:22
 */
data class Account(val sessionId: String, val accountId: String) {

    companion object {
        @JvmField
        val default: Account = Account("", "")

        @JvmStatic
        fun parse(jsonObject: JSONObject?): Account? {
            jsonObject ?: return null
            val sessionId: String = jsonObject.optString("sessionId")
            val accountId: String = jsonObject.optString("accountId")
            if (sessionId.isEmpty() || accountId.isEmpty()) return null
            return Account(sessionId, accountId)
        }
    }
}
