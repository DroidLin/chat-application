package com.app.channel.backend.server.service.domain

import com.app.channel.backend.server.room.dao.AccountDao
import com.app.channel.backend.server.room.metadata.LocalAccount
import org.springframework.stereotype.Component

/**
 * @author liuzhongao
 * @since 2024/8/2 17:27
 */
@Component
class AccountDomainService(
    private val accountDao: AccountDao,
) {

    suspend fun createAccount(accountId: String, passwordHash: String): LocalAccount {
        val account = LocalAccount(
            accountId = accountId,
            createTime = System.currentTimeMillis(),
            updateTime = System.currentTimeMillis(),
            passwordHash = passwordHash
        )
        this.accountDao.insertAccount(localAccount = account)
        return account
    }
}