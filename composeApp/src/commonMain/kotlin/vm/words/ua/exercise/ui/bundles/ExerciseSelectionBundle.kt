package vm.words.ua.exercise.ui.bundles

import vm.words.ua.words.domain.models.UserWord

data class ExerciseSelectionBundle(
    val playListId: String,
    val words: List<UserWord>
)