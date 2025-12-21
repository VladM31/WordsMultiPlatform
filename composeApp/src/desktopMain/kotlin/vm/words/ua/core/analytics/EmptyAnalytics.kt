package vm.words.ua.core.analytics

/**
 * Empty implementation of Analytics for Desktop platform
 * Events are logged to console in debug mode
 */
class EmptyAnalytics : Analytics {
    override fun logEvent(eventName: String, parameters: Map<String, Any>?) {
        // Empty implementation - you can add console logging if needed
        println("Analytics Event: $eventName, params: $parameters")
    }

    override fun setUserProperty(name: String, value: String) {
        // Empty implementation
        println("Analytics UserProperty: $name = $value")
    }

    override fun setUserId(userId: String?) {
        // Empty implementation
        println("Analytics UserId: $userId")
    }

    override fun setCurrentScreen(screenName: String, screenClass: String?) {
        // Empty implementation
        println("Analytics Screen: $screenName (class: $screenClass)")
    }
}

