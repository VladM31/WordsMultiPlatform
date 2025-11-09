package vm.words.ua.words.domain.utils

import vm.words.ua.core.domain.managers.ByteContentManager
import vm.words.ua.words.domain.models.Word
import vm.words.ua.words.domain.models.WordByteContent

suspend fun ByteContentManager.downloadWordByteContent(word: Word): WordByteContent {
    val imageContent =
        if (word.imageLink.isNullOrBlank()) null else downloadByteContent(word.imageLink)
    val soundContent =
        if (word.soundLink.isNullOrBlank()) null else downloadByteContent(word.soundLink)
    return WordByteContent(
        wordId = word.id,
        imageContent = imageContent,
        soundContent = soundContent
    )
}