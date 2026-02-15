package vm.words.ua.core.platform

var wasmDeviceUserAgent: String = "Web Browser"


fun setWasmDeviceUserAgent(name: String) {
    wasmDeviceUserAgent = name
}

actual fun getDeviceName(): String = wasmDeviceUserAgent
