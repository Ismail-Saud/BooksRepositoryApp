package com.example.booksrepositoryapp.ui.account_details

import com.example.booksrepositoryapp.data.local.room.entity.UserModel

sealed class AccountDetailsState {
    object Idle : AccountDetailsState()
    object Loading : AccountDetailsState()
    data class Success(val user: UserModel?) : AccountDetailsState()
    data class Error(val message: String) : AccountDetailsState()
}