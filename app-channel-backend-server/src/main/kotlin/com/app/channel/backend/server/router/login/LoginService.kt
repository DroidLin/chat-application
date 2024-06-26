package com.app.channel.backend.server.router.login

import com.app.channel.backend.server.metadata.ApiResult
import com.app.channel.backend.server.metadata.toAccountVO
import com.app.channel.backend.server.metadata.toUserInfoVO
import com.app.channel.backend.server.room.dao.AccountDao
import com.app.channel.backend.server.room.dao.SessionInfoDao
import com.app.channel.backend.server.room.dao.UserInfoDao
import com.app.channel.backend.server.room.metadata.LocalAccount
import com.app.channel.backend.server.room.metadata.LocalSessionInfo
import com.app.channel.backend.server.room.metadata.LocalUserInfo
import com.app.channel.backend.server.router.INTERNAL_ERROR
import com.app.channel.backend.server.router.INVALID_PARAMETER
import com.app.channel.backend.server.router.PASSWORD_ERROR_OR_USER_NOT_EXIST
import com.app.channel.backend.server.router.USER_NOT_FOUNT
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

/**
 * @author liuzhongao
 * @since 2024/6/26 23:58
 */
class LoginService(
    private val userInfoDao: UserInfoDao,
    private val accountDao: AccountDao,
    private val sessionInfoDao: SessionInfoDao
) {

    fun Routing.route() {
        route(path = "/login") {
            route("/user") {
                get { this.call.handleUserLogin(this.call.parameters) }
                post { this.call.handleUserLogin(this.call.receiveParameters()) }
            }
            route("/register") {
                get { this.call.handleAccountRegister(this.call.parameters) }
                post { this.call.handleAccountRegister(this.call.receiveParameters()) }
            }
        }
    }

    private suspend fun ApplicationCall.handleUserLogin(parameters: Parameters) {
        val userName = parameters["userAccount"]
        val password = parameters["password"]
        if (userName.isNullOrEmpty() || password.isNullOrEmpty()) {
            respond(INVALID_PARAMETER)
            return
        }
        val account = this@LoginService.accountDao.fetchAccountById(accountId = userName)
        if (account == null) {
            respond(USER_NOT_FOUNT)
            return
        }
        if (account.passwordHash != password) {
            respond(PASSWORD_ERROR_OR_USER_NOT_EXIST)
            return
        }
        val userInfo = this@LoginService.userInfoDao.fetchUserInfoByAccount(account.accountId)
        if (userInfo == null) {
            respond(USER_NOT_FOUNT)
            return
        }
        val sessionInfo = this@LoginService.sessionInfoDao.fetchSessionInfo(account.accountId)
        if (sessionInfo == null) {
            respond(USER_NOT_FOUNT)
            return
        }
        respond(
            ApiResult.success(
                mapOf<String, Any?>(
                    "userInfo" to userInfo.toUserInfoVO(sessionInfo),
                    "account" to account.toAccountVO()
                )
            )
        )
    }

    private suspend fun ApplicationCall.handleAccountRegister(parameters: Parameters) {
        val userAccount = parameters["userAccount"]
        val password = parameters["password"]
        val userName = parameters["userName"]

        if (userAccount.isNullOrEmpty() || userName.isNullOrEmpty() || password.isNullOrEmpty()) {
            respond(INVALID_PARAMETER)
            return
        }

        val account = LocalAccount(
            accountId = userAccount,
            createTime = System.currentTimeMillis(),
            updateTime = System.currentTimeMillis(),
            passwordHash = password
        )
        this@LoginService.accountDao.insertAccount(account)
        this@LoginService.userInfoDao.insertUserInfo(LocalUserInfo(userId = 0, userAccount = userAccount, userName = userName))
        val userInfo = this@LoginService.userInfoDao.fetchUserInfoByAccount(account.accountId)
        if (userInfo == null) {
            respond(INTERNAL_ERROR)
            return
        }

        val sessionInfo = LocalSessionInfo(UUID.randomUUID().toString(), userInfo.userId, account.accountId)
        this@LoginService.sessionInfoDao.insertSessionInfo(sessionInfo)
        respond(ApiResult.success(null))
    }
}