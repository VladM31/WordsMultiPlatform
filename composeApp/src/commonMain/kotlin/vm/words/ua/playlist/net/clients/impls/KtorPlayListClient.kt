package vm.words.ua.playlist.net.clients.impls

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.datetime.Instant
import vm.words.ua.core.domain.models.*
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.net.responds.PagedRespond
import vm.words.ua.playlist.domain.models.filters.DeletePlayListFilter
import vm.words.ua.playlist.domain.models.filters.PlayListCountFilter
import vm.words.ua.playlist.domain.models.filters.PlayListFilter
import vm.words.ua.playlist.domain.models.PlayList.PinnedWord
import vm.words.ua.playlist.domain.models.PlayList
import vm.words.ua.playlist.domain.models.PlayListCount

import vm.words.ua.playlist.net.clients.PlayListClient
import vm.words.ua.playlist.net.models.requests.PlayListGradeRequest
import vm.words.ua.playlist.net.models.requests.SavePlayListRequest
import vm.words.ua.playlist.net.models.requests.UpdatePlayListRequest
import vm.words.ua.playlist.net.models.responses.PlayListCountRespond
import vm.words.ua.playlist.net.models.responses.PlayListRespond
import vm.words.ua.words.domain.models.UserWord
import vm.words.ua.words.domain.models.Word

class KtorPlayListClient(
    private val client: HttpClient
) : PlayListClient {

    private val baseUrl: String = AppRemoteConfig.baseUrl

    override suspend fun countBy(token: String, filter: PlayListCountFilter): PagedModels<PlayListCount> {
        return try {
            val response = client.get("$baseUrl/words-api/play-list/count") {
                header("Authorization", token)
                filter.toQueryMap().forEach { parameter(it.key, it.value) }
            }

            val pagedResponse = response.body<PagedRespond<PlayListCountRespond>>()
            PagedModels.of(pagedResponse) {
                PlayListCount(
                    id = it.id,
                    name = it.name,
                    createdAt = Instant.parse(it.createdAt),
                    count = it.count
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            PagedModels.empty()
        }
    }

    override suspend fun findBy(token: String, filter: PlayListFilter): PagedModels<PlayList> {
        return try {
            val response = client.get("$baseUrl/words-api/play-list") {
                header("Authorization", token)
                filter.toQueryMap().forEach { parameter(it.key, it.value) }
            }

            val pagedResponse = response.body<PagedRespond<PlayListRespond>>()
            PagedModels.of(pagedResponse) { it.toPlayList() }
        } catch (e: Exception) {
            e.printStackTrace()
            PagedModels.empty()
        }
    }

    override suspend fun save(token: String, playLists: List<SavePlayListRequest>) {
        try {
            client.post("$baseUrl/words-api/play-list") {
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
            client.put("$baseUrl/words-api/play-list") {
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
            client.put("$baseUrl/words-api/play-list/grades") {
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
            client.delete("$baseUrl/words-api/play-list/delete") {
                header("Authorization", token)
                parameter("id", filter.id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun PlayListRespond.toPlayList(): PlayList {
        return PlayList(
            id = id,
            name = name,
            createdAt = Instant.parse(createdAt),
            words = words.map { it.toPinnedWord() }
        )
    }

    private fun PlayListRespond.PinnedWordResponse.toPinnedWord(): PinnedWord {
        return PinnedWord(
            learningGrade = learningGrade,
            lastReadDate = Instant.parse(lastReadDate),
            userWord = word.toUserWord()
        )
    }

    private fun PlayListRespond.UserWordResponse.toUserWord(): UserWord {
        return UserWord(
            id = id,
            learningGrade = learningGrade,
            createdAt = Instant.parse(createdAt),
            lastReadDate = Instant.parse(lastReadDate),
            word = word.toWord()
        )
    }

    private fun PlayListRespond.WordResponse.toWord(): Word {
        return Word(
            id = id,
            original = original,
            lang = lang,
            translate = translate,
            translateLang = translateLang,
            cefr = cefr,
            description = description,
            category = category,
            soundLink = soundLink,
            imageLink = imageLink
        )
    }
}
