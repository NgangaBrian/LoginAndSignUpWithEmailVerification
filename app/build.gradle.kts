
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.loginandsignupwithemailverification"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.loginandsignupwithemailverification"
        minSdk = 23
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
            buildConfigField("String", "API_KEY", "\"${project.findProperty("API_KEY") ?: "defaultApiKey"}\"")
    }
        debug {
            buildConfigField ("String", "SENDER_EMAIL", "\"${project.findProperty("SENDER_EMAIL".toString()) ?: "default@example.com"}\"")
            buildConfigField ("String", "EMAIL_PASS", "\"${project.findProperty("SENDER_EMAIL_PASSWORD".toString()) ?: "defaultPassword"}\"")
            buildConfigField("String", "API_KEY", "\"${project.findProperty("API_KEY") ?: "defaultApiKey"}\"")
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

tasks.register<Copy>("generateFirebaseConfig") {
    val apiKey = project.findProperty("API_KEY") as String

    from("src/main/assets/firebase_config_template.json") {
        filter{line ->
            line.replace("__API_KEY__", apiKey)
        }
    }
    into("src/main/assets/")
    rename("firebase_config_template.json", "firebase_config.json")
}

tasks.named("preBuild").configure {
    dependsOn("generateFirebaseConfig")
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    implementation("com.sun.mail:android-mail:1.6.6")
    implementation("com.sun.mail:android-activation:1.6.7")
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database:20.0.5")
    implementation("com.google.firebase:firebase-appcheck:16.0.0") // Add this line
    implementation("com.android.volley:volley:1.2.1")
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
