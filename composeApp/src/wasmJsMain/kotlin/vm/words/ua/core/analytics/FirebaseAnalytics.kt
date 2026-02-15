@file:OptIn(ExperimentalJsExport::class)

package vm.words.ua.core.analytics


@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("(eventName) => { console.log('JsFun logFirebaseEvent called:', eventName); if (window.logFirebaseEvent) { window.logFirebaseEvent(eventName, null); } }")
private external fun logFirebaseEventJs(eventName: JsString)

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("(userId) => { console.log('JsFun setFirebaseUserId called:', userId); if (window.setFirebaseUserId) { window.setFirebaseUserId(userId); } }")
private external fun setFirebaseUserIdJs(userId: JsString?)

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("(name, value) => { console.log('JsFun setFirebaseUserProperty called:', name, value); if (window.setFirebaseUserProperty) { window.setFirebaseUserProperty(name, value); } }")
private external fun setFirebaseUserPropertyJs(name: JsString, value: JsString)

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("(screenName, screenClass) => { console.log('JsFun logFirebaseScreenView called:', screenName, screenClass); if (window.logFirebaseScreenView) { window.logFirebaseScreenView(screenName, screenClass); } }")
private external fun logFirebaseScreenViewJs(screenName: JsString, screenClass: JsString?)


class FirebaseAnalytics : Analytics {

    override fun logEvent(eventName: String, parameters: Map<String, Any>?) {
        try {
            logFirebaseEventJs(eventName.toJsString())
        } catch (_: Throwable) {
        }
    }

    override fun setUserProperty(name: String, value: String) {
        try {
            setFirebaseUserPropertyJs(name.toJsString(), value.toJsString())
        } catch (_: Throwable) {
        }
    }

    override fun setUserId(userId: String?) {
        try {
            setFirebaseUserIdJs(userId?.toJsString())
        } catch (_: Throwable) {
        }
    }

    override fun setCurrentScreen(screenName: String, screenClass: String?) {
        try {
            logFirebaseScreenViewJs(screenName.toJsString(), screenClass?.toJsString())
        } catch (_: Throwable) {
        }
    }
}

