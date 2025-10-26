package vm.words.ua.core.utils

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import vm.words.ua.core.domain.models.enums.DeviceFormat

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
        maxWidth <= 360.dp -> 0.8f  // Small phones
        else -> 1.0f // Just screen
    }
}

fun getWidthDeviceFormat(maxWidth: Dp) : DeviceFormat {
    return when {
        maxWidth >= 1920.dp -> DeviceFormat.FOUR_K
        maxWidth >= 1280.dp -> DeviceFormat.FULL_HD
        maxWidth >= 800.dp -> DeviceFormat.BIG_TABLET
        maxWidth <= 360.dp -> DeviceFormat.SMALL_PHONE
        else -> DeviceFormat.PHONE
    }
}

