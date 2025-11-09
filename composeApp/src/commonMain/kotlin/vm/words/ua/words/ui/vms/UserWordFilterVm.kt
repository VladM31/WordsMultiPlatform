package vm.words.ua.words.ui.vms

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.words.domain.models.filters.UserWordFilter
import vm.words.ua.words.ui.actions.UserWordFilterAction
import vm.words.ua.words.ui.states.UserWordFilterState

class UserWordFilterVm : ViewModel() {

    private val mutableState = MutableStateFlow(UserWordFilterState())
    val state: StateFlow<UserWordFilterState> = mutableState

    fun sent(action: UserWordFilterAction) {
        mutableState.value = when (action) {
            is UserWordFilterAction.Init -> {
                handleInit(action)
            }

            is UserWordFilterAction.SetOriginal -> {
                state.value.copy(original = action.value?.ifBlank { null })
            }

            is UserWordFilterAction.SetTranslate -> {
                state.value.copy(translate = action.value?.ifBlank { null })
            }

            is UserWordFilterAction.SetOriginalLang -> {
                state.value.copy(lang = if (action.value == Language.UNDEFINED) null else action.value)
            }

            is UserWordFilterAction.SetTranslateLang -> {
                state.value.copy(translateLang = if (action.value == Language.UNDEFINED) null else action.value)
            }

            is UserWordFilterAction.SetCategories -> handleCategories(action)
            is UserWordFilterAction.SetSortBy -> state.value.copy(
                sortBy = action.sortBy
            )

            is UserWordFilterAction.SetAsc -> state.value.copy(asc = action.asc)
            is UserWordFilterAction.SetCefr -> handleCefr(action)
            is UserWordFilterAction.Clear -> handleClear()
            is UserWordFilterAction.Find -> state.value.copy(isEnd = true)
        }
    }

    private fun handleClear(): UserWordFilterState {
        return setFilter(UserWordFilter())
    }

    private fun handleInit(action: UserWordFilterAction.Init): UserWordFilterState {
        if (state.value.isInit) {
            return state.value
        }
        return setFilter(action.filter)
    }

    private fun setFilter(filter: UserWordFilter): UserWordFilterState {
        return state.value.copy(
            isInit = true,

            original = filter.original,
            translate = filter.translate,
            lang = filter.languages?.firstOrNull(),
            translateLang = filter.translateLanguages?.firstOrNull(),

            categories = filter.categories,
            asc = filter.asc,
            sortBy = filter.sortField,
            cefrs = filter.cefrs
        )
    }

    private fun handleCefr(
        action: UserWordFilterAction.SetCefr
    ): UserWordFilterState {
        if (action.cefr == null) {
            return state.value.copy(cefrs = null)
        }
        val cefrs = state.value.cefrs?.toMutableSet() ?: mutableSetOf()
        if (cefrs.contains(action.cefr)) {
            cefrs.remove(action.cefr)
        } else {
            cefrs.add(action.cefr)
        }
        return state.value.copy(cefrs = if (cefrs.isEmpty()) null else cefrs)
    }

    private fun handleCategories(action: UserWordFilterAction.SetCategories): UserWordFilterState {
        if (action.value?.isEmpty() == true) {
            return state.value.copy(categories = null)
        }
        return state.value.copy(categories = action.value)
    }


}