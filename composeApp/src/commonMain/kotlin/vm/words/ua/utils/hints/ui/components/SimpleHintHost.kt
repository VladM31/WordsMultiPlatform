package vm.words.ua.utils.hints.ui.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.utils.hints.domain.models.HintPosition
import vm.words.ua.utils.hints.ui.controllers.SimpleHintController
import vm.words.ua.utils.hints.ui.utils.LocalSimpleHintController
import vm.words.ua.utils.hints.ui.utils.LocalSimpleHintHostOffset

@Composable
fun SimpleHintHost(
    modifier: Modifier = Modifier,
    onNext: () -> Unit,
    content: @Composable () -> Unit,
) {
    val controller = remember { SimpleHintController() }
    var hostOffset by remember { mutableStateOf(Offset.Zero) }

    CompositionLocalProvider(
        LocalSimpleHintController provides controller,
        LocalSimpleHintHostOffset provides hostOffset
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .onGloballyPositioned { coordinates ->
                    hostOffset = coordinates.positionInWindow()
                }
        ) {
            content()

            val hint = controller.currentHint
            val anchorRect = controller.anchorRect

            if (hint == null) {
                return@Box
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        compositingStrategy =
                            CompositingStrategy.Offscreen
                    }
                    .clickable { onNext() }
                    .drawWithContent {

                        drawRect(AppTheme.Black.copy(alpha = 0.8f))
                        if (anchorRect == null) {
                            return@drawWithContent
                        }

                        drawIntoCanvas { canvas ->
                            val paint = Paint().apply {
                                blendMode = BlendMode.Clear
                            }

                            canvas.drawRoundRect(
                                anchorRect.left,
                                anchorRect.top,
                                anchorRect.right,
                                anchorRect.bottom,
                                radiusX = 12.dp.toPx(),
                                radiusY = 12.dp.toPx(),
                                paint = paint
                            )
                        }
                    }
            )

            HintBubble(
                text = hint.text,
                modifier = Modifier.align(
                    when (hint.position) {
                        HintPosition.TOP -> Alignment.TopCenter
                        HintPosition.CENTER -> Alignment.Center
                        HintPosition.BOTTOM -> Alignment.BottomCenter
                    }
                ).clickable { onNext() }
            )
        }
    }
}