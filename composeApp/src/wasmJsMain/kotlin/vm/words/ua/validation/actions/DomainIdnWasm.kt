package vm.words.ua.validation.actions

actual fun domainToAscii(domain: String): String? {
    // wasm/wasmJs target: no builtin IDN conversion here.
    // Return null so common code uses the original domain string.
    return null
}

