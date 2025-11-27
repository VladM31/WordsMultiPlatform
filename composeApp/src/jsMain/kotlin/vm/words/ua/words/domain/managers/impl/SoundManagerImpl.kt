package vm.words.ua.words.domain.managers.impl

import org.khronos.webgl.Uint8Array
import vm.words.ua.core.domain.models.ByteContent
import vm.words.ua.words.domain.managers.SoundManager

actual class SoundManagerImpl actual constructor() : SoundManager {

    private var audioElement: dynamic = null
    private var currentUrl: dynamic = null

    override suspend fun playSound(byteContent: ByteContent) {
        stopSound()
        try {
            // Create Uint8Array from ByteArray
            val bytes = byteContent.bytes
            val uint8 = Uint8Array(bytes.size)
            for (i in bytes.indices) {
                // Use dynamic assignment to avoid calling the wrong `set` overload
                uint8.asDynamic()[i] = bytes[i].toInt() and 0xFF
            }
            // Create object URL from the uint8 array and construct Audio via the JS Audio constructor
            val url = js("URL.createObjectURL(new Blob([uint8], {type: 'audio/mpeg'}))")
            currentUrl = url
            val AudioConstructor: dynamic = js("Audio")
            audioElement = AudioConstructor(url)
            // Revoke the object URL when playback ends to avoid leaking Blob URLs
            try {
                audioElement.onended = { _: dynamic -> js("URL.revokeObjectURL(url)") }
            } catch (_: dynamic) {
                // ignore if setting handler fails on some platforms
            }
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
        // Revoke URL if we created one
        if (currentUrl != null) {
            try {
                js("URL.revokeObjectURL(currentUrl)")
            } catch (e: dynamic) {
                console.error("Failed to revoke object URL: ", e)
            } finally {
                currentUrl = null
            }
        }
        audioElement = null
    }
}
