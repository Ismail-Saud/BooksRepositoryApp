package com.example.booksrepositoryapp.data.mapper

import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.UserProfile
import com.example.booksrepositoryapp.domain.model.User

fun UserProfile.toDomain(): User {
    return User(
        id = uid,
        username = username,
        email = email,
        profilePicture = profilePicture
    )
}

fun User.toProfile(): UserProfile {
    return UserProfile(
        uid = id,
        username = username,
        email = email,
        profilePicture = profilePicture
    )
}
