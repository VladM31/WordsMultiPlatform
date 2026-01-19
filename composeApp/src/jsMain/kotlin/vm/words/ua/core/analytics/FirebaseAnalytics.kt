package vm.words.ua.core.analytics

/**
 * JS implementation of Analytics using Firebase Analytics
 * Calls Firebase Analytics functions defined in the global scope
 */
class FirebaseAnalytics : Analytics {

    override fun logEvent(eventName: String, parameters: Map<String, Any>?) {
        try {
            val firebaseAnalytics = js("window.firebaseAnalytics")
            if (firebaseAnalytics == null) return

            if (parameters == null || parameters.isEmpty()) {
                firebaseAnalytics.logEvent(eventName)
            } else {
                val jsParams = js("({})")
                parameters.forEach { (key, value) ->
                    jsParams.asDynamic()[key] = value
                }
                firebaseAnalytics.logEvent(eventName, jsParams)
            }
        } catch (e: Throwable) {
            console.log("Firebase Analytics logEvent error: ${e.message}")
        }
    }

    override fun setUserProperty(name: String, value: String) {
        try {
            val firebaseAnalytics = js("window.firebaseAnalytics")
            if (firebaseAnalytics == null) return

            val props = js("({})")
            props.asDynamic()[name] = value
            firebaseAnalytics.setUserProperties(props)
        } catch (e: Throwable) {
            console.log("Firebase Analytics setUserProperty error: ${e.message}")
        }
    }

    override fun setUserId(userId: String?) {
        try {
            if (userId != null) {
                val firebaseAnalytics = js("window.firebaseAnalytics")
                if (firebaseAnalytics != null) {
                    firebaseAnalytics.setUserId(userId)
                }
            }
        } catch (e: Throwable) {
            console.log("Firebase Analytics setUserId error: ${e.message}")
        }
    }

    override fun setCurrentScreen(screenName: String, screenClass: String?) {
        try {
            val firebaseAnalytics = js("window.firebaseAnalytics")
            if (firebaseAnalytics == null) return

            val params = js("({})")
            params.asDynamic()["screen_name"] = screenName
            if (screenClass != null) {
                params.asDynamic()["screen_class"] = screenClass
            }
            firebaseAnalytics.logEvent("screen_view", params)
        } catch (e: Throwable) {
            console.log("Firebase Analytics setCurrentScreen error: ${e.message}")
        }
    }
}

