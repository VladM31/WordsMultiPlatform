package vm.words.ua.navigation

sealed class Screen(val route: String) {
    data object UpdateApp : Screen("update_app")
    data object Login : Screen("login")

    data object SignUpProvider : Screen("sign_up_provider")
    data object TelegramSignUp : Screen("sign_up_telegram")
    data object GoogleSignUp : Screen("sign_up_google")


    data object TelegramLogin : Screen("telegram_login")
    data object ConfirmSignUp : Screen("confirm_sign_up")

    data object WordList : Screen("word_list")
    data object WordDetails : Screen("word_details")

    data object Home : Screen("home")

    data object Instruction : Screen("instruction")


    data object Settings : Screen("settings")
    data object Policy : Screen("policy")
    data object Profile : Screen("profile")

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

    data object LeaningPlan : Screen("learning_plan")
    data object StatisticLearningHistory : Screen("statistic_learning_history")
    data object LearningHistoryList : Screen("learning_history_list")

    data object DefaultAddWord : Screen("default_add_word")
}
