@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package vm.words.ua.words.domain.managers.impl

import vm.words.ua.core.domain.models.ByteContent
import vm.words.ua.words.domain.managers.SoundManager
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned

import platform.AVFAudio.*
import platform.Foundation.*
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

actual class SoundManagerImpl actual constructor() : SoundManager {

    private var player: AVAudioPlayer? = null
    private var tempFilePath: String? = null

    override suspend fun playSound(byteContent: ByteContent) {
        dispatch_async(dispatch_get_main_queue()) {
            // Guard: empty bytes -> nothing to play
            if (byteContent.bytes.isEmpty()) {
                stopInternal(); return@dispatch_async
            }
            stopInternal()
            prepareSession()

            val data = byteArrayToNSData(byteContent.bytes)
            var p = AVAudioPlayer(data = data, error = null)
            if (p == null) {
                // Fallback: write to temp mp3 file (guaranteed MP3 per requirements)
                tempFilePath = writeTempMp3(data)
                p = AVAudioPlayer(contentsOfURL = NSURL.fileURLWithPath(tempFilePath!!), error = null)
            }
            p?.delegate = object : NSObject(), AVAudioPlayerDelegateProtocol {
                override fun audioPlayerDidFinishPlaying(player: AVAudioPlayer, successfully: Boolean) {
                    cleanupTempFile()
                    this@SoundManagerImpl.player = null
                }
            }
            p?.prepareToPlay()
            if (p?.play() == true) {
                player = p
            } else {
                println("[SoundManagerImpl] Failed to start playback")
                cleanupTempFile()
            }
        }
    }

    override suspend fun stopSound() {
        dispatch_async(dispatch_get_main_queue()) { stopInternal() }
    }

    private fun stopInternal() {
        player?.let { if (it.playing) it.stop() }
        player = null
        cleanupTempFile()
    }

    // Session setup separated for clarity
    private fun prepareSession() {
        val session = AVAudioSession.sharedInstance()
        // Use constant instead of raw string; category only
        // Newer APIs may expose different overloads; this one accepts error pointer last.
        session.setCategory(AVAudioSessionCategoryPlayback, error = null)
        session.setActive(true, error = null)
    }

    // Convert ByteArray to NSData safely (no named arg ambiguity)
    private fun byteArrayToNSData(bytes: ByteArray): NSData = bytes.usePinned { pinned ->
        NSData.create(bytes = pinned.addressOf(0), length = bytes.size.toULong())
    }

    private fun writeTempMp3(data: NSData): String {
        val dir = NSTemporaryDirectory() // usually ends with /
        val path = dir + "kmp_sound_${NSUUID().UUIDString}.mp3"
        data.writeToFile(path, atomically = true)
        return path
    }

    private fun cleanupTempFile() {
        tempFilePath?.let { path ->
            NSFileManager.defaultManager.removeItemAtPath(path, null)
        }
        tempFilePath = null
    }

    // Optional helpers if later needed:
    fun setVolume(volume: Float) = dispatch_async(dispatch_get_main_queue()) { player?.volume = volume }
    fun enableLoop(loop: Boolean) =
        dispatch_async(dispatch_get_main_queue()) { player?.numberOfLoops = if (loop) -1 else 0 }

    fun isPlaying(): Boolean = player?.playing == true
}
