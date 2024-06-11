package com.application.channel.message

import com.application.channel.message.meta.LoginResultMessage
import javax.inject.Inject

/**
 * @author liuzhongao
 * @since 2024/5/30 00:19
 */
interface Authorization {

    val contact: Account?

    val isLogin: Boolean

    fun setupContact(account: Account)

    fun setLoginResultMessage(loginResultMessage: LoginResultMessage?)
}

internal class AuthorizationImpl @Inject constructor() : Authorization {

    private var _contact: Account? = null
    private var _loginResultMessage: LoginResultMessage? = null

    override val contact: Account?
        get() = this._contact

    override val isLogin: Boolean
        get() = this._loginResultMessage?.receiver == this._contact

    override fun setLoginResultMessage(loginResultMessage: LoginResultMessage?) {
        this._loginResultMessage = loginResultMessage
    }

    override fun setupContact(account: Account) {
        this._contact = account
    }

}