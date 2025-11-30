package vm.words.ua.utils.hints.ui.controllers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Rect
import vm.words.ua.utils.hints.domain.models.HintPosition
import vm.words.ua.utils.hints.domain.models.SimpleHint


class SimpleHintController {

    var currentHint by mutableStateOf<SimpleHint?>(null)
        private set

    // Прямоугольник области компонента, который не затемняем
    var anchorRect by mutableStateOf<Rect?>(null)
        private set

    fun showHint(text: String, position: HintPosition) {
        currentHint = SimpleHint(text, position)
    }

    fun hideHint() {
        currentHint = null
        anchorRect = null
    }

    fun updateAnchor(rect: Rect?) {
        anchorRect = rect
    }
}
