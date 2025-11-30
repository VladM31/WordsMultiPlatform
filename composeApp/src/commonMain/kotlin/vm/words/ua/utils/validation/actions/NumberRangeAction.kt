package vm.words.ua.utils.validation.actions

internal data class NumberRangeAction<N : Number>(
    val comparator: Comparator<N>,
    val minInclusive: Boolean = false,
    val maxInclusive: Boolean = false,
    val min: N? = null,
    val max: N? = null
) : vm.words.ua.utils.validation.actions.ValidAction<N> {
    override fun validate(value: N): vm.words.ua.utils.validation.models.ValidResult {
        // Check min bound (if present)
        if (min != null) {
            val cmpMin = comparator.compare(value, min)
            // invalid when value < min OR (value == min and min is exclusive)
            if (cmpMin < 0 || (!minInclusive && cmpMin == 0)) {
                val verb = if (minInclusive) "greater than or equal to" else "greater than"
                return _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult.Companion.invalid("Value should be $verb $min")
            }
        }

        // Check max bound (if present)
        if (max != null) {
            val cmpMax = comparator.compare(value, max)
            // invalid when value > max OR (value == max and max is exclusive)
            if (cmpMax > 0 || (!maxInclusive && cmpMax == 0)) {
                val verb = if (maxInclusive) "less than or equal to" else "less than"
                return _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult.Companion.invalid("Value should be $verb $max")
            }
        }

        return _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult.Companion.valid()
    }
}
