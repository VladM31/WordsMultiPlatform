package vm.words.ua.words.domain.models

import vm.words.ua.core.domain.models.ByteContent

data class WordByteContent(
    val wordId: String,
    val soundContent: ByteContent? = null,
    val imageContent: ByteContent? = null
)
