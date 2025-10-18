import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    jvm()
    
    js {
        browser()
        binaries.executable()
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.kotlinx.serialization.json)
            implementation(projects.shared)
        }

        // Промежуточный source set для платформ с Firebase (JVM и JS)
        val firebaseMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(libs.firebase.common)
                implementation(libs.firebase.config)
            }
        }

        jvmMain {
            dependsOn(commonMain.get())
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutinesSwing)
            }
        }

        jsMain {
            dependsOn(firebaseMain)
        }

        // WasmJS платформа - без Firebase
        val wasmJsMain by getting {
            dependsOn(commonMain.get())
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}


compose.desktop {
    application {
        mainClass = "vm.words.ua.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "vm.words.ua"
            packageVersion = "1.0.0"
        }
    }
}
