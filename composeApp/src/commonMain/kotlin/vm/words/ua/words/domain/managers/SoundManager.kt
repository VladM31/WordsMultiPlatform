package vm.words.ua.words.domain.managers

import vm.words.ua.core.domain.models.ByteContent

interface SoundManager {

    suspend fun playSound(byteContent: ByteContent)

    suspend fun stopSound()
}

