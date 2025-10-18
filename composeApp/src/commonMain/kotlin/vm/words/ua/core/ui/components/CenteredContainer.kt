package vm.words.ua.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Компонент-контейнер, который центрирует содержимое с заданной максимальной шириной
 *
 * @param maxWidth максимальная ширина содержимого. Если ширина экрана больше, содержимое будет центрировано
 * @param modifier модификатор для контейнера
 * @param content содержимое, которое нужно отобразить
 */
@Composable
fun CenteredContainer(
    maxWidth: Dp = 600.dp,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier.widthIn(max = maxWidth)
        ) {
            content()
        }
    }
}
