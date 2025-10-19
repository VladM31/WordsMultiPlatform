package vm.words.ua.core.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.kodein.di.instance
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.di.DiContainer
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController

@Composable
fun AuthWatcher(
    navController: SimpleNavController
) {
    val userCacheManager : UserCacheManager by DiContainer.di.instance()
    val tokenState by userCacheManager.tokenFlow.collectAsState()

    LaunchedEffect(tokenState) {
        val current = navController.currentRoute
        val isNotExpired = userCacheManager.isExpired.not()

        if (isNotExpired) {
            return@LaunchedEffect
        }

        if (current != Screen.Login.route) {
            println("AuthWatcher: navigating to login from $current")
            navController.navigateAndClear(Screen.Login)
        } else {
            println("AuthWatcher: already on login, no navigation")
        }
    }
}

