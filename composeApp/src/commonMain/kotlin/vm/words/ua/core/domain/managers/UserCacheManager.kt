package vm.words.ua.core.domain.managers

import kotlinx.coroutines.flow.StateFlow
import vm.words.ua.core.domain.models.Token
import vm.words.ua.core.domain.models.User

interface UserCacheManager {

    val user: User

    val token: Token

    val isExpired: Boolean


    val tokenFlow: StateFlow<Token?>

    fun saveUser(user: User)

    fun saveToken(token: Token)

    fun clear()
}