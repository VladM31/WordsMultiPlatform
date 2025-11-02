package vm.words.ua.core.platform

import java.awt.Dimension
import java.awt.Toolkit

actual fun currentOrientation(): Orientation {
    return try {
        val size: Dimension = Toolkit.getDefaultToolkit().screenSize
        if (size.width > size.height) Orientation.LANDSCAPE
        else if (size.height > size.width) Orientation.PORTRAIT
        else Orientation.SQUARE
    } catch (e: Throwable) {
        Orientation.UNKNOWN
    }
}

