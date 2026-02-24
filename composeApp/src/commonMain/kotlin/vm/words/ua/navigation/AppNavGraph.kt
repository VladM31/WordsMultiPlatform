package vm.words.ua.navigation

import androidx.compose.runtime.*
import vm.words.ua.core.ui.animation.NavTransition
import org.kodein.di.instance
import vm.words.ua.core.analytics.Analytics
import vm.words.ua.core.platform.AppPlatform
import vm.words.ua.core.platform.currentPlatform
import vm.words.ua.core.ui.components.SwipeProvider
import vm.words.ua.core.ui.screen.LoaderScreen
import vm.words.ua.core.ui.screen.UpdateScreen
import vm.words.ua.di.DiContainer
import vm.words.ua.navigation.providers.*
import vm.words.ua.utils.net.hasInternet
import vm.words.ua.utils.net.ui.components.NoInternetBanner

private val noInternetSupportRoutes = setOf(
    Screen.Home,
    Screen.Settings,
    Screen.PlayList,
    Screen.PlayListFilter,
    Screen.Theme
).map { it.route }

private val noHomeBtmSupportRoutes = setOf(
    Screen.UpdateApp,
    Screen.Login,
    Screen.TelegramSignUp,
    Screen.TelegramLogin,
    Screen.ConfirmSignUp
).map { it.route }

@Composable
fun AppNavGraph() {
    val navController: SimpleNavController by DiContainer.di.instance()
    val analytics by DiContainer.di.instance<Analytics>()

    // List of providers - modules can add their own provider to this list
    val providers: List<ScreenProvider> = listOf(
        CoreScreenProvider(),
        AuthScreenProvider(),
        MainScreenProvider(),
        WordsScreenProvider(),
        PlayListScreenProvider(),
        ExerciseScreenProvider(),
        SettingScreenProvider(),
        LearningScreenProvider()
    )

    val route = navController.currentRoute
    val isOnline = hasInternet()
    val platform = currentPlatform()
    val showHomeBtn = remember(isOnline, route) {
        platform != AppPlatform.JS && noHomeBtmSupportRoutes.contains(route).not()
    }

    LaunchedEffect(route) {
        analytics.setCurrentScreen(route)
    }

    if (isOnline.not() && (noInternetSupportRoutes.contains(route).not() || platform == AppPlatform.JS)) {
        NoInternetBanner(
            navController = navController,
            showHomeBtn = showHomeBtn
        )
        return
    }

    BackHandler(navController = navController)

    NavTransition(
        route = route,
        isNavigatingBack = navController.isNavigatingBack
    ) { currentAnimatedRoute ->
        SwipeProvider(currentAnimatedRoute) {
            if (currentAnimatedRoute == Screen.UpdateApp.route) {
                UpdateScreen(navController = navController)
                return@SwipeProvider
            }
            val handled = providers.any { provider ->
                provider.provide(currentAnimatedRoute, navController)
            }
            if (!handled) {
                LoaderScreen(isInitiated = true) {}
            }
        }
    }
}
