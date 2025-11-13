package vm.words.ua.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.kodein.di.direct
import org.kodein.di.instance

@Composable
actual inline fun <reified T : Any> rememberInstance(): T {
    return remember {
        DiContainer.di.direct.instance<T>()
    }
}

