package com.example.booksrepositoryapp.ui.utils

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.Fragment
import com.example.booksrepositoryapp.R

class NavigationUtil (private val fragmentManager: FragmentManager){
    fun navigateTo (fragment: Fragment) {
        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
    fun navigateAsRoot(fragment: Fragment) {
        fragmentManager.popBackStack(
            null,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
    fun replace(fragment: Fragment) {
        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
    fun goBack() {
        fragmentManager.popBackStack()
    }
}