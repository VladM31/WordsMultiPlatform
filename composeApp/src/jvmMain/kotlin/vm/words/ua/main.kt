package vm.words.ua

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    val windowState = rememberWindowState(
        width = 900.dp,
        height = 600.dp
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "Words",
        state = windowState
    ) {
        App()
    }
}