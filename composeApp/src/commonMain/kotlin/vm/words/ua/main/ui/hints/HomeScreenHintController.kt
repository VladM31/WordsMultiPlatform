package vm.words.ua.main.ui.hints

import androidx.compose.runtime.Composable
import vm.words.ua.core.ui.hints.createDefaultHintController
import vm.words.ua.hints.domain.models.HintPosition
import vm.words.ua.hints.ui.utils.ViewHintStep
import vm.words.ua.utils.storage.AppStorage

private const val LAST_INDEX = 7
private const val VALUE_KEY = "value"
private val STORE = AppStorage.create(
    name = "home_screen_hint_controller_v1"
)

enum class HomeScreenHintStep(
    override val text: String,
    override val position: HintPosition,
) : ViewHintStep {
    WORDS_BUTTON(
        text = "Words: Opens the standard word library",
        position = HintPosition.BOTTOM,
    ),
    MY_WORDS_BUTTON(
        text = "My words: Shows the words currently being learned",
        position = HintPosition.BOTTOM,
    ),
    ADD_WORD_BUTTON(
        text = "Add word: Allows you to manually add new words",
        position = HintPosition.BOTTOM,
    ),
    INSTRUCTION_BUTTON(
        text = "Instruction: Opens the instruction page",
        position = HintPosition.BOTTOM,
    ),

    PLAY_LIST_BUTTON(
        text = "Playlists: Create and view personal playlists",
        position = HintPosition.TOP,
    ),
    HOME_BUTTON(
        text = "Home: Start page of the app",
        position = HintPosition.TOP,
    ),
    SETTINGS_BUTTON(
        text = "Settings: Adjust profile and app preferences",
        position = HintPosition.TOP,
    ),

    UNDEFINED(
        text = "",
        position = HintPosition.CENTER,
    );
}

data class HomeScreenHintController(
    val currentStep: ViewHintStep,
    val doNext: () -> Unit
)

@Composable
fun createHomeScreenHintController(): HomeScreenHintController {
    val defaultController = createDefaultHintController(
        storage = STORE,
        key = VALUE_KEY,
        lastIndex = LAST_INDEX,
        undefinedStep = HomeScreenHintStep.UNDEFINED,
    ) {
        HomeScreenHintStep.entries[it]
    }

    return HomeScreenHintController(defaultController.currentStep, defaultController.doNext)
}