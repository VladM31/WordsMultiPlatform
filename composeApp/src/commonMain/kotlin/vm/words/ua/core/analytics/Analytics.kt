package vm.words.ua.core.analytics

/**
 * Multiplatform Analytics interface for tracking events across all platforms
 */
interface Analytics {
    /**
     * Log an event with optional parameters
     * @param eventName The name of the event to log
     * @param parameters Optional map of parameters associated with the event
     */
    fun logEvent(eventName: String, parameters: Map<String, Any>? = null)

    /**
     * Set user property
     * @param name The name of the user property
     * @param value The value of the user property
     */
    fun setUserProperty(name: String, value: String)

    /**
     * Set user ID
     * @param userId The user ID to set
     */
    fun setUserId(userId: String?)

    /**
     * Set current screen name
     * @param screenName The name of the current screen
     * @param screenClass The class name of the current screen (optional)
     */
    fun setCurrentScreen(screenName: String, screenClass: String? = null)
}

