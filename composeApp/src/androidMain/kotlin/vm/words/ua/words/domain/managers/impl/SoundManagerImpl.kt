package vm.words.ua.words.domain.managers.impl

import android.media.AudioAttributes
import android.media.MediaDataSource
import android.media.MediaPlayer
import vm.words.ua.core.domain.models.ByteContent
import vm.words.ua.words.domain.managers.SoundManager

actual class SoundManagerImpl actual constructor() : SoundManager {

    private var mediaPlayer: MediaPlayer? = null
    private var dataSource: ByteArrayMediaDataSource? = null

    override suspend fun playSound(byteContent: ByteContent) {
        stopSound()

        try {
            val mp = MediaPlayer()
            mediaPlayer = mp

            mp.setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            )

            val ds = ByteArrayMediaDataSource(byteContent.bytes)
            dataSource = ds

            mp.setOnErrorListener { _, what, extra ->
                println("MediaPlayer Error: what=$what, extra=$extra")
                cleanup()
                true
            }
            mp.setOnCompletionListener {
                println("Sound playback completed")
                cleanup()
            }
            mp.setOnPreparedListener { player ->
                println("Sound prepared, duration: ${player.duration} ms")
                player.start()
            }

            mp.setDataSource(ds)
            mp.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error playing sound: ${e.message}")
            cleanup()
        }
    }

    override suspend fun stopSound() {
        try {
            mediaPlayer?.let { mp ->
                try { mp.setOnCompletionListener(null) } catch (_: Exception) {}
                try { mp.setOnPreparedListener(null) } catch (_: Exception) {}
                try { mp.setOnErrorListener(null) } catch (_: Exception) {}
                try { if (mp.isPlaying) mp.stop() } catch (_: Exception) {}
                try { mp.reset() } catch (_: Exception) {}
                try { mp.release() } catch (_: Exception) {}
            }
        } catch (_: Exception) {
        } finally {
            mediaPlayer = null
            try { dataSource?.close() } catch (_: Exception) {}
            dataSource = null
        }
    }

    private fun cleanup() {
        try {
            mediaPlayer?.let { mp ->
                try { mp.setOnCompletionListener(null) } catch (_: Exception) {}
                try { mp.setOnPreparedListener(null) } catch (_: Exception) {}
                try { mp.setOnErrorListener(null) } catch (_: Exception) {}
                try { if (mp.isPlaying) mp.stop() } catch (_: Exception) {}
                try { mp.reset() } catch (_: Exception) {}
                try { mp.release() } catch (_: Exception) {}
            }
        } catch (_: Exception) {
        } finally {
            mediaPlayer = null
            try { dataSource?.close() } catch (_: Exception) {}
            dataSource = null
        }
    }

    private class ByteArrayMediaDataSource(private val bytes: ByteArray) : MediaDataSource() {
        override fun readAt(position: Long, buffer: ByteArray, offset: Int, size: Int): Int {
            if (position >= bytes.size) return -1
            val pos = position.toInt()
            val available = bytes.size - pos
            val toCopy = minOf(size, available)
            if (toCopy <= 0) return -1
            System.arraycopy(bytes, pos, buffer, offset, toCopy)
            return toCopy
        }

        override fun getSize(): Long = bytes.size.toLong()

        override fun close() {
            // nothing to release
        }
    }
}
