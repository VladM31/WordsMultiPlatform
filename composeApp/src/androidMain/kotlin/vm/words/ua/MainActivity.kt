package vm.words.ua

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import vm.words.ua.auth.domain.factories.ActivityHolder
import vm.words.ua.auth.domain.managers.AuthHistorySettingsFactory
import vm.words.ua.core.domain.managers.SettingsFactory
import vm.words.ua.core.ui.theme.ThemeManager
import vm.words.ua.utils.storage.AndroidStorageConfig

class MainActivity : ComponentActivity() {
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make content draw behind system bars
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Configure edge-to-edge manually for Android 15+ compatibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Set cutout mode to ALWAYS for Android 15+ compatibility
            window.attributes.layoutInDisplayCutoutMode =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
                } else {
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                }
        }

        // Initial edge-to-edge with transparent bars
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
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
                // Observe theme changes and update system bars
                val currentTheme by ThemeManager.instance.currentTheme.collectAsState()

                LaunchedEffect(currentTheme) {
                    val backgroundColor = currentTheme.primaryBack.toArgb()
                    // Set window background color
                    window.decorView.setBackgroundColor(backgroundColor)

                    val style = if (currentTheme.isDark) {
                        SystemBarStyle.dark(backgroundColor)
                    } else {
                        SystemBarStyle.light(backgroundColor, backgroundColor)
                    }
                    enableEdgeToEdge(
                        statusBarStyle = style,
                        navigationBarStyle = style
                    )
                }

                // Wrap app in Box with background that fills behind system bars
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(currentTheme.primaryBack)
                ) {
                    App(
                        modifier = Modifier
                            .fillMaxSize()
                            .systemBarsPadding()
                    )
                }
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
