package vm.words.ua.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import vm.words.ua.core.domain.managers.SettingsFactory


class ThemeManager(
    private val settings: Settings
) {
    private val _currentTheme = MutableStateFlow(loadTheme())
    val currentTheme: StateFlow<AppThemeConfig> = _currentTheme.asStateFlow()

    private fun loadTheme(): AppThemeConfig {
        val themeId = settings.getStringOrNull(THEME_KEY) ?: AppThemes.Default.id
        return AppThemes.getById(themeId)
    }

    fun setTheme(theme: AppThemeConfig) {
        settings.putString(THEME_KEY, theme.id)
        _currentTheme.value = theme
    }

    fun setThemeById(themeId: String) {
        val theme = AppThemes.getById(themeId)
        setTheme(theme)
    }

    companion object {
        private const val THEME_KEY = "APP_THEME_KEY"

        // Singleton instance
        private var _instance: ThemeManager? = null

        val instance: ThemeManager
            get() {
                if (_instance == null) {
                    _instance = ThemeManager(SettingsFactory.create())
                }
                return _instance!!
            }
    }
}

/**
 * Composable helper to observe current theme
 */
@Composable
fun rememberCurrentTheme(): State<AppThemeConfig> {
    return ThemeManager.instance.currentTheme.collectAsState()
}

