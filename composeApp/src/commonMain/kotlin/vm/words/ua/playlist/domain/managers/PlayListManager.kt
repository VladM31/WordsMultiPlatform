package vm.words.ua.playlist.domain.managers

import vm.words.ua.core.domain.models.PagedModels
import vm.words.ua.playlist.domain.models.PlayList
import vm.words.ua.playlist.domain.models.PlayListCount
import vm.words.ua.playlist.domain.models.PlayListGrade
import vm.words.ua.playlist.domain.models.SavePlayList
import vm.words.ua.playlist.domain.models.UpdatePlayList
import vm.words.ua.playlist.domain.models.filters.DeletePlayListFilter
import vm.words.ua.playlist.domain.models.filters.PlayListCountFilter
import vm.words.ua.playlist.domain.models.filters.PlayListFilter

interface PlayListManager {
    suspend fun countBy(filter: PlayListCountFilter): PagedModels<PlayListCount>
    suspend fun findBy(filter: PlayListFilter): PagedModels<PlayList>
    suspend fun save(playLists: List<SavePlayList>): Result<Unit>
    suspend fun update(playLists: List<UpdatePlayList>)
    suspend fun updateGrades(grades: List<PlayListGrade>)
    suspend fun delete(filter: DeletePlayListFilter)
}

