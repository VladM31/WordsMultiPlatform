package vm.words.ua.core.platform

enum class Orientation {
    PORTRAIT,
    LANDSCAPE,
    SQUARE,
    UNKNOWN
}

expect fun currentOrientation(): Orientation

val Orientation.isLandscape: Boolean
    get() = this == Orientation.LANDSCAPE

