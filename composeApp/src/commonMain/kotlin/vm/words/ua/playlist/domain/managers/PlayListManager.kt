package vm.words.ua.playlist.domain.managers

import vm.words.ua.core.domain.models.PagedModels
import vm.words.ua.playlist.domain.models.*
import vm.words.ua.playlist.domain.models.filters.*

interface PlayListManager {
    suspend fun countBy(filter: PlayListCountFilter): PagedModels<PlayListCount>
    suspend fun countBy(filter: PublicPlayListCountFilter): PagedModels<PublicPlayListCountDto>


    suspend fun findBy(filter: PlayListFilter): PagedModels<PlayList>
    suspend fun findBy(filter: PublicPlayListFilter): PagedModels<PlayList>

    suspend fun getAssignedPlaylists(): Set<AssignedPlaylistDto>
    suspend fun assignPlayLists(dto: AssignPlayListsDto)

    suspend fun update(playLists: List<UpdatePlayList>)
    suspend fun updateGrades(grades: List<PlayListGrade>)


    suspend fun save(playLists: List<SavePlayList>): Result<Unit>
    suspend fun delete(filter: DeletePlayListFilter)
}

