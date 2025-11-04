package vm.words.ua.core.platform

enum class AppPlatform {
    ANDROID,
    IOS,
    JVM,
    JS,
    WASM,
    UNKNOWN
}

expect fun currentPlatform(): AppPlatform

val AppPlatform.isWeb: Boolean
    get() = this == AppPlatform.JS || this == AppPlatform.WASM

val AppPlatform.isPhone: Boolean
    get() = this == AppPlatform.ANDROID || this == AppPlatform.IOS



