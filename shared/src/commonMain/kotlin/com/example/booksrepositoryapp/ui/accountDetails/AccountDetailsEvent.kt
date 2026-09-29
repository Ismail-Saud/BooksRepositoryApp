package com.example.booksrepositoryapp.ui.accountDetails

sealed class AccountDetailsEvent {
    object LoadUser : AccountDetailsEvent()
    object LogoutClicked : AccountDetailsEvent()
    data class ProfilePictureSelected(val imageUri: String) : AccountDetailsEvent()
    object RemoveProfilePictureClicked : AccountDetailsEvent()
    object CameraPermissionDeniedPermanent : AccountDetailsEvent()
}