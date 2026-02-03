package vm.words.ua.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


fun rememberIconSize(scaleFactor: Float): Dp {
    return (45 * scaleFactor).dp
}

fun getIconButtonSize(scaleFactor: Float) : Dp{
    val iconSize = rememberIconSize(scaleFactor)
    return iconSize * 1.2f
}

fun rememberFontSize(scaleFactor: Float): TextUnit {
    return (24 * scaleFactor).sp
}


@Composable
fun rememberIconSize(): Dp {
    val scaleFactor = rememberScaleFactor()
    return remember(scaleFactor) {
        (45 * scaleFactor).dp
    }
}

@Composable
fun getIconButtonSize() : Dp{
    val iconSize = rememberIconSize(rememberScaleFactor())
    return iconSize * 1.2f
}

@Composable
fun rememberFontSize(): TextUnit {
    val scale = rememberScaleFactor()
    return remember(scale) {
        (24 * scale).sp
    }
}

@Composable
fun rememberLabelFontSize(): TextUnit {
    val fontSize = rememberFontSize()
    return remember(fontSize) {
        fontSize * 0.7f
    }
}


@Composable
fun getImageSize() : Dp{
    return (300 * rememberScaleFactor()).dp
}