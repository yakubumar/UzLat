plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "uz.uzbeklatin.keyboard"
    compileSdk = 35
    defaultConfig {
        applicationId = "uz.uzbeklatin.keyboard"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
}
