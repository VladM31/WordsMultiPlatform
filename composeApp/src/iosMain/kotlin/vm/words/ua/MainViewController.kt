package vm.words.ua

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

// Object to expose MainViewController to Swift
object MainViewControllerFactory {
    fun create(): UIViewController = ComposeUIViewController { App() }
}

// Alternative: Direct function for Swift (requires @ObjCName or will use mangled name)
fun MainViewController(): UIViewController = ComposeUIViewController { App() }

