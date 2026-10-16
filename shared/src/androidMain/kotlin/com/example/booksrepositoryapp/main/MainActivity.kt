package com.example.booksrepositoryapp.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.example.booksrepositoryapp.localization.AppEnvironment
import com.example.booksrepositoryapp.localization.LanguageManager
import com.example.booksrepositoryapp.localization.LanguagePreferences
import com.example.booksrepositoryapp.localization.changeAppLanguage
import com.example.booksrepositoryapp.localization.createLanguageDataStore
import com.example.booksrepositoryapp.navigation.AppNavigation
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

class MainActivity : ComponentActivity() {
    private val languageDataStore by lazy {
        createLanguageDataStore(applicationContext)
    }
    private val languagePreferences by lazy {
        LanguagePreferences(languageDataStore)
    }

    private val languageManager by lazy {
        LanguageManager(languagePreferences)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        val defaults = mapOf<String, Any>(
            "shipping_fee" to 10.0,
            "max_addresses" to 5L,
            "checkout_enabled" to true,
            "is_maintenance_mode" to false,
        )
        remoteConfig.setDefaultsAsync(defaults)

        setContent {
            AppEnvironment {
                var isLanguageLoaded by remember { mutableStateOf(false) }
                var isRemoteConfigLoaded by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    val savedLanguage = languageManager.loadSavedLanguage()

                    if (savedLanguage != null) {
                        changeAppLanguage(savedLanguage.code)
                    }

                    isLanguageLoaded = true
                }

                LaunchedEffect(Unit) {
                    remoteConfig.fetchAndActivate()
                        .addOnCompleteListener {
                            isRemoteConfigLoaded = true
                        }
                }

                if (isLanguageLoaded && isRemoteConfigLoaded) {
                    val isLoggedIn = FirebaseAuth.getInstance().currentUser != null

                    AppNavigation(
                        isLoggedIn = isLoggedIn,
                        shippingFee = remoteConfig.getDouble("shipping_fee"),
                        maxAddresses = remoteConfig.getLong("max_addresses"),
                        isCheckoutEnabled = remoteConfig.getBoolean("checkout_enabled"),
                        isMaintenanceMode = remoteConfig.getBoolean("is_maintenance_mode"),
                        onLanguageSelected = { language ->
                            changeAppLanguage(language.code)
                            languageManager.saveLanguage(language)
                        },
                        modifier = Modifier.safeDrawingPadding(),
                    )
                }
            }
        }
    }
}