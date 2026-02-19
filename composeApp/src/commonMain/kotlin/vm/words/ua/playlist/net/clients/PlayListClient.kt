package vm.words.ua.playlist.net.clients

import vm.words.ua.core.net.responds.PagedRespond
import vm.words.ua.playlist.domain.models.filters.DeletePlayListFilter
import vm.words.ua.playlist.domain.models.filters.PlayListCountFilter
import vm.words.ua.playlist.domain.models.filters.PlayListFilter
import vm.words.ua.playlist.net.requests.*
import vm.words.ua.playlist.net.responds.AssignedPlaylistRespond
import vm.words.ua.playlist.net.responds.PlayListCountRespond
import vm.words.ua.playlist.net.responds.PlayListRespond
import vm.words.ua.playlist.net.responds.PublicPlayListCountRespond

interface PlayListClient {
    suspend fun countBy(token: String, filter: PlayListCountFilter): PagedRespond<PlayListCountRespond>
    suspend fun countBy(token: String, filter: PublicPlayListCountRequest): PagedRespond<PublicPlayListCountRespond>

    suspend fun findBy(token: String, filter: PlayListFilter): PagedRespond<PlayListRespond>
    suspend fun findBy(token: String, filter: PublicPlayListGetRequest): PagedRespond<PlayListRespond>


    suspend fun getAssignedPlaylists(token: String): Set<AssignedPlaylistRespond>
    suspend fun assignPlayLists(token: String, req: AssignPlayListsRequest)

    suspend fun save(token: String, playLists: List<SavePlayListRequest>)
    suspend fun delete(token: String, filter: DeletePlayListFilter)

    suspend fun update(token: String, playLists: List<UpdatePlayListRequest>)
    suspend fun updateGrades(token: String, grades: List<PlayListGradeRequest>)
}

