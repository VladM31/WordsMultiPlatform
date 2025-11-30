package vm.words.ua.utils.hints.ui.utils

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
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
            val rect = coordinates.boundsInRoot()
            lastRect = rect
            if (visible && controller != null) {
                controller.updateAnchor(rect)
            }
        }
    )
}
