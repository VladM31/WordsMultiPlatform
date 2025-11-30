package vm.words.ua.words.domain.managers.impl

import org.khronos.webgl.Uint8Array
import vm.words.ua.core.domain.models.ByteContent
import vm.words.ua.words.domain.managers.SoundManager

actual class SoundManagerImpl actual constructor() : SoundManager {

    private var audioElement: dynamic = null
    private var currentUrl: String? = null

    override suspend fun playSound(byteContent: ByteContent) {
        stopSound()
        try {
            // Create Uint8Array from ByteArray
            val bytes = byteContent.bytes
            val uint8 = Uint8Array(byteContent.bytes.toTypedArray())

            // Создаём Blob url
            @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
            val url: String = js(
                "URL.createObjectURL(new Blob([uint8], {type: 'audio/mpeg'}))"
            ) as String
            currentUrl = url

            // ВАЖНО: используем new Audio()
            val audio: dynamic = js("new Audio()")
            audio.src = url
            audioElement = audio

            // По окончании воспроизведения освобождаем URL
            try {
                audio.onended = { _: dynamic ->
                    try {
                        val urlToRevoke = currentUrl
                        if (urlToRevoke != null) {
                            js("URL.revokeObjectURL")(urlToRevoke)
                        }
                    } catch (_: dynamic) {
                        // ignore
                    } finally {
                        currentUrl = null
                    }
                }
            } catch (_: dynamic) {
                // ignore if setting handler fails
            }

            audio.play()
        } catch (e: dynamic) {
            console.error("Failed to play sound: ", e)
        }
    }

    override suspend fun stopSound() {
        val element = audioElement
        if (element != null) {
            try {
                element.pause()
                element.currentTime = 0.0
            } catch (e: dynamic) {
                console.error("Failed to stop sound: ", e)
            }
        }

        // Revoke URL if we created one
        val urlToRevoke = currentUrl
        if (urlToRevoke != null) {
            try {
                js("URL.revokeObjectURL")(urlToRevoke)
            } catch (e: dynamic) {
                console.error("Failed to revoke object URL: ", e)
            } finally {
                currentUrl = null
            }
        }
        audioElement = null
    }
}
