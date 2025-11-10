package vm.words.ua.core.domain.managers

import vm.words.ua.core.domain.models.ByteContent

interface ByteContentManager {

    suspend fun downloadByteContent(url: String, needAuth: Boolean = true): ByteContent
}