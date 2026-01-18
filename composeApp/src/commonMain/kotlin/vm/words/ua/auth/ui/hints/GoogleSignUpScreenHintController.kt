package vm.words.ua.auth.ui.hints

import androidx.compose.runtime.Composable
import vm.words.ua.core.ui.hints.createDefaultHintController
import vm.words.ua.utils.hints.domain.models.HintPosition
import vm.words.ua.utils.hints.ui.utils.ViewHintStep


private const val VALUE_KEY = "sign_up_screen_v2"

enum class GoogleSignUpScreenHintStep(
    override val text: String,
    override val position: HintPosition,
) : ViewHintStep {
    PRIVACY_POLICY(
        text = "Privacy Policy: Learn how we collect and protect your data.",
        position = HintPosition.BOTTOM,
    ),
    FIRST_NAME(
        text = "Enter your first name.",
        position = HintPosition.BOTTOM,
    ),
    LAST_NAME(
        text = "Enter your last name.",
        position = HintPosition.BOTTOM,
    ),
    PASSWORD(
        text = "Password must be between 8 and 60 characters long and include a mix of letters and numbers.",
        position = HintPosition.BOTTOM,
    ),
    CURRENCY_SELECTION(
        text = "Select your preferred currency for transactions and pricing.",
        position = HintPosition.TOP,
    ),
    AGREEMENT_CHECKBOX(
        text = "Agree to our terms and conditions to proceed with account creation.",
        position = HintPosition.TOP,
    ),
    SIGN_UP_BUTTON(
        text = "Tap here to create your account after filling in all required fields.",
        position = HintPosition.TOP,
    ),

    UNDEFINED(
        text = "",
        position = HintPosition.CENTER,
    );
}

data class GoogleSignUpScreenHintController(
    val currentStep: ViewHintStep,
    val doNext: () -> Unit
)


@Composable
fun createGoogleSignUpScreenHintController(): GoogleSignUpScreenHintController {
    val defaultController = createDefaultHintController(
        key = VALUE_KEY,
        lastIndex = GoogleSignUpScreenHintStep.entries.size - 1,
        undefinedStep = GoogleSignUpScreenHintStep.UNDEFINED,
    ) {
        GoogleSignUpScreenHintStep.entries[it]
    }

    return GoogleSignUpScreenHintController(defaultController.currentStep, defaultController.doNext)
}