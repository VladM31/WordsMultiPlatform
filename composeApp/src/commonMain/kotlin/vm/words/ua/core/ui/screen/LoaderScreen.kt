package vm.words.ua.core.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.kodein.di.instance
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.platform.currentPlatform
import vm.words.ua.core.platform.isWeb
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.di.DiContainer
import vm.words.ua.di.initDi
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController

@Composable
fun LoaderScreen(
    isInitiated: Boolean = false,
    modifier: Modifier = Modifier,
    message: String? = null,
    onInitialized: (() -> Unit)
) {

    LaunchedEffect(Unit) {
        try {
            if (isInitiated.not()) {
                initDi()
                AppRemoteConfig.initialize()
            }

            val userCacheManager : UserCacheManager by DiContainer.di.instance()
            val navController : SimpleNavController by DiContainer.di.instance()

            try {
                if (AppRemoteConfig.currentVersion != AppRemoteConfig.version && currentPlatform().isWeb.not()) {
                    navController.navigateAndClear(Screen.UpdateApp)
                    return@LaunchedEffect
                }
                if (userCacheManager.tokenFlow.value?.isExpired() == false) {
                    navController.navigate(Screen.Home)
                    return@LaunchedEffect
                }
            } catch (_: Throwable) {
                // ignore - treat as no token
            }
            navController.navigateAndClear(Screen.Login)
        } catch (e: Throwable) {
            // Log error if needed
            e.printStackTrace()
            // Navigate to login as fallback
            try {
                val navController : SimpleNavController by DiContainer.di.instance()
                navController.navigateAndClear(Screen.Login)
            } catch (_: Throwable) {
                // If even DI fails, just call onInitialized
            }
        } finally {
            onInitialized()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(modifier = Modifier.size(48.dp))
        if (!message.isNullOrEmpty()) {
            Text(
                text = message,
                color = AppTheme.PrimaryColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

