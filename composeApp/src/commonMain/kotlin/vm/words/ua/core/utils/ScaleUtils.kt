package vm.words.ua.core.utils

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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

