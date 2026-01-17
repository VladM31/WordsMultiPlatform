package vm.words.ua.learning.ui.actions

sealed interface LearningHistoryListAction {
    data object LoadMore : LearningHistoryListAction
    data object Refresh : LearningHistoryListAction
}

