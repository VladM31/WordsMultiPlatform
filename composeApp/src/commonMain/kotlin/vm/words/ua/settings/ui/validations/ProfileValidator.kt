package vm.words.ua.settings.ui.validations

/**
 * Правила бэкенда (`AuthWords`): пароль 8..60 символов и не более 72 байт в UTF-8,
 * имя и фамилия 2..60 символов.
 * Функции возвращают текст ошибки или `null`, если значение подходит.
 */
private const val PASSWORD_MIN = 8
private const val PASSWORD_MAX = 60
private const val PASSWORD_MAX_BYTES = 72
private const val NAME_MIN = 2
private const val NAME_MAX = 60

fun validatePassword(label: String, value: String): String? {
    if (value.length < PASSWORD_MIN || value.length > PASSWORD_MAX) {
        return "$label must be between $PASSWORD_MIN and $PASSWORD_MAX characters"
    }
    if (value.encodeToByteArray().size > PASSWORD_MAX_BYTES) {
        return "$label is too long: at most $PASSWORD_MAX_BYTES bytes"
    }
    return null
}

fun validateName(label: String, value: String): String? {
    val trimmed = value.trim()
    if (trimmed.length < NAME_MIN || trimmed.length > NAME_MAX) {
        return "$label must be between $NAME_MIN and $NAME_MAX characters"
    }
    return null
}
