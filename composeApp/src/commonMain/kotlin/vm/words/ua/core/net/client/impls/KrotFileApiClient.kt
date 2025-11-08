package vm.words.ua.core.net.client.impls

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.net.client.FileApiClient
import vm.words.ua.core.net.requests.AudioGenerationRequest
import vm.words.ua.core.net.requests.SaveFileRequest
import vm.words.ua.core.net.responds.UploadRespond

class KrotFileApiClient(
    private val userCacheManager: UserCacheManager,
    private val httpClient: HttpClient,
    private val json: Json = Json { ignoreUnknownKeys = true }
) : FileApiClient {

    private val saveFileUrl by lazy { "${AppRemoteConfig.baseUrl}/files/upload" }
    private val textToAudioUrl by lazy { "${AppRemoteConfig.baseUrl}/files/text-to-audio" }

    override suspend fun uploadFile(request: SaveFileRequest): UploadRespond {
        return upload(request.content, request.fileName)
    }

    override suspend fun textToAudioFile(request: AudioGenerationRequest): UploadRespond {
        val response: HttpResponse = httpClient.post(textToAudioUrl) {
            header(HttpHeaders.Authorization, "Bearer ${userCacheManager.token.value}")
            contentType(ContentType.Application.Json)
            setBody(json.encodeToString(request))
        }

        if (!response.status.isSuccess()) {
            throw RuntimeException("Error: ${response.status.value} ${response.status.description}")
        }

        val bodyString = response.bodyAsText()
        return json.decodeFromString(bodyString)
    }

    suspend fun upload(fileContent: ByteArray, filename: String): UploadRespond {
        val mimeType = guessMimeType(filename)

        val response: HttpResponse = httpClient.submitFormWithBinaryData(
            url = saveFileUrl,
            formData = formData {
                append("file", fileContent, Headers.build {
                    append(HttpHeaders.ContentType, mimeType)
                    append(HttpHeaders.ContentDisposition, "filename=$filename")
                })
            }
        ) {
            header(HttpHeaders.Authorization, "Bearer ${userCacheManager.token.value}")
        }

        if (!response.status.isSuccess()) {
            throw RuntimeException("Error: ${response.status.value} ${response.status.description}")
        }

        val bodyString = response.bodyAsText()
        return json.decodeFromString(bodyString)
    }

    private fun guessMimeType(filename: String): String {
        val lower = filename.lowercase()
        return when {
            lower.endsWith(".webp") -> "image/webp"
            lower.endsWith(".jpg") || lower.endsWith(".jpeg") -> "image/jpeg"
            lower.endsWith(".png") -> "image/png"
            lower.endsWith(".wav") -> "audio/wav"
            lower.endsWith(".mp3") -> "audio/mpeg"
            else -> "application/octet-stream"
        }
    }
}