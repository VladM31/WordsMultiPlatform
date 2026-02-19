package vm.words.ua


import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import kotlinx.browser.window

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    window.onload = {
        val body = document.body ?: error("document.body is null")
        js("if (window.hideAppLoader) window.hideAppLoader()")
        ComposeViewport(body) {
            App()
        }
    }
}

