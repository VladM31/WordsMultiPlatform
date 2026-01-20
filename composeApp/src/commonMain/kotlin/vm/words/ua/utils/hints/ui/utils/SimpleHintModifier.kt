package vm.words.ua.utils.hints.ui.utils

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.toSize
import vm.words.ua.utils.hints.domain.models.HintPosition

interface ViewHintStep {
    val text: String
    val position: HintPosition
}

fun Modifier.viewHint(
    step: ViewHintStep,
    current: ViewHintStep?,
): Modifier {
    return viewHint(
        text = step.text,
        position = step.position,
        visible = current == step,
    )
}

fun Modifier.viewHint(
    step: ViewHintStep,
    visible: Boolean,
): Modifier {
    return viewHint(
        text = step.text,
        position = step.position,
        visible = visible,
    )
}


fun Modifier.viewHint(
    text: String,
    position: HintPosition,
    visible: Boolean,
): Modifier = composed {
    val controller = LocalSimpleHintController.current
    val hostOffset = LocalSimpleHintHostOffset.current
    var lastRect by remember { mutableStateOf<Rect?>(null) }

    LaunchedEffect(text, position, visible) {
        if (visible) {
            controller?.showHint(text, position)
            controller?.updateAnchor(lastRect)
        } else if (controller != null && controller.currentHint?.text == text) {
            // скрываем только если именно ЭТОТ simpleHint сейчас активен
            controller.hideHint()
        }
    }

    this.then(
        Modifier.onGloballyPositioned { coordinates ->
            // Get position in window and subtract host offset to get relative position
            val absolutePosition = coordinates.positionInWindow()
            val size = coordinates.size.toSize()
            val rect = Rect(
                left = absolutePosition.x - hostOffset.x,
                top = absolutePosition.y - hostOffset.y,
                right = absolutePosition.x - hostOffset.x + size.width,
                bottom = absolutePosition.y - hostOffset.y + size.height
            )
            lastRect = rect
            if (visible && controller != null) {
                controller.updateAnchor(rect)
            }
        }
    )
}
