package com.example.booksrepositoryapp.ui.accountDetails

import android.net.Uri

sealed class AccountDetailsEvent {
    object LoadUser : AccountDetailsEvent()
    object LogoutClicked : AccountDetailsEvent()
    data class ProfilePictureSelected(val uri: Uri) : AccountDetailsEvent()
    object RemoveProfilePictureClicked : AccountDetailsEvent()
    object CameraPermissionDeniedPermanent : AccountDetailsEvent()
}