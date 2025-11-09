package vm.words.ua.core.platform

import android.os.Build
import java.util.Locale

actual fun getDeviceName(): String {
    val manufacturer = Build.MANUFACTURER ?: ""
    val model = Build.MODEL ?: ""
    return if (model.startsWith(manufacturer, ignoreCase = true)) {
        model.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    } else {
        "${manufacturer.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }} $model"
    }
}

