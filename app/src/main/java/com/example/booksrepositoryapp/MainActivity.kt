package com.example.booksrepositoryapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.booksrepositoryapp.data.repository.UserRepository
import com.example.booksrepositoryapp.ui.book_category.BooksCategoryFragment
import com.example.booksrepositoryapp.ui.landingpage.LandingPageFragment
import com.example.booksrepositoryapp.ui.theme.BooksRepositoryAppTheme

class MainActivity : AppCompatActivity() {
    private lateinit var userRepository: UserRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userRepository = UserRepository(this)
        installSplashScreen()
        if (savedInstanceState == null) {
            if (userRepository.isLoggedIn()) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, BooksCategoryFragment())
                    .commit()
            }
            else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, LandingPageFragment())
                    .commit()
            }
        }
    }
}