package vm.words.ua.words.domain.managers.impl

import vm.words.ua.core.domain.models.ByteContent
import vm.words.ua.words.domain.managers.SoundManager

actual class SoundManagerImpl actual constructor() : SoundManager {

    private var audioElement: dynamic = null

    override suspend fun playSound(byteContent: ByteContent) {
        stopSound()

        try {
            val blob = js("new Blob([new Uint8Array(${byteContent.bytes})], {type: 'audio/mpeg'})")
            val url = js("URL.createObjectURL(${blob})")

            audioElement = js("new Audio(${url})")
            audioElement.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun stopSound() {
        audioElement?.let {
            try {
                it.pause()
                it.currentTime = 0.0
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
