package vm.words.ua.core.domain.models.enums

enum class DeviceFormat(
    val isPhone: Boolean = false
) {
    FOUR_K,
    FULL_HD,
    BIG_TABLET,
    SMALL_PHONE(isPhone = true),
    PHONE(isPhone = true)
}