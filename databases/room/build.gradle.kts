plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    compileSdk = libs.versions.androidCompileSdkVersion.get().toInt()
    defaultConfig {
        namespace = "com.example.databases.room"
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

    implementation(libs.roomRuntime)
    implementation(libs.roomCoroutines)
    ksp(libs.roomCompiler)
}
