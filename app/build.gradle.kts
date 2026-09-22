plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    id("org.jlleitschuh.gradle.ktlint")
}

android {
    namespace = "com.example.booksrepositoryapp"
    compileSdk = 37

    lint {
        abortOnError = true
        warningsAsErrors = false
    }
    defaultConfig {
        applicationId = "com.example.booksrepositoryapp"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
        compose = true
        buildConfig = true
    }
}

ktlint {
    version.set("1.6.0")
    android.set(true)
    outputToConsole.set(true)
    ignoreFailures.set(false)
}

dependencies {
    // Compose BOM
    // Compose dependencies.
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))

    // Jetpack Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)

    // Extended Material Icons
    implementation(libs.androidx.compose.material.icons.extended)

    // Compose Testing
    // Used for instrumented UI tests with Compose.
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // Espresso UI testing
    androidTestImplementation(libs.androidx.espresso.core)

    // AndroidX JUnit integration
    androidTestImplementation(libs.androidx.junit)

    // Compose test manifest
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Compose Preview / Layout Inspector tooling
    debugImplementation(libs.androidx.compose.ui.tooling)

    // AndroidX Core
    implementation(libs.androidx.core.ktx)

    // Splash screen API
    implementation(libs.androidx.core.splashscreen)

    // AndroidX Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // AndroidX Navigation
    // Compose Navigation
    implementation(libs.androidx.navigation.compose)

    // Shared Navigation APIs
    implementation(libs.androidx.navigation.common.ktx)

    // Navigation UI helpers
    implementation(libs.androidx.navigation.ui.ktx)

    // Camera
    implementation(libs.androidx.camera.camera2.pipe)

    // Google Credentials / Sign-In
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    // Firebase BOM
    // Controls compatible versions of Firebase libraries.
    implementation(platform(libs.firebase.bom))

    // Firebase Authentication
    implementation(libs.firebase.auth)

    // Firebase Firestore
    implementation(libs.firebase.firestore)

    // Firebase Storage
    implementation(libs.firebase.storage)

    // Firebase Analytics
    implementation(libs.firebase.analytics)

    // Firebase Crashlytics
    implementation(libs.firebase.crashlytics)

    // Firebase Performance Monitoring
    implementation(libs.firebase.perf)

    // Firebase Remote Config
    implementation(libs.firebase.config)

    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Retrofit / Networking
    implementation(libs.retrofit)

    // Gson converter for Retrofit
    implementation(libs.converter.gson)

    // JSON
    implementation(libs.gson)

    // Image Loading
    implementation(libs.glide)

    // Glide Compose integration
    implementation(libs.compose)

    // Coil Compose
    implementation(libs.coil.compose)

    // Coil OkHttp network integration
    implementation(libs.coil.network.okhttp)


    // Keep this if you still use Material XML/View-based components.
    implementation(libs.material)

    // Shimmer
    implementation(libs.shimmer)

    // Google Play Services
    // Location
    implementation(libs.play.services.location)

    // Google Maps
    implementation(libs.play.services.maps)

    // Google Mobile Ads
//    implementation(libs.play.services.ads)

    // Hilt Dependency Injection
    implementation(libs.hilt.android)

    // Hilt annotation processor
    ksp(libs.hilt.compiler)

    // Hilt + Jetpack Compose Navigation integration
    implementation(libs.androidx.hilt.navigation.compose)

    // Unit Testing
    testImplementation(libs.junit)

    // Koin
    implementation(platform(libs.koin.bom))

    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)

    // Ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
}