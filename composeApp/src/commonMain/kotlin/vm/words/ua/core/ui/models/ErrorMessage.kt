package vm.words.ua.core.ui.models

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


data class ErrorMessage @OptIn(ExperimentalUuidApi::class) constructor(
    val message: String = "",
    val id: String = Uuid.random().toString()
)
