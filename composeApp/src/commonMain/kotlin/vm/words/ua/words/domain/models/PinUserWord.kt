package vm.words.ua.words.domain.models

import io.github.vinceglb.filekit.PlatformFile

data class PinUserWord(
    val wordId: String,
    val sound: PlatformFile? = null,
    val image: PlatformFile? = null,
)