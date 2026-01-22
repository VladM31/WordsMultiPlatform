# WordsMultiPlatform

WordsMultiPlatform is a cross-platform application for working with a dictionary/words (viewing, filtering, and managing
word cards). The project is written in Kotlin Multiplatform and provides interfaces for Android, iOS, Web (WASM/JS), and
Desktop (JVM) using shared code.

## Summary

The app allows storing and browsing words, applying filters, paging through results, and selecting highlighted words.
Main scenarios:

- Viewing a paginated list of words
- Applying filters (by user and other attributes)
- Selecting/deselecting words (multiple selection)
- Synchronization/loading of data via domain managers

The project is split into a `shared` module (business logic) and `composeApp` (UI implemented with Compose
Multiplatform). There is also a native iOS project in `iosApp`.

## Technologies

The project uses the following key technologies and libraries:

- Kotlin Multiplatform (KMP) — shared code for multiple targets
- Compose Multiplatform — UI for Desktop / Web / Android (shared UI layer)
- Kotlin Coroutines + StateFlow — asynchronous programming and state management
- Gradle (Kotlin DSL) — project build system
- Kotlin/WASM and Kotlin/JS — web targets (WASM preferred for modern browsers)
- JVM target — for the Desktop application
- iOS (SwiftUI entry point) — native container for the mobile UI
- (Optional) Firebase — Android configuration files (google-services.json) and setup instructions are present
- Common libraries: Ktor (HTTP), kotlinx-datetime, Coil (image loading) — exact dependencies are listed in
  `gradle/libs.versions.toml` and `build.gradle.kts`.

> Note: the exact set of external libraries can be found in `build.gradle.kts` and `gradle/libs.versions.toml`.

## Repository structure (simplified)

- /composeApp — main Compose Multiplatform module (UI + platform-specific code)
  - src/commonMain — common code for all targets
  - src/androidMain, src/iosMain, src/jsMain, src/jvmMain — platform-specific implementations
- /shared — shared business logic and models
- /iosApp — iOS container (Swift/SwiftUI)
- /gradle, /build — build configuration and outputs

## How to run (Windows)

Build and run via Gradle. Example commands (run from the repository root in Windows command prompt):

- Run Desktop (JVM):

  .\gradlew.bat :composeApp:run

- Run Web (WASM) in development mode:

    .\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun

- Run Web (JS) in development mode:

    .\gradlew.bat :composeApp:jsBrowserDevelopmentRun

- iOS: open the `iosApp` folder in Xcode and run from Xcode (macOS required).

If the project uses services such as Firebase, follow the instructions in `FIREBASE_SETUP.md` and `ANDROID_SETUP.md`.

## Useful files

- `FIREBASE_SETUP.md` — how to configure Firebase (if needed)
- `ANDROID_SETUP.md` — Android build and setup instructions
- `gradle/libs.versions.toml` — dependency versions

## For developers

- Shared business logic lives in `shared` and `composeApp/src/commonMain` — changes there apply to all targets.
- The UI is implemented in `composeApp` using Compose Multiplatform.
- Screen state is typically managed with `StateFlow` in ViewModels (for example, `WordsViewModel`); asynchronous work
  uses Coroutines.

## Possible improvements / additions

- API/backend documentation (if an external server is used)
- Example tests and CI configuration
- Detailed description of data models and structures

## License

This project is distributed under a proprietary license. See the `LICENSE` file in the repository root for full terms:

- `LICENSE` — Proprietary License Agreement (© 2025 VladM31). Use is permitted for personal, educational, or research
  purposes; commercial use requires written permission. For commercial licensing inquiries contact
  study.words.go@gmail.com

