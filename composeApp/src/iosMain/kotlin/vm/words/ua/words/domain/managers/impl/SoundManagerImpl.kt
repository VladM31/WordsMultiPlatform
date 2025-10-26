package vm.words.ua.words.domain.managers.impl

import vm.words.ua.core.domain.models.ByteContent
import vm.words.ua.words.domain.managers.SoundManager

actual class SoundManagerImpl actual constructor() : SoundManager {

    override suspend fun playSound(byteContent: ByteContent) {
        // iOS implementation using AVAudioPlayer
        playAudioFromBytes(byteContent.bytes)
    }

    override suspend fun stopSound() {
        stopAudio()
    }

    private external fun playAudioFromBytes(bytes: ByteArray)
    private external fun stopAudio()
}

