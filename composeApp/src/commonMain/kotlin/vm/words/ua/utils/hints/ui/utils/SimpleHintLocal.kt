package vm.words.ua.utils.hints.ui.utils

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import vm.words.ua.utils.hints.ui.controllers.SimpleHintController

val LocalSimpleHintController =
    staticCompositionLocalOf<SimpleHintController?> { null }

val LocalSimpleHintHostOffset =
    staticCompositionLocalOf { Offset.Zero }
