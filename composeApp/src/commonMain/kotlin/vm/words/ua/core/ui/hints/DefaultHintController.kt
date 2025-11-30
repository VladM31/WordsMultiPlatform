package vm.words.ua.core.ui.hints

import androidx.compose.runtime.*
import vm.words.ua.utils.hints.ui.utils.ViewHintStep
import vm.words.ua.utils.storage.AppStorage
import vm.words.ua.utils.storage.managers.KeyValueStorage

private val STORE = AppStorage.create(
    name = "hint_controller"
)

data class DefaultHintController(
    val currentStep: ViewHintStep,
    val doNext: () -> Unit
)

@Composable
fun createDefaultHintController(
    storage: KeyValueStorage = STORE,
    key: String,
    lastIndex: Int,
    undefinedStep: ViewHintStep,
    toStep: (Int) -> ViewHintStep,
): DefaultHintController {
    var currentIndex by remember {
        mutableStateOf(storage.getInt(key, 0))
    }
    val currentState by remember(currentIndex) {
        if (currentIndex >= lastIndex) {
            mutableStateOf(undefinedStep)
        } else {
            mutableStateOf(toStep(currentIndex))
        }
    }


    return DefaultHintController(
        currentState
    ) {
        if (currentIndex < lastIndex) {
            currentIndex++
            storage.putInt(key, currentIndex)
            return@DefaultHintController
        }
    }
}