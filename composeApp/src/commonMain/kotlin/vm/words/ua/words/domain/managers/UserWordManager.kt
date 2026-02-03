package vm.words.ua.words.domain.managers

import vm.words.ua.core.domain.models.PagedModels
import vm.words.ua.words.domain.models.DeleteUserWord
import vm.words.ua.words.domain.models.PinUserWord
import vm.words.ua.words.domain.models.SaveWord
import vm.words.ua.words.domain.models.UserWord
import vm.words.ua.words.domain.models.filters.UserWordFilter

interface UserWordManager {

    suspend fun findBy(filter: UserWordFilter): PagedModels<UserWord>

    suspend fun save(words: Collection<SaveWord>)

    suspend fun pin(pins: Collection<PinUserWord>): List<UserWord>

    suspend fun delete(requests: List<DeleteUserWord>)
}