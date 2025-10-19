package vm.words.ua.navigation

sealed class Screen(val route: String) {
    data object Loader : Screen("loader")
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object TelegramLogin : Screen("telegram_login")
    data object ConfirmSignUp : Screen("confirm_signup")

    data object Home : Screen("home")
    data object Settings : Screen("settings")
    data object PlayList : Screen("playlist")
    data object PlayListFilter : Screen("playlist_filter")
}
