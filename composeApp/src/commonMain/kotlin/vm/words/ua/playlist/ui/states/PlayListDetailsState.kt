package vm.words.ua.playlist.ui.states

import vm.words.ua.playlist.domain.models.PlayList
import vm.words.ua.words.domain.models.UserWord

data class PlayListDetailsState(
    val name: String = "Loading...",
    val id: String = "",
    val selectedWords: Map<String, Int> = emptyMap(),
    val words: List<PlayList.PinnedWord> = emptyList(),
    val isEnd: Boolean = false
) {

    fun getWords(): List<UserWord> {
        if (selectedWords.isEmpty()) {
            return words.map { it.userWord }
        }
        return words
            .filter { selectedWords.containsKey(it.userWord.id) }
            .map { it.userWord }
    }
}

