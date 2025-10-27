package vm.words.ua.subscribes.domain.managers.impl

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import vm.words.ua.subscribes.domain.managers.SubscribeCacheManager
import vm.words.ua.subscribes.domain.models.Cache
import vm.words.ua.subscribes.domain.models.Subscribe
import vm.words.ua.subscribes.net.clients.SubscribeClient

/**
 * Реализация SubscribeCacheManager в памяти (без файлов)
 */
class InMemorySubscribeCacheManager(
    private val subscribeClient: SubscribeClient
) : SubscribeCacheManager {

    private var cachedSubscribe: Subscribe? = null
    private var cacheDate: Instant? = null

    override suspend fun fetch(): Subscribe? {
        val response = subscribeClient.fetch() ?: return null

        cachedSubscribe = Subscribe(response.expirationDate)
        cacheDate = Clock.System.now()

        return cachedSubscribe
    }

    override suspend fun cache(): Cache<Subscribe?> {
        val currentCacheDate = cacheDate
        val currentSubscribe = cachedSubscribe

        // Если кэш существует и ещё свежий (менее 30 секунд)
        if (currentCacheDate != null &&
            (Clock.System.now() - currentCacheDate).inWholeSeconds < SAVE_CACHE_TIME &&
            currentSubscribe != null) {
            return Cache(currentSubscribe, currentCacheDate)
        }

        // Попытаемся получить свежие данные
        val sub = fetch()

        return if (sub != null) {
            Cache.ofNow(sub)
        } else {
            // Если не получилось, вернём кэш или пусто
            if (currentCacheDate != null && currentSubscribe != null) {
                Cache(currentSubscribe, currentCacheDate)
            } else {
                Cache.empty()
            }
        }
    }

    override suspend fun isActiveSubscribe(): Boolean {
        val subCache = cache()

        // Если кэш пуст и свежий (менее 5 секунд)
        if (subCache.isEmpty && subCache.durationSeconds < WORK_CACHE_TIME) {
            return false
        }

        // Если кэш пуст и старый (более 5 секунд), попробуем обновить
        if (subCache.isEmpty && subCache.durationSeconds > WORK_CACHE_TIME) {
            val sub = fetch()
            return sub?.expirationDate?.let { it > Clock.System.now() } ?: false
        }

        // Если подписка активна, возвращаем true
        if (subCache.value?.expirationDate?.let { it > Clock.System.now() } == true) {
            return true
        }

        // Если кэш старый, обновляем
        if (subCache.durationSeconds > WORK_CACHE_TIME) {
            val sub = fetch()
            return sub?.expirationDate?.let { it > Clock.System.now() } ?: false
        }

        return false
    }

    override suspend fun update(expirationDate: Instant) {
        cachedSubscribe = Subscribe(expirationDate)
        cacheDate = Clock.System.now()
    }

    companion object {
        private const val SAVE_CACHE_TIME = 30  // Кэш действителен 30 секунд
        private const val WORK_CACHE_TIME = 5   // Проверяем обновление каждые 5 секунд
    }
}

