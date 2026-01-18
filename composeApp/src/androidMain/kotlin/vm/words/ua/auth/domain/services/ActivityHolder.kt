package vm.words.ua.auth.domain.services

import android.app.Activity
import java.lang.ref.WeakReference

/**
 * Holds a weak reference to the current Activity for Google Sign-In
 */
object ActivityHolder {
    private var activityRef: WeakReference<Activity>? = null

    fun setActivity(activity: Activity) {
        activityRef = WeakReference(activity)
    }

    fun getActivity(): Activity? = activityRef?.get()

    fun clear() {
        activityRef = null
    }
}

