package vm.words.ua.subscribes.domain.managers

import vm.words.ua.subscribes.domain.models.Cache
import vm.words.ua.subscribes.domain.models.Subscribe

interface SubscribeCacheManager {
    suspend fun fetch(): Subscribe?

    suspend fun cache(): Cache<Subscribe?>

    suspend fun isActiveSubscribe(): Boolean

    suspend fun update(expirationDate: kotlinx.datetime.Instant)
}

