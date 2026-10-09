plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.compose)
}

kotlin {

    android {
        namespace = "com.example.booksrepositoryapp.shared"

        compileSdk {
            version = release(37)
        }

        minSdk = 24

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    val xcfName = "sharedKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    sourceSets {

        commonMain {
            dependencies {

                // Lifecycle
                api(libs.androidx.lifecycle.viewmodel)
                api(libs.androidx.lifecycle.runtime)

                // Kotlin
                api(libs.kotlin.stdlib)
                api(libs.kotlinx.coroutines.core)


                // Ktor
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.logging)

                // Coil
                implementation(libs.coil.compose)
                implementation(libs.coil.network.ktor3)

                // Koin
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)

                // Compose
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material3)
                api(compose.ui)
                api(compose.components.resources)
                api(compose.materialIconsExtended)

                // Compose Multiplatform Navigation
                implementation(libs.navigation.compose)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.ktor.client.mock)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        androidMain {
            dependencies {
                // Ktor Android engine
                implementation(libs.ktor.client.okhttp)

                // Koin Android
                implementation(libs.koin.android)

                // Firebase Authentication
                implementation(libs.firebase.auth)

                // Firebase Firestore
                implementation(libs.firebase.firestore)

                // Enables kotlinx.coroutines.tasks.await() for Firebase Task APIs.
                implementation(libs.kotlinx.coroutines.play.services)

                // Room
                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.room.ktx)

                // Android Location APIs
                implementation(libs.play.services.location)

                implementation(libs.androidx.core.splashscreen)

                implementation(libs.firebase.config)
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.junit)
                implementation(libs.androidx.test.core)
                implementation(libs.androidx.espresso.core)
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
//
//        iosMain {
//            dependencies {
//                // Ktor iOS engine
//                implementation(libs.ktor.client.darwin)
//            }
//        }
    }
}

dependencies {
    // The KMP source-set dependency DSL does not expose platform(). Declare the
    // Firebase BoM on Android's generated implementation configuration instead.
    add("androidMainImplementation", platform(libs.firebase.bom))

    // Room entities and DAOs are in androidMain, so only Android requires
    // Room's code generator.
    add("kspAndroid", libs.androidx.room.compiler)
}
