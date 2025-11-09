package vm.words.ua.words.ui.actions

import io.github.vinceglb.filekit.PlatformFile
import vm.words.ua.words.domain.models.Word

interface PinUserWordsAction {
    data class Load(val words: Collection<Word>) : PinUserWordsAction
    data class SetImage(val image: PlatformFile?) : PinUserWordsAction
    data class SetSound(val sound: PlatformFile?) : PinUserWordsAction

    data object PlaySound : PinUserWordsAction

    data object SaveFiles : PinUserWordsAction
    data object Pin : PinUserWordsAction
    data object NextWord : PinUserWordsAction
    data object PreviousWord : PinUserWordsAction
}