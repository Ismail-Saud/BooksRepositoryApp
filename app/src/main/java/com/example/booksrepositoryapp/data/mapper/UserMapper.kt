package com.example.booksrepositoryapp.data.mapper

import com.example.booksrepositoryapp.data.source.local.room.entity.UserModel
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.UserProfile
import com.example.booksrepositoryapp.domain.model.User

fun UserModel.toDomain(): User {
    return User(
        id = id.toString(),
        username = username,
        email = email,
        profilePicture = profilePicture
    )
}

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
