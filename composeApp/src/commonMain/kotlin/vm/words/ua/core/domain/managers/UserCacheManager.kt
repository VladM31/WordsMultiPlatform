package vm.words.ua.core.domain.managers

import vm.words.ua.core.domain.models.Token
import vm.words.ua.core.domain.models.User

interface UserCacheManager {

    val user: User

    val token: Token

    val isExpired: Boolean

    fun saveUser(user: User)

    fun saveToken(token: Token)

    fun clear()
}