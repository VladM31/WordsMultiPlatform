package vm.words.ua.core.platform

import platform.UIKit.UIDevice

actual fun getDeviceName(): String {
    val device = UIDevice.currentDevice
    val name = device.name ?: "iOS Device"
    val model = device.model ?: ""
    return if (name.startsWith(model, ignoreCase = true)) {
        name
    } else {
        "$name $model".trim()
    }
}

