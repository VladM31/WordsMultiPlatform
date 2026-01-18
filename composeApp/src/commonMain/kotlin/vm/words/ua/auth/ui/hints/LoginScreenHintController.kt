package vm.words.ua.auth.ui.hints

import androidx.compose.runtime.Composable
import vm.words.ua.core.ui.hints.createDefaultHintController
import vm.words.ua.utils.hints.domain.models.HintPosition
import vm.words.ua.utils.hints.ui.utils.ViewHintStep

private const val LAST_INDEX = 6
private const val VALUE_KEY = "login_screen_v1"

enum class LoginScreenHintStep(
    override val text: String,
    override val position: HintPosition,
) : ViewHintStep {
    PHONE_NUMBER_OR_EMAIL(
        text = "Input your phone number with country code. E.g., 11234567890, 3801234567890 or your email address.",
        position = HintPosition.BOTTOM,
    ),
    PASSWORD(
        text = "Password must be between 8 and 60 characters long and include a mix of letters and numbers.",
        position = HintPosition.TOP,
    ),
    LOGIN_BUTTON(
        text = "Tap here to log in to your account after entering your credentials.",
        position = HintPosition.TOP,
    ),
    SIGN_UP_LINK(
        text = "Don't have an account? Click here to sign up and create a new account.",
        position = HintPosition.TOP,
    ),
    TELEGRAM_LOGIN_BUTTON(
        text = "You can also log in using your Telegram account for quick access.",
        position = HintPosition.TOP,
    ),
    GMAIL_LOGIN_BUTTON(
        text = "Alternatively, log in with your Gmail account for convenience.",
        position = HintPosition.TOP,
    ),

    UNDEFINED(
        text = "",
        position = HintPosition.CENTER,
    );
}

data class LoginScreenHintController(
    val currentStep: ViewHintStep,
    val doNext: () -> Unit
)

@Composable
fun createLoginScreenHintController(): LoginScreenHintController {
    val defaultController = createDefaultHintController(
        key = VALUE_KEY,
        lastIndex = LAST_INDEX,
        undefinedStep = LoginScreenHintStep.UNDEFINED,
    ) {
        LoginScreenHintStep.entries[it]
    }

    return LoginScreenHintController(defaultController.currentStep, defaultController.doNext)
}