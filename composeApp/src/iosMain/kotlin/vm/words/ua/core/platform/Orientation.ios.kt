package vm.words.ua.core.platform

import kotlinx.cinterop.*
import platform.UIKit.*

actual fun currentOrientation(): Orientation {
    return try {
        when (UIDevice.currentDevice.orientation) {
            UIDeviceOrientation.UIDeviceOrientationLandscapeLeft,
            UIDeviceOrientation.UIDeviceOrientationLandscapeRight -> Orientation.LANDSCAPE

            UIDeviceOrientation.UIDeviceOrientationPortrait,
            UIDeviceOrientation.UIDeviceOrientationPortraitUpsideDown -> Orientation.PORTRAIT

            else -> Orientation.UNKNOWN
        }
    } catch (e: Throwable) {
        Orientation.UNKNOWN
    }
}

