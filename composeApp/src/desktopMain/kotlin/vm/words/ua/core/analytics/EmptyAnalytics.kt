package vm.words.ua.core.analytics

/**
 * Empty implementation of Analytics for Desktop platform
 * Events are logged to console in debug mode
 */
class EmptyAnalytics : Analytics {
    override fun logEvent(eventName: String, parameters: Map<String, Any>?) {
        println("Analytics Event: $eventName, params: $parameters")
    }

    override fun setUserProperty(name: String, value: String) {
        println("Analytics UserProperty: $name = $value")
    }

    override fun setUserId(userId: String?) {
        println("Analytics UserId: $userId")
    }

    override fun setCurrentScreen(screenName: String, screenClass: String?) {
        println("Analytics Screen: $screenName (class: $screenClass)")
    }
}

