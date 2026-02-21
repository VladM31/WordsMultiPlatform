package vm.words.ua.playlist.ui.vms

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import vm.words.ua.core.domain.models.Range
import vm.words.ua.playlist.domain.models.enums.PlaylistSortField
import vm.words.ua.playlist.domain.models.filters.PlayListCountFilter
import vm.words.ua.playlist.ui.actions.PlayListFilterAction
import vm.words.ua.playlist.ui.states.PlayListFilterState

class PlayListFilterViewModel : ViewModel() {
    private val mutableState = MutableStateFlow(PlayListFilterState())
    val state: StateFlow<PlayListFilterState> = mutableState.asStateFlow()

    fun send(action: PlayListFilterAction) {
        when (action) {
            is PlayListFilterAction.Init -> handleInit(action)
            is PlayListFilterAction.ChangeStartCount -> handleStartCount(action)
            is PlayListFilterAction.ChangeEndCount -> handleEndCount(action)
            is PlayListFilterAction.ChangeName -> handleChangeName(action)
            is PlayListFilterAction.Find -> handleFind()
            is PlayListFilterAction.Clear -> handleClear()
            is PlayListFilterAction.SetSortField -> mutableState.value =
                mutableState.value.copy(sortField = action.sortField)

            is PlayListFilterAction.SetAsc -> mutableState.value = mutableState.value.copy(asc = action.asc)
        }
    }

    private fun handleInit(action: PlayListFilterAction.Init) {
        if (mutableState.value.isInited) return

        mutableState.value = mutableState.value.copy(
            startCount = action.filter.count?.from?.toString().orEmpty(),
            endCount = action.filter.count?.to?.toString().orEmpty(),
            name = action.filter.name.orEmpty(),
            isInited = true,
            sortField = action.filter.sortField,
            asc = action.filter.asc
        )
    }

    private fun handleStartCount(action: PlayListFilterAction.ChangeStartCount) {
        if (action.startCount.isNotBlank() && action.startCount.toLongOrNull() == null) return
        mutableState.value = mutableState.value.copy(
            startCount = action.startCount
        )
    }

    private fun handleEndCount(action: PlayListFilterAction.ChangeEndCount) {
        if (action.endCount.isNotBlank() && action.endCount.toLongOrNull() == null) return
        mutableState.value = mutableState.value.copy(
            endCount = action.endCount
        )
    }

    private fun handleChangeName(action: PlayListFilterAction.ChangeName) {
        mutableState.value = mutableState.value.copy(
            name = action.name
        )
    }

    private fun handleFind() {
        mutableState.value = mutableState.value.copy(
            isEnd = true
        )
    }

    private fun handleClear() {
        mutableState.value = PlayListFilterState(
            startCount = "",
            endCount = "",
            name = "",
            sortField = PlaylistSortField.CREATED_AT,
            asc = false,
            isInited = true,
            isEnd = false
        )
    }

    fun toFilter(): PlayListCountFilter {
        val currentState = mutableState.value
        return PlayListCountFilter(
            name = currentState.name.ifBlank { null },
            count = if (currentState.startCount.isNotBlank() || currentState.endCount.isNotBlank()) {
                Range(
                    from = currentState.startCount.toLongOrNull(),
                    to = currentState.endCount.toLongOrNull()
                )
            } else null,
            sortField = currentState.sortField,
            asc = currentState.asc
        )
    }
}

