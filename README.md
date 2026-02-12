# Words — Multiplatform App for Vocabulary Learning

> A cross-platform application for learning, managing, and practicing vocabulary.
> Available on **Android**, **iOS**, **Web**, and **Desktop**.

[![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20iOS%20%7C%20Web%20%7C%20Desktop-blue)]()
[![Language](https://img.shields.io/badge/Language-Kotlin-purple)](https://kotlinlang.org/)
[![UI](https://img.shields.io/badge/UI-Compose%20Multiplatform-green)](https://www.jetbrains.com/compose-multiplatform/)
[![License](https://img.shields.io/badge/License-Proprietary-red)](LICENSE)

---

## Links

- [Study Words (Web)](https://studywords.online/) — web version of the application
- [Words on Google Play](https://play.google.com/store/apps/details?id=com.vlad.words) — Android app on Google Play

---

## Features

- **Accounts & Authorization** — Google Sign-In, Firebase Authentication
- **Word Management** — create, edit, delete, and browse word cards with pagination and filters
- **Exercises & Training** — practice vocabulary with various exercise modes
- **Progress Tracking** — charts and statistics on your learning progress
- **Media Attachments** — add images and audio pronunciation to words
- **Multi-language Support** — learn words in any language pair
- **Offline Support** — work with your dictionary without an internet connection
- **Cross-platform Sync** — your data is available on all platforms

## Architecture & Modules

```
/composeApp          — Main Compose Multiplatform module (UI + platform code)
  src/commonMain     — Shared UI and logic for all platforms
  src/androidMain    — Android-specific implementations
  src/iosMain        — iOS-specific implementations
  src/jsMain         — JavaScript web target
  src/wasmJsMain     — WebAssembly web target
  src/desktopMain    — Desktop (JVM) target
/shared              — Shared business logic, models, and domain layer
/iosApp              — Native iOS container (Swift / SwiftUI)
/documents           — Policy and legal documents
```

The project follows a clean architecture approach with dependency injection (Kodein) and reactive state management (StateFlow + Coroutines).

## Tech Stack

| Category | Technologies |
|----------|-------------|
| **Language** | Kotlin 2.2, Kotlin Multiplatform |
| **UI** | Compose Multiplatform |
| **Async** | Kotlin Coroutines, StateFlow |
| **DI** | Kodein |
| **Networking** | Ktor Client (OkHttp / JS / Darwin engines) |
| **Serialization** | Kotlinx Serialization |
| **Auth** | Firebase Authentication, Google Sign-In |
| **Analytics** | Firebase Analytics, Firebase Crashlytics |
| **Storage** | Multiplatform Settings |
| **Charts** | KoalaPlot |
| **PDF** | PDFBox (Desktop), pdfjs-dist (Web), PDFKit (iOS), android-pdf-viewer (Android) |
| **Files** | FileKit |
| **Build** | Gradle (Kotlin DSL) |

## Quick Start

### Requirements

- **JDK 17+**
- **Android Studio** or **IntelliJ IDEA** (with KMP plugin)
- **Xcode** (for iOS, macOS only)

### Clone & Run

```bash
git clone https://github.com/VladM31/WordsMultiPlatform.git
cd WordsMultiPlatform
```

**Desktop (JVM):**
```bash
./gradlew :composeApp:run
```

**Web (WASM):**
```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

**Web (JS):**
```bash
./gradlew :composeApp:jsBrowserDevelopmentRun
```

**Android:**
Open the project in Android Studio and run on a device or emulator.

**iOS:**
Open `iosApp/iosApp.xcodeproj` in Xcode and run on a simulator or device (macOS required).

## Useful Links

- [`LICENSE`](LICENSE) — Proprietary License Agreement
- [`documents/en_policy.pdf`](documents/en_policy.pdf) — Privacy Policy
- [`gradle/libs.versions.toml`](gradle/libs.versions.toml) — Dependency versions

## License

Proprietary License (c) 2025 VladM31.
Use is permitted for personal, educational, or research purposes.
Commercial use requires written permission.

## Contacts

For questions and commercial licensing inquiries: **study.words.go@gmail.com**
