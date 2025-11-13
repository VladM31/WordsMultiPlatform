package vm.words.ua.words.domain.managers.impl

import vm.words.ua.core.domain.models.ByteContent
import vm.words.ua.words.domain.managers.SoundManager

actual class SoundManagerImpl actual constructor() : SoundManager {

    override suspend fun playSound(byteContent: ByteContent) {
        // TODO: Implement AVAudioPlayer-based playback. No-op for now on iOS.
    }

    override suspend fun stopSound() {
        // TODO: Implement stop for AVAudioPlayer. No-op for now on iOS.
    }
}
