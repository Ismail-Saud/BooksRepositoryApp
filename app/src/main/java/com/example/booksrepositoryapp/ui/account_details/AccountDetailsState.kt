package com.example.booksrepositoryapp.ui.account_details

import com.example.booksrepositoryapp.data.firebase.authentication.UserProfile

sealed class AccountDetailsState {
    object Idle : AccountDetailsState()
    object Loading : AccountDetailsState()
    data class Success(val user: UserProfile) : AccountDetailsState()
    data class Error(val message: String) : AccountDetailsState()
}