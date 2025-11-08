package vm.words.ua.words.domain.managers.impl

import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.domain.models.PagedModels
import vm.words.ua.words.domain.managers.UserWordManager
import vm.words.ua.words.domain.models.DeleteUserWord
import vm.words.ua.words.domain.models.PinUserWord
import vm.words.ua.words.domain.models.SaveWord
import vm.words.ua.words.domain.models.UserWord
import vm.words.ua.words.domain.models.filters.UserWordFilter
import vm.words.ua.words.net.clients.UserWordClient

class UserWordManagerImpl(
    private val userCacheManager: UserCacheManager,
    private val userWordClient: UserWordClient
) : UserWordManager {
    override suspend fun findBy(filter: UserWordFilter): PagedModels<UserWord> {
        TODO("Not yet implemented")
    }

    override suspend fun save(words: Collection<SaveWord>) {
        TODO("Not yet implemented")
    }

    override suspend fun pin(pins: Collection<PinUserWord>) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(requests: List<DeleteUserWord>) {
        TODO("Not yet implemented")
    }
}