import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.firebaseCrashlytics)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    jvm("desktop")

    js {
        browser {
            commonWebpackConfig {
                sourceMaps = false
            }

            runTask {
                devtool = "eval"
            }

            webpackTask {
                devtool = "none"
            }
        }
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            commonWebpackConfig {
                devServer?.open = false
            }
        }
        binaries.executable()
    }

    val xcf = XCFramework()

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            xcf.add(this)
            linkerOpts("-framework", "PDFKit")
            linkerOpts("-undefined", "dynamic_lookup")
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.animation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.androidx.navigation.compose)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)
                implementation(libs.multiplatform.settings)
                implementation("org.kodein.di:kodein-di:7.21.0")
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.logging)
                implementation(projects.shared)

                implementation("io.github.vinceglb:filekit-core:0.12.0")
                implementation("io.github.vinceglb:filekit-dialogs:0.12.0")
                implementation("io.github.vinceglb:filekit-dialogs-compose:0.12.0")
                implementation("io.github.vinceglb:filekit-coil:0.12.0")

                implementation(libs.connectivity.core)
                implementation(libs.connectivity.compose)
                implementation("io.github.koalaplot:koalaplot-core:0.5.3")
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("androidx.activity:activity-compose:1.10.1")
                implementation("androidx.core:core-ktx:1.16.0")
                implementation(libs.ktor.client.okhttp)
                implementation(libs.ktor.client.android)
                implementation("androidx.security:security-crypto:1.1.0")

                implementation(libs.androidx.credentials)
                implementation(libs.androidx.credentials.play.services)
                implementation(libs.googleid)

                implementation(libs.firebase.crashlytics.ktx)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutinesSwing)
                implementation(libs.ktor.client.okhttp)
                implementation("org.apache.pdfbox:pdfbox:2.0.30")
                implementation(libs.mp3spi)
                implementation(libs.tritonus.share)

                implementation(libs.connectivity.http)
                implementation(libs.connectivity.compose.http)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
                implementation(npm("pdfjs-dist", "4.7.76"))
                implementation(compose.html.core)

                implementation(libs.connectivity.http)
                implementation(libs.connectivity.compose.http)
            }
        }

        val wasmJsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)

                implementation(libs.connectivity.http)
                implementation(libs.connectivity.compose.http)

                implementation(npm("compression-webpack-plugin", "11.1.0"))
            }
        }

        val iosArm64Main by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        val iosSimulatorArm64Main by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val deviceMain by creating {
            dependsOn(commonMain)
            androidMain.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                implementation(libs.connectivity.device)
                implementation(libs.connectivity.compose.device)

                implementation(libs.firebase.analytics)
                implementation(libs.firebase.common)
                implementation(libs.firebase.auth)
            }
        }

        val iosMain by creating {
            dependsOn(deviceMain)
            dependencies {
                implementation(libs.firebase.analytics)

                implementation(libs.connectivity.http)
                implementation(libs.connectivity.compose.http)
            }
        }
        iosArm64Main.dependsOn(iosMain)
        iosSimulatorArm64Main.dependsOn(iosMain)
    }
}

android {
    namespace = "vm.words.ua"
    compileSdk = 36

    defaultConfig {
        applicationId = "vm.words.ua"
        minSdk = 24
        targetSdk = 35
        versionCode = 5
        versionName = "1.1.0"

        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            ndk {
                debugSymbolLevel = "FULL"
            }
        }

        getByName("debug") {
            ndk {
                debugSymbolLevel = "FULL"
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "vm.words.ua.MainKt"

        jvmArgs += listOf(
            "-Xmx512m",
            "-Dfile.encoding=UTF-8"
        )

        buildTypes.release.proguard {
            isEnabled.set(false)
        }

        nativeDistributions {
            includeAllModules = true
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)

            packageName = "Words"
            packageVersion = "1.1.0"
            description = "Words App"
            vendor = "VM"

            windows {
                iconFile.set(project.file("icon.ico"))
                shortcut = true
                menuGroup = "Words"
                dirChooser = true
            }

            macOS {
                iconFile.set(project.file("icon.icns"))
            }

            linux {
                iconFile.set(project.file("icon.png"))
            }
        }
    }
}

tasks.withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.register("installAndRunDebug") {
    group = "android"
    description = "Install and run the debug APK on connected device"
    dependsOn("installDebug")

    doLast {
        try {
            val adbPath = android.adbExecutable.absolutePath
            println("Using ADB: $adbPath")

            project.exec {
                commandLine(adbPath, "shell", "am", "start", "-n", "vm.words.ua/.MainActivity")
            }

            println("✓ Application launched successfully!")
        } catch (e: Exception) {
            println("Failed to launch application: ${e.message}")
            println("You can manually launch the app from your device.")
        }
    }
}

tasks.register("iosSimulatorArm64Run") {
    group = "ios"
    description = "Build and run the iOS app on ARM64 simulator (M1/M2 Mac)"
    dependsOn("linkDebugFrameworkIosSimulatorArm64")

    doLast {
        println("📱 Building iOS app for simulator...")

        val xcodebuildPath = "/usr/bin/xcodebuild"

        println("🔨 Building with xcodebuild...")
        project.exec {
            workingDir = file("${rootProject.projectDir}/iosApp")
            commandLine(
                xcodebuildPath,
                "-project", "iosApp.xcodeproj",
                "-scheme", "iosApp",
                "-configuration", "Debug",
                "-sdk", "iphonesimulator",
                "-destination", "platform=iOS Simulator,name=iPhone 17 Pro",
                "build"
            )
        }

        println("🚀 Launching iOS Simulator...")
        val homeDir = System.getProperty("user.home")
        val appPath = "$homeDir/Library/Developer/Xcode/DerivedData/iosApp-bxpfibllityukmdutqwcfggofsxj/Build/Products/Debug-iphonesimulator/WordsMultiPlatform.app"

        try {
            project.exec {
                commandLine("xcrun", "simctl", "boot", "iPhone 17 Pro")
                isIgnoreExitValue = true
            }

            project.exec {
                commandLine("xcrun", "simctl", "install", "booted", appPath)
            }

            project.exec {
                commandLine("xcrun", "simctl", "launch", "booted", "vm.words.ua.WordsMultiPlatform")
            }

            println("✅ iOS app launched successfully on simulator!")
        } catch (e: Exception) {
            println("❌ Failed to launch: ${e.message}")
            println("You can open Xcode and run manually")
        }
    }
}

tasks.register("iosRun") {
    group = "ios"
    description = "Build and run iOS app (shortcut)"
    dependsOn("iosSimulatorArm64Run")
}