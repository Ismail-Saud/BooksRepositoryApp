package com.example.booksrepositoryapp.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.example.booksrepositoryapp.navigation.AppNavigation
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.remoteConfig

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        setContent {
            val remoteConfig = Firebase.remoteConfig
            val isLoggedIn = FirebaseAuth.getInstance().currentUser != null

            AppNavigation(
                isLoggedIn = isLoggedIn,
                shippingFee = remoteConfig.getDouble("shipping_fee"),
                maxAddresses = remoteConfig.getLong("max_addresses"),
                isCheckoutEnabled = remoteConfig.getBoolean("checkout_enabled"),
                isMaintenanceMode = remoteConfig.getBoolean("is_maintenance_mode"),
                modifier = Modifier.safeDrawingPadding(),
            )
        }
    }
}