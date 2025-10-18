package vm.words.ua.navigation

sealed class Screen(val route: String) {
    data object Loader : Screen("loader")
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object TelegramLogin : Screen("telegram_login")
    data object ConfirmSignUp : Screen("confirm_signup")
}

