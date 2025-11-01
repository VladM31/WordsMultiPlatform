package vm.words.ua.exercise.domain.models.data

data class MatchWordsBox(
    val word: ExerciseWordDetails,
    val isMistake: Boolean = false,
    val position: Int? = null
) : Comparable<MatchWordsBox> {
    override fun compareTo(other: MatchWordsBox): Int {
        val thisPosition = position ?: -1
        val otherPosition = other.position ?: -1

        return otherPosition.compareTo(thisPosition)
    }
}