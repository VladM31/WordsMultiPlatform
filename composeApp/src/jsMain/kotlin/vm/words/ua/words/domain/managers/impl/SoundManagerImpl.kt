package vm.words.ua.words.domain.managers.impl

import vm.words.ua.core.domain.models.ByteContent
import vm.words.ua.words.domain.managers.SoundManager

actual class SoundManagerImpl actual constructor() : SoundManager {

    private var audioElement: dynamic = null

    override suspend fun playSound(byteContent: ByteContent) {
        stopSound()

        try {
            val blob = js("new Blob([new Uint8Array(args[0])], {type: 'audio/mpeg'})", byteContent.bytes)
            val url = js("URL.createObjectURL(blob)")

            audioElement = js("new Audio(url)")
            (audioElement as dynamic).play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun stopSound() {
        audioElement?.let {
            try {
                js("it.pause()")
                js("it.currentTime = 0")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

