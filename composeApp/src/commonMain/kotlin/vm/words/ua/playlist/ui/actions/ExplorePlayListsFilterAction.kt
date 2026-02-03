package vm.words.ua.playlist.ui.actions

import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.playlist.domain.models.enums.PublicPlaylistSortField
import vm.words.ua.playlist.domain.models.filters.PublicPlayListCountFilter

sealed interface ExplorePlayListsFilterAction {
    data class Init(val filter: PublicPlayListCountFilter) : ExplorePlayListsFilterAction
    data class ChangeName(val name: String) : ExplorePlayListsFilterAction
    data class ToggleCefr(val cefr: CEFR) : ExplorePlayListsFilterAction
    data class SetLanguage(val language: Language?) : ExplorePlayListsFilterAction
    data class SetTranslateLanguage(val language: Language?) : ExplorePlayListsFilterAction
    data class ChangeTagInput(val input: String) : ExplorePlayListsFilterAction
    data object AddTag : ExplorePlayListsFilterAction
    data class RemoveTag(val tag: String) : ExplorePlayListsFilterAction
    data class SetSortField(val field: PublicPlaylistSortField) : ExplorePlayListsFilterAction
    data class SetAsc(val asc: Boolean) : ExplorePlayListsFilterAction
    data object Clear : ExplorePlayListsFilterAction
    data object Find : ExplorePlayListsFilterAction
}

