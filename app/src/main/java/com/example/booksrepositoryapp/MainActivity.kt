package com.example.booksrepositoryapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.example.booksrepositoryapp.data.repository.UserRepository
import com.example.booksrepositoryapp.ui.account_details.AccountDetailsFragment
import com.example.booksrepositoryapp.ui.book_category.BooksCategoryFragment
import com.example.booksrepositoryapp.ui.cart_screen.AddToCartFragment
import com.example.booksrepositoryapp.ui.landingpage.LandingPageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var userRepository: UserRepository
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)
        userRepository = UserRepository(this)
        bottomNav = findViewById(R.id.bottomNav)
        if (savedInstanceState == null) {
            if (userRepository.isLoggedIn()) {
                openFragment(BooksCategoryFragment())
            } else {
                openFragment(LandingPageFragment())
            }
        }
        updateBottomNav()
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    openFragment(BooksCategoryFragment())
                    true
                }
                R.id.nav_cart -> {
                    openFragment(AddToCartFragment())
                    true
                }
                R.id.nav_account -> {
                    openFragment(AccountDetailsFragment())
                    true
                }
                else -> false
            }
        }
    }
    fun updateBottomNav() {
        bottomNav.visibility =
            if (userRepository.isLoggedIn()) View.VISIBLE else View.GONE
    }
    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        bottomNav.visibility = if (userRepository.isLoggedIn()) View.VISIBLE else View.GONE
    }
}