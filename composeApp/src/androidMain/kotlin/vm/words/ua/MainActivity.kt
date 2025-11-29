package vm.words.ua

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import vm.words.ua.auth.domain.managers.AuthHistorySettingsFactory
import vm.words.ua.core.domain.managers.SettingsFactory
import vm.words.ua.utils.storage.AndroidStorageConfig

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            // Initialize Android-specific factories
            SettingsFactory.init(this)
            AuthHistorySettingsFactory.init(this)
            AndroidStorageConfig.init(this)

            setContent {
                App()
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onCreate", e)
            e.printStackTrace()
        }
    }
}
