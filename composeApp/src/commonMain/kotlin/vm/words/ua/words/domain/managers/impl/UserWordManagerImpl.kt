package vm.words.ua.words.domain.managers.impl

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.domain.models.PagedModels
import vm.words.ua.core.net.client.FileApiClient
import vm.words.ua.core.net.requests.AudioGenerationRequest
import vm.words.ua.core.net.requests.SaveFileRequest
import vm.words.ua.core.utils.toPair
import vm.words.ua.words.domain.managers.UserWordManager
import vm.words.ua.words.domain.models.*
import vm.words.ua.words.domain.models.filters.UserWordFilter
import vm.words.ua.words.net.clients.UserWordClient
import vm.words.ua.words.net.requests.*
import vm.words.ua.words.net.responds.UserWordRespond
import vm.words.ua.words.net.responds.WordRespond

class UserWordManagerImpl(
    private val userCacheManager: UserCacheManager,
    private val userWordClient: UserWordClient,
    private val fileClient: FileApiClient,
) : UserWordManager {



    private data class UploadResults(
        val imageFileName: String?,
        val soundFileName: String?
    )

    override suspend fun findBy(filter: UserWordFilter): PagedModels<UserWord> {
        val paged = userWordClient.findBy(
            userCacheManager.toPair().second,
            filter.toQueryMap()
        )

        return PagedModels.of(paged) {
            toWord(it)
        }
    }

    private fun toWord(respond: UserWordRespond): UserWord = UserWord(
        id = respond.id,
        learningGrade = respond.learningGrade,
        createdAt = respond.createdAt,
        lastReadDate = respond.lastReadDate,
        word = respond.word.toWord()
    )

    override suspend fun save(words: Collection<SaveWord>) {
        withContext(kotlinx.coroutines.Dispatchers.Default) {
            val asyncList = words.map { async { toRequest(it) } }
            userWordClient.save(
                userCacheManager.toPair().second,
                asyncList.awaitAll()
            )
        }
    }

    override suspend fun update(word: EditUserWord) {
        val uploadResults = toUploadResults(
            image = word.image,
            sound = word.sound
        )
        userWordClient.update(
            userCacheManager.toPair().second,
            UserWordEditRequest(
                id = word.id,
                original = word.original,
                lang = word.lang,
                translate = word.translate,
                translateLang = word.translateLang,
                cefr = word.cefr,
                category = word.category,
                description = word.description,
                soundFileName = uploadResults.soundFileName ?: word.soundFileName,
                imageFileName = uploadResults.imageFileName ?: word.imageFileName,
            )
        )
    }

    override suspend fun pin(pins: Collection<PinUserWord>): List<UserWord> {
        return withContext(kotlinx.coroutines.Dispatchers.Default) {
            val asyncList = pins.map { async { toRequest(it) } }
            userWordClient.pin(
                userCacheManager.toPair().second,
                asyncList.awaitAll()
            ).map { toWord(it) }
        }
    }

    override suspend fun delete(requests: List<DeleteUserWord>) {
        userWordClient.delete(
            userCacheManager.toPair().second,
            requests.map {
                DeleteUserWordRequest(
                    id = it.id,
                    wordId = it.wordId
                )
            }
        )
    }

    private suspend fun toUploadResults(
        image: PlatformFile?,
        sound: PlatformFile?,
        audioRequest: AudioGenerationRequest? = null,
    ): UploadResults {
        val imageFileName: String? = image?.let { img ->
            try {
                val bytes = img.readBytes()
                if (bytes.isEmpty()) return@let null
                val req = SaveFileRequest(content = bytes, fileName = img.name)
                fileClient.uploadFile(req).fileName
            } catch (e: Throwable) {
                println("UserWordManager: Image upload failed: ${e.message}")
                null
            }
        }

        var soundFileName: String? = sound?.let { snd ->
            try {
                val bytes = snd.readBytes()
                if (bytes.isEmpty()) return@let null
                val req = SaveFileRequest(content = bytes, fileName = snd.name)
                fileClient.uploadFile(req).fileName
            } catch (_: Throwable) {
                println($$"UserWordManager: Sound upload failed: ${t.message}")
                null
            }
        }

        if (soundFileName == null && audioRequest != null) {
            soundFileName = try {
                fileClient.textToAudioFile(audioRequest).fileName
            } catch (_: Throwable) {
                println($$"UserWordManager: TTS generation failed: ${t.message}")
                null
            }
        }

        return UploadResults(
            imageFileName = imageFileName,
            soundFileName = soundFileName
        )
    }


    private suspend fun toRequest(word: PinUserWord): PinUserWordRequest {
        val uploadResults = toUploadResults(
            image = word.image,
            sound = word.sound
        )
        return PinUserWordRequest(
            customSoundFileName = uploadResults.soundFileName,
            customImageFileName = uploadResults.imageFileName,
            wordId = word.wordId
        )
    }

    private suspend fun toRequest(word: SaveWord): UserWordRequest {
        var audioRequest: AudioGenerationRequest? = null

        if (word.needSound && word.sound == null) {
            audioRequest = AudioGenerationRequest(
                text = word.word,
                language = word.language
            )
        }

        val uploadResults = toUploadResults(
            image = word.image,
            sound = word.sound,
            audioRequest = audioRequest
        )

        return UserWordRequest(
            customSoundFileName = uploadResults.soundFileName,
            customImageFileName = uploadResults.imageFileName,
            word = word.toRequest()
        )
    }


    private fun WordRespond.toWord() = Word(
        id = id,
        original = original,
        translate = translate,
        lang = lang,
        translateLang = translateLang,
        cefr = cefr,
        description = description,
        category = category,
        soundLink = soundLink,
        imageLink = imageLink
    )

    private fun SaveWord.toRequest() = WordRequest(
        original = word,
        lang = language,

        translate = translation,
        translateLang = translationLanguage,

        category = category,
        description = description,
        cefr = cefr
    )
}