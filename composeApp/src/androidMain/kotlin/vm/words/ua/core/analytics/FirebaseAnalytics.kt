package vm.words.ua.core.analytics

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.analytics
import dev.gitlive.firebase.analytics.logEvent

/**
 * Android implementation of Analytics using Firebase Analytics
 */
class FirebaseAnalytics : Analytics {
    private val analytics = Firebase.analytics

    override fun logEvent(eventName: String, parameters: Map<String, Any>?) {
        if (parameters == null || parameters.isEmpty()) {
            analytics.logEvent(eventName)
            return
        }
        analytics.logEvent(eventName) {
            parameters.forEach { (key, value) ->
                when (value) {
                    is String -> param(key, value)
                    is Long -> param(key, value)
                    is Double -> param(key, value)
                    is Int -> param(key, value.toLong())
                    is Float -> param(key, value.toDouble())
                    is Boolean -> param(key, if (value) 1L else 0L)
                    else -> param(key, value.toString())
                }
            }
        }
    }

    override fun setUserProperty(name: String, value: String) {
        analytics.logEvent("user_property_set") {
            param("property_name", name)
            param("property_value", value)
        }
    }

    override fun setUserId(userId: String?) {
        if (userId == null) {
            return
        }
        analytics.logEvent("user_id_set") {
            param("user_id", userId)
        }
    }

    override fun setCurrentScreen(screenName: String, screenClass: String?) {
        analytics.logEvent(AnalyticsEvents.SCREEN_VIEW) {
            param("screen_name", screenName)
            screenClass?.let { param("screen_class", it) }
        }
    }
}

