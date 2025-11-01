package vm.words.ua.exercise.ui.bundles

import vm.words.ua.words.domain.models.UserWord
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class ExerciseSelectionBundle @OptIn(ExperimentalUuidApi::class) constructor(
    val playListId: String?,
    val transactionId: String = Uuid.random().toString(),
    val words: List<UserWord>
)