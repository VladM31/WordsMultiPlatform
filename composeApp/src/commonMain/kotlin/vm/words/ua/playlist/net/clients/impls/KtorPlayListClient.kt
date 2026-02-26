package vm.words.ua.playlist.net.clients.impls

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.net.responds.PagedRespond
import vm.words.ua.playlist.domain.models.filters.DeletePlayListFilter
import vm.words.ua.playlist.domain.models.filters.PlayListCountFilter
import vm.words.ua.playlist.domain.models.filters.PlayListFilter
import vm.words.ua.playlist.net.clients.PlayListClient
import vm.words.ua.playlist.net.requests.*
import vm.words.ua.playlist.net.responds.*

class KtorPlayListClient(
    private val client: HttpClient
) : PlayListClient {

    private val baseUrl: String by lazy {
        "${AppRemoteConfig.baseUrl}/words-api/play-list"
    }

    override suspend fun countBy(token: String, filter: PlayListCountFilter): PagedRespond<PlayListCountRespond> {
        return try {
            val response = client.get("$baseUrl/count") {
                header("Authorization", token)
                filter.toQueryMap().forEach { parameter(it.key, it.value) }
            }

            response.body<PagedRespond<PlayListCountRespond>>()
        } catch (e: Exception) {
            e.printStackTrace()
            PagedRespond.empty()
        }
    }

    override suspend fun findBy(token: String, filter: PlayListFilter): PagedRespond<PlayListRespond> {
        return try {
            val response = client.get("$baseUrl") {
                header("Authorization", token)
                filter.toQueryMap().forEach { parameter(it.key, it.value) }
            }

            response.body<PagedRespond<PlayListRespond>>()
        } catch (e: Exception) {
            e.printStackTrace()
            PagedRespond.empty()
        }
    }

    override suspend fun findBy(
        token: String,
        filter: PublicPlayListGetRequest
    ): PagedRespond<PlayListRespond> {
        return try {
            val response = client.get("$baseUrl/public") {
                header("Authorization", token)
                filter.toQueryMap().forEach { parameter(it.key, it.value) }
            }

            response.body<PagedRespond<PlayListRespond>>()
        } catch (e: Exception) {
            e.printStackTrace()
            PagedRespond.empty()
        }
    }

    override suspend fun countBy(
        token: String,
        filter: PublicPlayListCountRequest
    ): PagedRespond<PublicPlayListCountRespond> {
        val response = client.get("$baseUrl/count/public") {
            header("Authorization", token)
            filter.toQueryMap().forEach { parameter(it.key, it.value) }
        }
        return response.body<PagedRespond<PublicPlayListCountRespond>>()
    }

    override suspend fun countRandom(token: String): PlayListCountRespond? {
        val response = client.get("$baseUrl/random") {
            header("Authorization", token)
        }
        if (response.status == HttpStatusCode.NotFound) {
            return null
        }
        if (response.status.isSuccess().not()) {
            val body = response.runCatching {
                bodyAsText()
            }.getOrDefault("Unknown error")
            throw Exception(body)
        }
        return response.body<PlayListCountRespond>()
    }

    override suspend fun getAssignedPlaylists(token: String): Set<AssignedPlaylistRespond> {
        val response = client.get("$baseUrl/assigned") {
            header("Authorization", token)
        }
        return response.body<Set<AssignedPlaylistRespond>>()
    }

    override suspend fun assignPlayLists(
        token: String,
        req: AssignPlayListsRequest
    ): List<PlaylistIdRespond> {
        val response = client.post("$baseUrl/assign") {
            header("Authorization", token)
            contentType(ContentType.Application.Json)
            setBody(req)
        }
        if (response.status.isSuccess()) {
            return response.body()
        }
        val body = response.runCatching {
            bodyAsText()
        }.getOrDefault("Unknown error")

        throw Exception("Failed to assign playlists, status: ${response.status}, body: $body")
    }

    override suspend fun save(token: String, playLists: List<SavePlayListRequest>) {
        try {
            client.post("$baseUrl") {
                header("Authorization", token)
                contentType(ContentType.Application.Json)
                setBody(playLists)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun update(token: String, playLists: List<UpdatePlayListRequest>) {
        try {
            client.put("$baseUrl") {
                header("Authorization", token)
                contentType(ContentType.Application.Json)
                setBody(playLists)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun updateGrades(token: String, grades: List<PlayListGradeRequest>) {
        try {
            client.put("$baseUrl/grades") {
                header("Authorization", token)
                contentType(ContentType.Application.Json)
                setBody(grades)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun delete(token: String, filter: DeletePlayListFilter) {
        try {
            client.delete("$baseUrl/delete") {
                header("Authorization", token)
                filter.toQueryMap().forEach {
                    parameter(it.key, it.value)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



}
