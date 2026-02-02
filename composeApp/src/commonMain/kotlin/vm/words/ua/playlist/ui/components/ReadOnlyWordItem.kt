package vm.words.ua.playlist.ui.components

import androidx.compose.runtime.Composable
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.words.domain.models.Word
import vm.words.ua.words.ui.bundles.WordDetailsBundle
import vm.words.ua.words.ui.components.WordItem


@Composable
fun ReadOnlyWordItem(
    word: Word,
    navController: SimpleNavController,
) {
    WordItem(word = word, notSelectedIcon = null, isSelected = false, onSelect = {}, onOpen = {
        navController.navigate(
            Screen.WordDetails,
            WordDetailsBundle(
                word = word
            )
        )
    })
}



