package vm.words.ua.playlist.domain.managers.impl

import kotlinx.datetime.Instant
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.domain.models.PagedModels
import vm.words.ua.playlist.domain.managers.PlayListManager
import vm.words.ua.playlist.domain.models.*
import vm.words.ua.playlist.domain.models.PlayList.PinnedWord
import vm.words.ua.playlist.domain.models.filters.*
import vm.words.ua.playlist.net.clients.PlayListClient
import vm.words.ua.playlist.net.requests.*
import vm.words.ua.playlist.net.responds.PlayListCountRespond
import vm.words.ua.playlist.net.responds.PlayListRespond
import vm.words.ua.playlist.net.responds.PublicPlayListCountRespond
import vm.words.ua.words.domain.models.UserWord
import vm.words.ua.words.domain.models.Word

class PlayListManagerImpl(
    private val playListClient: PlayListClient,
    private val userCacheManager: UserCacheManager
) : PlayListManager {

    private fun getToken(): String {
        return userCacheManager.token.value
    }

    override suspend fun countBy(filter: PlayListCountFilter): PagedModels<PlayListCount> {
        return try {
            val result = playListClient.countBy(getToken(), filter)
            PagedModels.of(result) {
                it.toPlayListCount()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            PagedModels.empty()
        }
    }

    override suspend fun findBy(filter: PlayListFilter): PagedModels<PlayList> {
        return try {
            val result = playListClient.findBy(getToken(), filter)
            PagedModels.of(result) {
                it.toPlayList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            PagedModels.empty()
        }
    }

    override suspend fun findBy(filter: PublicPlayListFilter): PagedModels<PlayList> {
        val req = PublicPlayListGetRequest(
            ids = filter.ids,
            name = filter.name,
            sortField = filter.sortField,
            asc = filter.asc,
            page = filter.page,
            size = filter.size
        )
        return try {
            val result = playListClient.findBy(getToken(), req)
            PagedModels.of(result) {
                it.toPlayList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            PagedModels.empty()
        }
    }

    override suspend fun countBy(
        filter: PublicPlayListCountFilter
    ): PagedModels<PublicPlayListCountDto> {
        return try {
            val respond = playListClient.countBy(
                getToken(),
                filter.toPublicPlayListCountRequest()
            )
            PagedModels.of(respond) {
                it.toPublicPlayListCountDto()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            PagedModels.empty()
        }

    }

    override suspend fun getAssignedPlaylists(): Set<AssignedPlaylistDto> {
        return try {
            val respond = playListClient.getAssignedPlaylists(
                getToken()
            )
            respond.map { AssignedPlaylistDto(it.id) }.toSet()
        } catch (e: Exception) {
            e.printStackTrace()
            emptySet()
        }
    }

    override suspend fun assignPlayLists(dto: AssignPlayListsDto) {
        playListClient.assignPlayLists(
            getToken(),
            AssignPlayListsRequest(dto.playListIds)
        )
    }

    private fun PublicPlayListCountRespond.toPublicPlayListCountDto(): PublicPlayListCountDto = PublicPlayListCountDto(
        id = id,
        name = name,
        createdAt = Instant.parse(createdAt),
        count = count,
        tags = tags,
        cefrs = cefrs,
        language = language,
        translateLanguage = translateLanguage
    )

    private fun PublicPlayListCountFilter.toPublicPlayListCountRequest(): PublicPlayListCountRequest =
        PublicPlayListCountRequest(
            name = name,
            tags = tags,
            cefrs = cefrs,
            language = language,
            translateLanguage = translateLanguage,
            page = page,
            size = size,
            sortField = sortField,
            asc = asc
        )

    override suspend fun save(playLists: List<SavePlayList>): Result<Unit> {
        return runCatching {
            playListClient.save(
                getToken(),
                playLists.map { SavePlayListRequest(it.name) }
            )
        }
    }

    override suspend fun update(playLists: List<UpdatePlayList>) {
        try {
            playListClient.update(
                getToken(),
                playLists.map { UpdatePlayListRequest(it.id, it.name) }
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun updateGrades(grades: List<PlayListGrade>) {
        try {
            playListClient.updateGrades(
                getToken(),
                grades.map { PlayListGradeRequest(it.wordId, it.wordGrade) }
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun delete(filter: DeletePlayListFilter) {
        try {
            playListClient.delete(getToken(), filter)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun PlayListCountRespond.toPlayListCount(): PlayListCount {
        return PlayListCount(
            id = id,
            name = name,
            createdAt = Instant.parse(createdAt),
            count = count,
            tags = tags,
            cefrs = cefrs,
            language = language,
            translateLanguage = translateLanguage
        )
    }

    private fun PlayListRespond.toPlayList(): PlayList {
        return PlayList(
            id = id,
            name = name,
            createdAt = Instant.parse(createdAt),
            words = words.map { it.toPinnedWord() },
            tags = tags,
            cefrs = cefrs,
            language = language,
            translateLanguage = translateLanguage
        )
    }

    private fun PlayListRespond.PinnedWordResponse.toPinnedWord(): PinnedWord {
        return PinnedWord(
            learningGrade = learningGrade,
            lastReadDate = Instant.parse(lastReadDate),
            userWord = word.toUserWord()
        )
    }

    private fun PlayListRespond.UserWordResponse.toUserWord(): UserWord {
        return UserWord(
            id = id,
            learningGrade = learningGrade,
            createdAt = Instant.parse(createdAt),
            lastReadDate = Instant.parse(lastReadDate),
            word = word.toWord()
        )
    }

    private fun PlayListRespond.WordResponse.toWord(): Word {
        return Word(
            id = id,
            original = original,
            lang = lang,
            translate = translate,
            translateLang = translateLang,
            cefr = cefr,
            description = description,
            category = category,
            soundLink = soundLink,
            imageLink = imageLink
        )
    }
}
