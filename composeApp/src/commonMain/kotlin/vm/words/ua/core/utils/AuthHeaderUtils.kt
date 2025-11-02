package vm.words.ua.core.utils

import vm.words.ua.core.domain.managers.UserCacheManager

fun UserCacheManager.toPair(): Pair<String, String> {
    return "Authorization" to "Bearer ${token.value}"
}


