package vm.words.ua.navigation

sealed class Screen(val route: String) {
    data object UpdateApp : Screen("update_app")
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object TelegramLogin : Screen("telegram_login")
    data object ConfirmSignUp : Screen("confirm_sign_up")

    data object WordList : Screen("word_list")
    data object WordDetails : Screen("word_details")

    data object Home : Screen("home")

    data object Instruction : Screen("instruction")


    data object Settings : Screen("settings")
    data object Policy : Screen("policy")

    data object PlayList : Screen("playlist")
    data object PlayListFilter : Screen("playlist_filter")
    data object PlayListDetails : Screen("playlist_details")

    data object ExerciseSelection : Screen("exercise_selection")

    data object OptionDescriptionByWords : Screen("option_description_by_words")
    data object OptionWordByDescription : Screen("option_word_by_description")
    data object OptionWordByOriginal : Screen("option_word_by_original")
    data object OptionWordByTranslate : Screen("option_word_by_translate")

    data object LetterMatchByDescription : Screen("letter_match_by_description")
    data object LetterMatchByTranslation : Screen("letter_match_by_translation")
    data object MatchWords : Screen("match_words")

    data object WriteByImageAndDescription : Screen("write_by_image_and_description")
    data object WriteByImageAndTranslation : Screen("write_by_image_and_translation")

    data object WordFilter : Screen("word_filter")
    data object UserWords : Screen("user_words")
    data object UserWordsFilter : Screen("user_words_filter")
    data object PinUserWords : Screen("pin_user_words")
}
