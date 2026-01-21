package vm.words.ua.settings.ui.validations

import kotlinx.coroutines.flow.StateFlow
import vm.words.ua.settings.ui.actions.ProfileAction
import vm.words.ua.utils.validation.Validator
import vm.words.ua.utils.validation.schemes.ValidationScheme
import vm.words.ua.utils.validation.schemes.length
import vm.words.ua.utils.validation.schemes.notBlank

val profileDeleteValidator: (StateFlow<ProfileAction.DeleteAccount>) -> Validator<ProfileAction.DeleteAccount> =
    { action ->
        Validator(action).apply {
            add(
                ProfileAction.DeleteAccount::password,
                ValidationScheme.stringSchema("Password")
                    .length(8, 60)
                    .notBlank()
            )
        }
    }
