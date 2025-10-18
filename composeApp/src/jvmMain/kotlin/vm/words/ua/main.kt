package vm.words.ua

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "WordsMultiPlatform",
    ) {
        App()
    }
}