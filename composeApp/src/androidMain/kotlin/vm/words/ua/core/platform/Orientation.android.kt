package vm.words.ua.core.platform

import android.content.res.Configuration
import android.content.res.Resources

actual fun currentOrientation(): Orientation {
    return try {
        val orientation = Resources.getSystem().configuration.orientation
        when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> Orientation.LANDSCAPE
            Configuration.ORIENTATION_PORTRAIT -> Orientation.PORTRAIT
            Configuration.ORIENTATION_SQUARE -> Orientation.SQUARE
            else -> Orientation.UNKNOWN
        }
    } catch (e: Throwable) {
        Orientation.UNKNOWN
    }
}
