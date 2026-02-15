package vm.words.ua.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import org.kodein.di.direct
import org.kodein.di.instance
import vm.words.ua.navigation.SimpleNavController


@Composable
actual inline fun <reified T : Any> rememberInstance(): T {
    val isViewModel = ViewModel::class.java.isAssignableFrom(T::class.java)
    if (!isViewModel) {
        return androidx.compose.runtime.remember {
            DiContainer.di.direct.instance<T>()
        }
    }
    val navController: SimpleNavController = DiContainer.di.direct.instance()
    return viewModel(
        viewModelStoreOwner = navController.viewModelStoreOwner(),
        modelClass = T::class.java as Class<ViewModel>,
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
                return DiContainer.di.direct.instance<T>() as VM
            }
        }
    ) as T
}
