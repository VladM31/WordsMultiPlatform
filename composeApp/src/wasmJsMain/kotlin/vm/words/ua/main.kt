package vm.words.ua

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("() => { if (window.hideAppLoader) window.hideAppLoader(); }")
private external fun hideAppLoader()

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    hideAppLoader()
    ComposeViewport(document.body!!) {
        App()
    }
}

