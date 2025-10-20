package vm.words.ua.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.kodein.di.direct
import org.kodein.di.instance

/**
 * JS/Web implementation using simple remember
 * Web apps don't have configuration changes like Android
 */
@Composable
actual inline fun <reified T : Any> rememberInstance(): T {
    return remember {
        DiContainer.di.direct.instance<T>()
    }
}

