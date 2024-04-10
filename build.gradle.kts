//buildscript {
//    dependencies {
//        classpath("io.realm:realm-gradle-plugin:10.16.1")
//    }
//}

plugins {
    id("com.android.application") version "8.3.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("org.jetbrains.kotlin.kapt") version "1.9.10" apply false
    id("io.realm.kotlin") version "1.11.0" apply false // VMMZ
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
}
