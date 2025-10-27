package vm.words.ua.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


fun getIconSize(scaleFactor: Float) : Dp{
    return (45 * scaleFactor).dp
}

fun getIconButtonSize(scaleFactor: Float) : Dp{
    val iconSize = getIconSize(scaleFactor)
    return iconSize * 1.2f
}

fun getFontSize(scaleFactor: Float) : TextUnit{
    return (24 * scaleFactor).sp
}


@Composable
fun getIconSize() : Dp{
    return (45 * getScaleFactor()).dp
}

@Composable
fun getIconButtonSize() : Dp{
    val iconSize = getIconSize(getScaleFactor())
    return iconSize * 1.2f
}

@Composable
fun getFontSize() : TextUnit{
    return (24 * getScaleFactor()).sp
}