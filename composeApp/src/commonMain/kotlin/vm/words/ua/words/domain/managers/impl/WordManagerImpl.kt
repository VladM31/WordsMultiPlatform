package vm.words.ua.words.domain.managers.impl

import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.domain.models.PagedModels
import vm.words.ua.core.utils.toPair
import vm.words.ua.words.domain.managers.WordManager
import vm.words.ua.words.domain.models.Word
import vm.words.ua.words.domain.models.filters.WordFilter
import vm.words.ua.words.net.clients.WordClient
import vm.words.ua.words.net.responds.WordRespond

class WordManagerImpl(
    private val wordClient: WordClient,
    private val userCacheManager: UserCacheManager
) : WordManager {
    override suspend fun findBy(filter: WordFilter): PagedModels<Word> {
        return try {
            val query = filter.toQueryMap()
            wordClient.findBy(userCacheManager.toPair().second, query)
                .let { PagedModels.of(it, this::toWord) }
        } catch (e: Exception) {
            e.printStackTrace()
            PagedModels.empty()
        }
    }

    override suspend fun findOne(filter: WordFilter): Word? {
        val newFilter = filter.copy(size = 1)
        return findBy(newFilter).content.firstOrNull()
    }

    private fun toWord(res: WordRespond): Word {
        return Word(
            id = res.id,
            original = res.original,
            translate = res.translate,
            lang = res.lang,
            translateLang = res.translateLang,
            cefr = res.cefr,
            description = res.description,
            category = res.category,
            soundLink = res.soundLink,
            imageLink = res.imageLink
        )
    }
}