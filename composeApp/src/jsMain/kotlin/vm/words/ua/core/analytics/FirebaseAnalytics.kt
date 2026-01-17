package vm.words.ua.core.analytics

/**
 * JS implementation of Analytics using Firebase Analytics
 * Calls Firebase Analytics functions defined in the global scope
 */
class FirebaseAnalytics : Analytics {

    override fun logEvent(eventName: String, parameters: Map<String, Any>?) {
        try {
            if (parameters == null || parameters.isEmpty()) {
                js("window.firebaseAnalytics && window.firebaseAnalytics.logEvent(eventName)")
            } else {
                val jsParams = js("({})")
                parameters.forEach { (key, value) ->
                    jsParams[key] = value
                }
                js("window.firebaseAnalytics && window.firebaseAnalytics.logEvent(eventName, jsParams)")
            }
        } catch (e: Throwable) {
            console.log("Firebase Analytics logEvent error: ${e.message}")
        }
    }

    override fun setUserProperty(name: String, value: String) {
        try {
            js("window.firebaseAnalytics && window.firebaseAnalytics.setUserProperties({ [name]: value })")
        } catch (e: Throwable) {
            console.log("Firebase Analytics setUserProperty error: ${e.message}")
        }
    }

    override fun setUserId(userId: String?) {
        try {
            if (userId != null) {
                js("window.firebaseAnalytics && window.firebaseAnalytics.setUserId(userId)")
            }
        } catch (e: Throwable) {
            console.log("Firebase Analytics setUserId error: ${e.message}")
        }
    }

    override fun setCurrentScreen(screenName: String, screenClass: String?) {
        try {
            val params = js("({ screen_name: screenName })")
            if (screenClass != null) {
                params["screen_class"] = screenClass
            }
            js("window.firebaseAnalytics && window.firebaseAnalytics.logEvent('screen_view', params)")
        } catch (e: Throwable) {
            console.log("Firebase Analytics setCurrentScreen error: ${e.message}")
        }
    }
}

