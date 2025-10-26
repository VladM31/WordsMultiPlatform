package vm.words.ua.words.domain.managers.impl

import org.w3c.dom.Audio
import org.w3c.dom.HTMLAudioElement
import vm.words.ua.core.domain.models.ByteContent
import vm.words.ua.words.domain.managers.SoundManager
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

actual class SoundManagerImpl actual constructor() : SoundManager {

    private var audioElement: HTMLAudioElement? = null

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun playSound(byteContent: ByteContent) {
        stopSound()
        try {
            val base64 = Base64.encode(byteContent.bytes)
            val src = "data:audio/mpeg;base64,$base64"

            val audio = Audio()
            audio.src = src
            audio.preload = "auto"
            audio.play()

            audioElement = audio
        } catch (e: Throwable) {
            // console.error("WASM audio play error", e)
        }
    }

    override suspend fun stopSound() {
        val audio = audioElement ?: return
        try {
            audio.pause()
            audio.currentTime = 0.0
        } catch (_: Throwable) {
        } finally {
            audioElement = null
        }
    }
}
