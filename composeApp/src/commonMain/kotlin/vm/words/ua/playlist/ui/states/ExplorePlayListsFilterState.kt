package vm.words.ua.playlist.ui.states

import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.playlist.domain.models.enums.PublicPlaylistSortField

data class ExplorePlayListsFilterState(
    val name: String = "",
    val cefrs: Set<CEFR> = emptySet(),
    val language: Language? = null,
    val translateLanguage: Language? = null,
    val tags: Set<String> = emptySet(),
    val tagInput: String = "",
    val sortField: PublicPlaylistSortField = PublicPlaylistSortField.CREATED_AT,
    val asc: Boolean = false,
    val isInited: Boolean = false,
    val isEnd: Boolean = false
)

