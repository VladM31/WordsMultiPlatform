package vm.words.ua.core.platform

import kotlinx.browser.window

actual fun currentOrientation(): Orientation {
    return try {
        val w = window.innerWidth
        val h = window.innerHeight
        when {
            w > h -> Orientation.LANDSCAPE
            h > w -> Orientation.PORTRAIT
            else -> Orientation.SQUARE
        }
    } catch (e: Throwable) {
        Orientation.UNKNOWN
    }
}
