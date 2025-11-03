package vm.words.ua.words.ui.vms

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.words.domain.mappers.toWordFilterState
import vm.words.ua.words.ui.actions.WordFilterAction
import vm.words.ua.words.ui.states.WordFilterState

class WordFilterViewModel : ViewModel() {

    private val mutableState = MutableStateFlow(WordFilterState())
    val state: StateFlow<WordFilterState> = mutableState

    fun sent(action: WordFilterAction) {
        mutableState.value = when (action) {
            is WordFilterAction.SetAsc -> state.value.copy(asc = action.value)
            is WordFilterAction.SetCategories -> state.value.copy(categories = action.value)
            is WordFilterAction.SetOriginal -> state.value.copy(original = action.value)
            is WordFilterAction.SetOriginalLang -> state.value.copy(originalLang = action.value)
            is WordFilterAction.SetSortBy -> state.value.copy(sortBy = action.value)
            is WordFilterAction.SetTranslate -> state.value.copy(translate = action.value)
            is WordFilterAction.SetTranslateLang -> state.value.copy(translateLang = action.value)
            is WordFilterAction.Init -> {
                handleInit(action)
            }
            is WordFilterAction.SetCefr -> toggleCefr(action.value)
            is WordFilterAction.SetCefrs -> state.value.copy(cefrs = action.value)
        }
    }

    private fun toggleCefr(value: CEFR?): WordFilterState {
        if (value == null) return state.value.copy(cefrs = null)
        val current = state.value.cefrs?.toMutableSet() ?: mutableSetOf()
        if (current.contains(value)) current.remove(value) else current.add(value)
        return state.value.copy(cefrs = if (current.isEmpty()) null else current)
    }

    private fun handleInit(action: WordFilterAction.Init) =
        if (state.value.isInit.not()) {
            action.value.toWordFilterState().copy(
                isInit = true
            )
        } else {
            state.value
        }
}