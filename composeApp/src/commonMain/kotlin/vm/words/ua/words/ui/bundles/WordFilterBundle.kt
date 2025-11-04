package vm.words.ua.words.ui.bundles

import vm.words.ua.words.domain.models.filters.WordFilter

data class WordFilterBundle(
    val filterId: Long,
    val filter: WordFilter
)
