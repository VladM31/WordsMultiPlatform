package vm.words.ua.words.domain.managers.impl

import org.khronos.webgl.Uint8Array
import vm.words.ua.core.domain.models.ByteContent
import vm.words.ua.words.domain.managers.SoundManager

actual class SoundManagerImpl actual constructor() : SoundManager {

    private var audioElement: dynamic = null

    override suspend fun playSound(byteContent: ByteContent) {
        stopSound()
        try {
            // Create Uint8Array from ByteArray
            val bytes = byteContent.bytes
            val uint8 = Uint8Array(bytes.size)
            for (i in bytes.indices) {
                uint8[i] = bytes[i].toInt() and 0xFF
            }
            // Create Blob
            val blob = js("new Blob([uint8], {type: 'audio/mpeg'})")
            val url = js("URL.createObjectURL(blob)")
            audioElement = js("new Audio(url)")
            audioElement?.play()
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
    }
}
