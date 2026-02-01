package vm.words.ua.playlist.domain.managers

import vm.words.ua.core.domain.models.PagedModels
import vm.words.ua.playlist.domain.models.*
import vm.words.ua.playlist.domain.models.filters.DeletePlayListFilter
import vm.words.ua.playlist.domain.models.filters.PlayListCountFilter
import vm.words.ua.playlist.domain.models.filters.PlayListFilter
import vm.words.ua.playlist.domain.models.filters.PublicPlayListCountFilter

interface PlayListManager {
    suspend fun countBy(filter: PlayListCountFilter): PagedModels<PlayListCount>
    suspend fun findBy(filter: PlayListFilter): PagedModels<PlayList>

    suspend fun findPublicBy(filter: PublicPlayListCountFilter): PagedModels<PublicPlayListCountDto>
    suspend fun getAssignedPlaylists(): Set<AssignedPlaylistDto>
    suspend fun assignPlayLists(dto: AssignPlayListsDto)

    suspend fun update(playLists: List<UpdatePlayList>)
    suspend fun updateGrades(grades: List<PlayListGrade>)


    suspend fun save(playLists: List<SavePlayList>): Result<Unit>
    suspend fun delete(filter: DeletePlayListFilter)
}

