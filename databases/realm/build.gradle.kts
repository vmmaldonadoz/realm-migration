import org.jetbrains.kotlin.js.translate.context.Namer.kotlin

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = libs.versions.androidCompileSdkVersion.get().toInt()
    defaultConfig {
        namespace = "com.example.databases.realm"
    }
}

java {
    toolchain {
        languageVersion.set(
            JavaLanguageVersion.of(
                libs.versions.javaToolchainVersion.get().toInt()
            )
        )
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(
            JavaLanguageVersion.of(
                libs.versions.javaToolchainVersion.get().toInt()
            )
        )
    }
}

dependencies {
    implementation(projects.databases.api)
}
