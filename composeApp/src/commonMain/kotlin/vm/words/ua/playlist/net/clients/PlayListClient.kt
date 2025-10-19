package vm.words.ua.playlist.net.clients

import vm.words.ua.core.domain.models.PagedModels
import vm.words.ua.playlist.domain.models.PlayListCount
import vm.words.ua.playlist.domain.models.PlayList
import vm.words.ua.playlist.domain.models.filters.DeletePlayListFilter
import vm.words.ua.playlist.domain.models.filters.PlayListCountFilter
import vm.words.ua.playlist.domain.models.filters.PlayListFilter
import vm.words.ua.playlist.net.models.requests.PlayListGradeRequest
import vm.words.ua.playlist.net.models.requests.SavePlayListRequest
import vm.words.ua.playlist.net.models.requests.UpdatePlayListRequest

interface PlayListClient {
    suspend fun countBy(token: String, filter: PlayListCountFilter): PagedModels<PlayListCount>
    suspend fun findBy(token: String, filter: PlayListFilter): PagedModels<PlayList>
    suspend fun save(token: String, playLists: List<SavePlayListRequest>)
    suspend fun update(token: String, playLists: List<UpdatePlayListRequest>)
    suspend fun updateGrades(token: String, grades: List<PlayListGradeRequest>)
    suspend fun delete(token: String, filter: DeletePlayListFilter)
}

