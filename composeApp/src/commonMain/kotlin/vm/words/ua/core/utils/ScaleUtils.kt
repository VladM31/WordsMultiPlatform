package vm.words.ua.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import vm.words.ua.core.domain.models.enums.DeviceFormat
import vm.words.ua.core.platform.currentOrientation
import vm.words.ua.core.platform.isLandscape

/**
 * Calculate scale factor based on screen width
 * @param maxWidth Maximum width of the container
 * @return Scale factor for UI elements
 */
fun getScaleFactor(maxWidth: Dp): Float {
    return when {
        maxWidth >= 1920.dp -> 1.8f // 4K
        maxWidth >= 1280.dp -> 1.5f // FullHD
        maxWidth >= 800.dp -> 1.2f  // Big tablets
        maxWidth <= 380.dp -> 1f  // Small phones
        else -> 1.0f // Just screen
    }
}

fun getWidthDeviceFormat(maxWidth: Dp): DeviceFormat {
    return when {
        maxWidth >= 1920.dp -> DeviceFormat.FOUR_K
        maxWidth >= 1280.dp -> DeviceFormat.FULL_HD
        maxWidth >= 800.dp -> DeviceFormat.BIG_TABLET
        maxWidth <= 380.dp -> DeviceFormat.SMALL_PHONE
        else -> DeviceFormat.PHONE
    }
}

@Composable
fun getScaleFactor(): Float {
    val maxWidth = appWidthDp()
    return remember(maxWidth) {
        getScaleFactor(maxWidth)
    }
}

@Composable
fun getWidthDeviceFormat(): DeviceFormat {
    val maxWidth = appWidthDp()
    return remember(maxWidth) {
        getWidthDeviceFormat(maxWidth)
    }
}

@Composable
fun isNotPhoneDeviceFormatOrLandscape(): Boolean {
    val format = getWidthDeviceFormat()
    val isLandscape = currentOrientation().isLandscape
    return format.isPhone.not() || isLandscape
}

