package vm.words.ua.core.net.client

import vm.words.ua.core.net.requests.AudioGenerationRequest
import vm.words.ua.core.net.requests.SaveFileRequest
import vm.words.ua.core.net.responds.UploadRespond

interface FileApiClient {
    suspend fun uploadFile(request: SaveFileRequest): UploadRespond

    suspend fun textToAudioFile(request: AudioGenerationRequest): UploadRespond
}