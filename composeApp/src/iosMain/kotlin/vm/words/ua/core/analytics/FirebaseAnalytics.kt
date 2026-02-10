package vm.words.ua.core.analytics

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.analytics

/**
 * iOS implementation of Analytics using Firebase Analytics (GitLive SDK)
 */
class FirebaseAnalytics : Analytics {

    private val firebaseAnalytics = Firebase.analytics

    override fun logEvent(eventName: String, parameters: Map<String, Any>?) {
        firebaseAnalytics.logEvent(eventName, parameters)
    }

    override fun setUserProperty(name: String, value: String) {
        firebaseAnalytics.setUserProperty(name, value)
    }

    override fun setUserId(userId: String?) {
        firebaseAnalytics.setUserId(userId)
    }

    override fun setCurrentScreen(screenName: String, screenClass: String?) {
        // Firebase Analytics uses logEvent for screen tracking
        val params = mutableMapOf<String, Any>(
            "screen_name" to screenName
        )
        screenClass?.let { params["screen_class"] = it }
        firebaseAnalytics.logEvent("screen_view", params)
    }
}

