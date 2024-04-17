plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.vmmaldonadoz.realmexample"
    compileSdk = libs.versions.androidCompileSdkVersion.get().toInt()

    defaultConfig {
        applicationId = "com.vmmaldonadoz.realmexample"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
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
}

dependencies {
    implementation(project(":databases:api"))
    implementation(project(":databases:room"))
    implementation(project(":databases:realm"))

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(platform(libs.coroutinesBom))
    implementation(libs.lifecycle.viewmodel.ktx)

    testImplementation(libs.junit)
}
