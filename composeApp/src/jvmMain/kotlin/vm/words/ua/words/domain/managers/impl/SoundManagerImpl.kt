package vm.words.ua.words.domain.managers.impl

import vm.words.ua.core.domain.models.ByteContent
import vm.words.ua.words.domain.managers.SoundManager
import javax.sound.sampled.*
import java.io.ByteArrayInputStream

actual class SoundManagerImpl actual constructor() : SoundManager {

    private var sourceDataLine: SourceDataLine? = null
    private var audioThread: Thread? = null
    @Volatile private var isPlaying = false

    override suspend fun playSound(byteContent: ByteContent) {
        stopSound()

        try {
            val rawStream = AudioSystem.getAudioInputStream(ByteArrayInputStream(byteContent.bytes))
            val baseFormat = rawStream.format

            // Готовим целевой PCM-формат (16-bit signed, little-endian)
            val channels = if (baseFormat.channels <= 0) 2 else baseFormat.channels
            val decodedFormat = AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.sampleRate,
                16,
                channels,
                channels * 2,
                baseFormat.sampleRate,
                false
            )

            val pcmStream = if (baseFormat.encoding != AudioFormat.Encoding.PCM_SIGNED || baseFormat.sampleSizeInBits != 16) {
                AudioSystem.getAudioInputStream(decodedFormat, rawStream)
            } else rawStream

            playPcmStream(pcmStream, decodedFormat)
        } catch (e: UnsupportedAudioFileException) {
            e.printStackTrace()
            // Формат не распознан — ничего не играем, чтобы не было шума
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun playPcmStream(stream: AudioInputStream, format: AudioFormat) {
        val info = DataLine.Info(SourceDataLine::class.java, format)
        val line = AudioSystem.getLine(info) as SourceDataLine
        line.open(format)
        line.start()

        sourceDataLine = line
        isPlaying = true

        audioThread = Thread {
            stream.use { ais ->
                val frameSize = format.frameSize
                val buffer = ByteArray(4096 * frameSize.coerceAtLeast(1))
                var carryLen = 0
                val carry = ByteArray(frameSize)

                try {
                    while (isPlaying) {
                        val read = ais.read(buffer, carryLen, buffer.size - carryLen)
                        if (read == -1) break

                        var total = carryLen + read
                        val aligned = total - (total % frameSize)
                        if (aligned > 0) {
                            line.write(buffer, 0, aligned)
                            // Переносим хвост в начало буфера
                            val rem = total - aligned
                            if (rem > 0) {
                                System.arraycopy(buffer, aligned, buffer, 0, rem)
                            }
                            carryLen = rem
                        } else {
                            carryLen = total
                        }
                    }
                    // Дреним остатки по фреймам если что-то осталось, но полного фрейма нет — игнорируем
                    line.drain()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    isPlaying = false
                    try { line.stop() } catch (_: Exception) {}
                    try { line.close() } catch (_: Exception) {}
                    sourceDataLine = null
                }
            }
        }.apply { isDaemon = true }

        audioThread?.start()
        // Ждать в suspend не будем — пусть играет асинхронно; управление вернётся сразу
    }

    override suspend fun stopSound() {
        isPlaying = false
        try {
            sourceDataLine?.stop()
            sourceDataLine?.close()
        } catch (_: Exception) {}
        sourceDataLine = null
        try { audioThread?.interrupt() } catch (_: Exception) {}
        audioThread = null
    }
}
