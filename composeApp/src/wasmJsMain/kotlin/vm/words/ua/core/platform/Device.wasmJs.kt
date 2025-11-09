package vm.words.ua.core.platform

/**
 * WASM build cannot reliably use dynamic `js(...)` interop in arbitrary code paths.
 * Instead expose a simple setter that the JS host can call after the module is
 * initialized to provide the real user agent (or any device string).
 */

// Mutable storage that the host can set. Defaults to a generic label.
var wasmDeviceUserAgent: String = "Web Browser"

// Public setter — the JS host should call this after initializing the WASM module.
// Example host call (see instructions in code comments below):
//   const ua = navigator.userAgent || navigator.platform || 'Web Browser';
//   moduleName.setWasmDeviceUserAgent(ua);
fun setWasmDeviceUserAgent(name: String) {
    wasmDeviceUserAgent = name
}

// Provide the actual implementation used by common code
actual fun getDeviceName(): String = wasmDeviceUserAgent
