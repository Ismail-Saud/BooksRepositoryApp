package com.example.booksrepositoryapp.ui.accountDetails

import com.example.booksrepositoryapp.domain.model.User

sealed class AccountDetailsState {
    object Idle : AccountDetailsState()
    object Loading : AccountDetailsState()
    data class Success(val user: User) : AccountDetailsState()
    data class Error(val message: String) : AccountDetailsState()
}
