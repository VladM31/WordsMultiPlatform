package vm.words.ua.core.net

/**
 * Точка подключения реакции на 401 (токен отсутствует, недействителен или просрочен).
 * Callback выставляется из UI слоя, где доступен кэш пользователя.
 */
object UnauthorizedHandler {
    var onUnauthorized: (() -> Unit)? = null
}
