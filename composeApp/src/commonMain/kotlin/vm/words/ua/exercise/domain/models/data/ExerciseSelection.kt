package vm.words.ua.exercise.domain.models.data

import vm.words.ua.exercise.domain.models.enums.Exercise

data class ExerciseSelection(
    val exercise: Exercise,
    val number: Int? = null,
): Comparable<ExerciseSelection> {
    override fun compareTo(other: ExerciseSelection): Int {
        if (number != null && other.number == null) {
            return -1
        }
        if (number == null && other.number != null) {
            return 1
        }
        return number?.compareTo(other.number ?: 0) ?: exercise.compareTo(other.exercise)
    }

    val isSelected: Boolean
        get() = number != null
}

