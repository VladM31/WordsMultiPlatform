package vm.words.ua.words.domain.models

import io.github.vinceglb.filekit.PlatformFile
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language

data class EditUserWord(
    val id: String,
    val original: String,
    val lang: Language,
    val translate: String,
    val translateLang: Language,
    val cefr: CEFR,
    val category: String? = null,
    val description: String? = null,
    /** Existing file name to keep. Ignored if [sound] is provided. */
    val soundFileName: String? = null,
    /** Existing file name to keep. Ignored if [image] is provided. */
    val imageFileName: String? = null,
    /** New sound file to upload. Overrides [soundFileName]. */
    val sound: PlatformFile? = null,
    /** New image file to upload. Overrides [imageFileName]. */
    val image: PlatformFile? = null,
)