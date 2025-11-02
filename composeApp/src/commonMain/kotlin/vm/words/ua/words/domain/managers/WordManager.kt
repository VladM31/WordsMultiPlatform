package vm.words.ua.words.domain.managers

import vm.words.ua.core.domain.models.PagedModels
import vm.words.ua.words.domain.models.Word
import vm.words.ua.words.domain.models.filters.WordFilter

interface WordManager {

    suspend fun findBy(filter: WordFilter): PagedModels<Word>

    suspend fun findOne(filter: WordFilter): Word?
}