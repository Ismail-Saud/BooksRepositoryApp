package com.example.booksrepositoryapp.ui.accountDetails

sealed class AccountDetailsEffect {
    object NavigateToLandingPage : AccountDetailsEffect()
    data class ShowToast(val message: String) : AccountDetailsEffect()
    object OpenAppSettings : AccountDetailsEffect()
}