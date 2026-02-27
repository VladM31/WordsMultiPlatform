package vm.words.ua.core.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val GoogleIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "Google",
        defaultWidth = 24.0f.dp,
        defaultHeight = 24.0f.dp,
        viewportWidth = 24.0f,
        viewportHeight = 24.0f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(21.35f, 11.1f)
            horizontalLineTo(12.18f)
            verticalLineTo(13.83f)
            horizontalLineTo(18.69f)
            curveTo(18.36f, 17.64f, 15.19f, 19.27f, 12.19f, 19.27f)
            curveTo(8.36f, 19.27f, 5.0f, 16.25f, 5.0f, 12.0f)
            curveTo(5.0f, 7.9f, 8.2f, 4.73f, 12.2f, 4.73f)
            curveTo(15.29f, 4.73f, 17.1f, 6.7f, 17.1f, 6.7f)
            lineTo(19.0f, 4.72f)
            curveTo(19.0f, 4.72f, 16.56f, 2.0f, 12.1f, 2.0f)
            curveTo(6.42f, 2.0f, 2.03f, 6.8f, 2.03f, 12.0f)
            curveTo(2.03f, 17.05f, 6.16f, 22.0f, 12.25f, 22.0f)
            curveTo(17.6f, 22.0f, 21.5f, 18.33f, 21.5f, 12.91f)
            curveTo(21.5f, 11.76f, 21.35f, 11.1f, 21.35f, 11.1f)
            close()
        }
    }.build()
}

