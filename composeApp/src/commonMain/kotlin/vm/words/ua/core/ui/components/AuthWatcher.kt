package vm.words.ua.core.ui.components

import androidx.compose.runtime.*
import org.kodein.di.instance
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.net.UnauthorizedHandler
import vm.words.ua.di.DiContainer
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController

@Composable
fun AuthWatcher() {
    val navController: SimpleNavController by DiContainer.di.instance()
    val userCacheManager : UserCacheManager by DiContainer.di.instance()
    val tokenState by userCacheManager.tokenFlow.collectAsState()

    // 401: сбрасываем токен, дальше LaunchedEffect ниже уведёт на экран входа
    DisposableEffect(userCacheManager) {
        UnauthorizedHandler.onUnauthorized = { userCacheManager.clear() }
        onDispose { UnauthorizedHandler.onUnauthorized = null }
    }

    LaunchedEffect(tokenState) {
        val current = navController.currentRoute
        val isNotExpired = userCacheManager.isExpired.not()

        if (isNotExpired) {
            return@LaunchedEffect
        }
        if (current == Screen.UpdateApp.route) {
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

