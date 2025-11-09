package vm.words.ua.words.ui.states

import io.github.vinceglb.filekit.PlatformFile
import vm.words.ua.core.domain.models.ByteContent
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.core.ui.states.EndetableState

data class PinUserWordsState(
    val index: Int = 0,
    val words: List<Word> = emptyList(),

    val image: PlatformFile? = null,
    val sound: PlatformFile? = null,
    val updateId: String? = null,
    val currentUpdateId: String? = null,

    val isInited: Boolean = false,
    override val isEnd: Boolean = false,
) : EndetableState {

    val hasFiles get() = image != null || sound != null

    val hasUpdate get() = updateId != currentUpdateId && updateId != null

    val currentWord: Word?
        get() = words.getOrNull(index)

    data class Word(
        val wordId: String,
        val original: String,
        val lang: Language,
        val originalSound: ByteContent? = null,
        val originalImage: ByteContent? = null,
        var customSound: PlatformFile? = null,
        var customImage: PlatformFile? = null,
    )
}