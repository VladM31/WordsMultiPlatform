package vm.words.ua.playlist.domain.managers.impl

import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.domain.models.PagedModels
import vm.words.ua.playlist.domain.models.PlayList
import vm.words.ua.playlist.domain.models.PlayListCount
import vm.words.ua.playlist.domain.models.PlayListGrade
import vm.words.ua.playlist.domain.models.SavePlayList
import vm.words.ua.playlist.domain.models.UpdatePlayList
import vm.words.ua.playlist.domain.models.filters.DeletePlayListFilter
import vm.words.ua.playlist.domain.models.filters.PlayListCountFilter
import vm.words.ua.playlist.domain.models.filters.PlayListFilter
import vm.words.ua.playlist.domain.managers.PlayListManager
import vm.words.ua.playlist.net.clients.PlayListClient
import vm.words.ua.playlist.net.models.requests.PlayListGradeRequest
import vm.words.ua.playlist.net.models.requests.SavePlayListRequest
import vm.words.ua.playlist.net.models.requests.UpdatePlayListRequest

class PlayListManagerImpl(
    private val playListClient: PlayListClient,
    private val userCacheManager: UserCacheManager
) : PlayListManager {

    private fun getToken(): String {
        return userCacheManager.token.value
    }

    override suspend fun countBy(filter: PlayListCountFilter): PagedModels<PlayListCount> {
        return try {
            println("PlayListManager.countBy: Starting request with filter: $filter")
            println("PlayListManager.countBy: Token: ${getToken()}")
            val result = playListClient.countBy(getToken(), filter)
            println("PlayListManager.countBy: Success, got ${result.content.size} items")
            result
        } catch (e: Exception) {
            println("PlayListManager.countBy: ERROR - ${e.message}")
            e.printStackTrace()
            PagedModels.empty()
        }
    }

    override suspend fun findBy(filter: PlayListFilter): PagedModels<PlayList> {
        return try {
            playListClient.findBy(getToken(), filter)
        } catch (e: Exception) {
            e.printStackTrace()
            PagedModels.empty()
        }
    }

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
}
