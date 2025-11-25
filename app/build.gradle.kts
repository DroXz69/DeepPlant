plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.example.appretrofit"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.appretrofit"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
        sourceSets.all {
            languageSettings.optIn("kotlinx.serialization.ExperimentalSerializationApi")
        }
    }
}

dependencies {
    // -------------------------
    // ANDROIDX / COMPOSE
    // -------------------------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
    implementation("androidx.activity:activity-ktx:1.11.0")

    // -------------------------
    // TESTING
    // -------------------------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // -------------------------
    // RETROFIT (CORREGIDO)
    // -------------------------
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // -------------------------
    // KTOR CLIENT
    // -------------------------
    implementation("io.ktor:ktor-client-core:3.3.2")
    implementation("io.ktor:ktor-client-okhttp:3.3.2")
    implementation("io.ktor:ktor-client-content-negotiation:3.3.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.3.2")
    implementation("io.ktor:ktor-client-logging:3.3.2")

    // -------------------------
    // COROUTINES
    // -------------------------
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    // -------------------------
    // KOTLIN SERIALIZATION
    // -------------------------
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    // -------------------------
    // COIL (PARA rememberAsyncImagePainter)
    // -------------------------
    implementation("io.coil-kt:coil-compose:2.6.0")

    // -------------------------
    // FIREBASE (OPTIMIZADO)
    // -------------------------
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))

    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-common-ktx")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")

    implementation("androidx.compose.material:material-icons-extended")

    implementation("androidx.core:core-splashscreen:1.2.0")

}