package com.example.booksrepositoryapp.ui.main

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.booksrepositoryapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
            true
        setContent {
            val navControllerCompose = rememberNavController()
            AppNavigation(
                navController = navControllerCompose,
                modifier = Modifier.safeDrawingPadding()
            )
        }
    }

    fun selectBottomNavItem(itemId:Int){
        binding.bottomNav.selectedItemId = itemId
    }
}