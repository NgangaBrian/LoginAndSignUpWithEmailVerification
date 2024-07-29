plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.loginandsignupwithemailverification"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.loginandsignupwithemailverification"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            buildConfigField ("String", "SENDER_EMAIL", "\"${project.findProperty("SENDER_EMAIL".toString()) ?: "default@example.com"}\"")
            buildConfigField ("String", "EMAIL_PASS", "\"${project.findProperty("SENDER_EMAIL_PASSWORD".toString()) ?: "defaultPassword"}\"")
    }
        debug {
            buildConfigField ("String", "SENDER_EMAIL", "\"${project.findProperty("SENDER_EMAIL".toString()) ?: "default@example.com"}\"")
            buildConfigField ("String", "EMAIL_PASS", "\"${project.findProperty("SENDER_EMAIL_PASSWORD".toString()) ?: "defaultPassword"}\"")
        }
    }
    buildFeatures {
        buildConfig = true // Enable BuildConfig feature
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    packagingOptions{
        exclude("META-INF/NOTICE.md")
        exclude("META-INF/LICENSE.md")
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    implementation("com.sun.mail:android-mail:1.6.6")
    implementation("com.sun.mail:android-activation:1.6.7")
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}