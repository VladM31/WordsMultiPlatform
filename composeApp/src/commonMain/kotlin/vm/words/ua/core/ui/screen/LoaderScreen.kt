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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.map
import org.kodein.di.instance
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.firebase.AppRemoteConfig
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.di.DiContainer
import vm.words.ua.di.initDi
import vm.words.ua.navigation.Screen
import kotlin.getValue

@Composable
fun LoaderScreen(
    modifier: Modifier = Modifier,
    message: String? = null,
    navController: vm.words.ua.navigation.SimpleNavController,
) {

    LaunchedEffect(Unit) {
        initDi()
        AppRemoteConfig.initialize()
        val userCacheManager : UserCacheManager by DiContainer.di.instance()

        try {
            if (userCacheManager.tokenFlow.value?.isExpired() == false) {
                navController.navigate(Screen.Home)
                return@LaunchedEffect
            }
        } catch (_: Throwable) {
            // ignore - treat as no token
        }
        navController.navigateAndClear(Screen.Login)
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

