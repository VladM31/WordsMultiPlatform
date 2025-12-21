package vm.words.ua.core.analytics

/**
 * Empty implementation of Analytics for WasmJS platform
 * Events are logged to console
 */
class EmptyAnalytics : Analytics {
    override fun logEvent(eventName: String, parameters: Map<String, Any>?) {
        // Empty implementation - you can add console.log if needed
        console.log("Analytics Event: $eventName, params: $parameters")
    }

    override fun setUserProperty(name: String, value: String) {
        // Empty implementation
        console.log("Analytics UserProperty: $name = $value")
    }

    override fun setUserId(userId: String?) {
        // Empty implementation
        console.log("Analytics UserId: $userId")
    }

    override fun setCurrentScreen(screenName: String, screenClass: String?) {
        // Empty implementation
        console.log("Analytics Screen: $screenName (class: $screenClass)")
    }
}

