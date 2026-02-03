package vm.words.ua.playlist.ui.vms

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.playlist.domain.models.filters.PublicPlayListCountFilter
import vm.words.ua.playlist.ui.actions.ExplorePlayListsFilterAction
import vm.words.ua.playlist.ui.states.ExplorePlayListsFilterState

class ExplorePlayListsFilterViewModel : ViewModel() {

    private val mutableState = MutableStateFlow(ExplorePlayListsFilterState())
    val state: StateFlow<ExplorePlayListsFilterState> = mutableState

    fun send(action: ExplorePlayListsFilterAction) {
        mutableState.value = when (action) {
            is ExplorePlayListsFilterAction.Init -> handleInit(action.filter)
            is ExplorePlayListsFilterAction.ChangeName -> state.value.copy(name = action.name)
            is ExplorePlayListsFilterAction.ToggleCefr -> handleToggleCefr(action.cefr)
            is ExplorePlayListsFilterAction.SetLanguage -> state.value.copy(language = action.language)
            is ExplorePlayListsFilterAction.SetTranslateLanguage -> state.value.copy(translateLanguage = action.language)
            is ExplorePlayListsFilterAction.ChangeTagInput -> state.value.copy(tagInput = action.input)
            is ExplorePlayListsFilterAction.AddTag -> handleAddTag()
            is ExplorePlayListsFilterAction.RemoveTag -> state.value.copy(tags = state.value.tags - action.tag)
            is ExplorePlayListsFilterAction.SetSortField -> state.value.copy(sortField = action.field)
            is ExplorePlayListsFilterAction.SetAsc -> state.value.copy(asc = action.asc)
            is ExplorePlayListsFilterAction.Clear -> ExplorePlayListsFilterState(isInited = true)
            is ExplorePlayListsFilterAction.Find -> state.value.copy(isEnd = true)
        }
    }

    private fun handleInit(filter: PublicPlayListCountFilter): ExplorePlayListsFilterState {
        if (state.value.isInited) {
            return state.value
        }
        return ExplorePlayListsFilterState(
            name = filter.name ?: "",
            cefrs = filter.cefrs ?: emptySet(),
            language = filter.language,
            translateLanguage = filter.translateLanguage,
            tags = filter.tags ?: emptySet(),
            sortField = filter.sortField,
            asc = filter.asc,
            isInited = true
        )
    }

    private fun handleToggleCefr(cefr: CEFR): ExplorePlayListsFilterState {
        val currentCefrs = state.value.cefrs
        val newCefrs = if (currentCefrs.contains(cefr)) {
            currentCefrs - cefr
        } else {
            currentCefrs + cefr
        }
        return state.value.copy(cefrs = newCefrs)
    }

    private fun handleAddTag(): ExplorePlayListsFilterState {
        val tag = state.value.tagInput.trim()
        if (tag.isEmpty()) return state.value
        return state.value.copy(
            tags = state.value.tags + tag,
            tagInput = ""
        )
    }

    fun toFilter(): PublicPlayListCountFilter {
        val currentState = state.value
        return PublicPlayListCountFilter(
            name = currentState.name.takeIf { it.isNotBlank() },
            cefrs = currentState.cefrs.takeIf { it.isNotEmpty() },
            language = currentState.language,
            translateLanguage = currentState.translateLanguage,
            tags = currentState.tags.takeIf { it.isNotEmpty() },
            sortField = currentState.sortField,
            asc = currentState.asc
        )
    }
}

