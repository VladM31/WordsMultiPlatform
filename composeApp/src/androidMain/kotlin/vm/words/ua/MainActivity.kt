package vm.words.ua

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import androidx.core.graphics.toColorInt
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import vm.words.ua.auth.domain.factories.ActivityHolder
import vm.words.ua.auth.domain.managers.AuthHistorySettingsFactory
import vm.words.ua.core.domain.managers.SettingsFactory
import vm.words.ua.utils.storage.AndroidStorageConfig

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display with dark theme colors
        val darkScrim = "#1E2127".toColorInt()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(darkScrim),
            navigationBarStyle = SystemBarStyle.dark(darkScrim)
        )

        // Adjust resize for soft keyboard
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        try {
            // Initialize Firebase
            Firebase.initialize(this)

            // Initialize Crashlytics (Android SDK) and enable collection
            try {
                val crashlytics = FirebaseCrashlytics.getInstance()
                crashlytics.setCrashlyticsCollectionEnabled(true)
                crashlytics.log("MainActivity_onCreate")
            } catch (e: Exception) {
                Log.w("MainActivity", "Crashlytics not initialized: ${e.message}")
            }

            // Initialize Android-specific factories
            SettingsFactory.init(this)
            AuthHistorySettingsFactory.init(this)
            AndroidStorageConfig.init(this)
            ActivityHolder.setActivity(this)

            setContent {
                App(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding()
                )
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onCreate", e)
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityHolder.clear()
    }
}
