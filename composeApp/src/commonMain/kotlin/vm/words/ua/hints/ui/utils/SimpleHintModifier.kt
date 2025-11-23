package vm.words.ua.hints.ui.utils

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import vm.words.ua.hints.domain.models.HintPosition

interface SimpleHintArgs {
    val text: String
    val position: HintPosition
}

fun Modifier.viewHint(
    args: SimpleHintArgs,
    current: SimpleHintArgs,
): Modifier {
    return viewHint(
        text = args.text,
        position = args.position,
        visible = current == args,
    )
}

fun Modifier.viewHint(
    args: SimpleHintArgs,
    visible: Boolean,
): Modifier {
    return viewHint(
        text = args.text,
        position = args.position,
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
