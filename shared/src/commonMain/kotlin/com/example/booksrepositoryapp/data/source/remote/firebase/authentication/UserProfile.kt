package com.example.booksrepositoryapp.data.source.remote.firebase.authentication

data class UserProfile (
    var uid: String = "",
    var username: String = "",
    var email: String = "",
    var profilePicture: String? = null
)