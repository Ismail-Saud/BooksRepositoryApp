package com.example.booksrepositoryapp.domain.repository

import android.net.Uri
import com.example.booksrepositoryapp.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val loginState: StateFlow<Boolean>
    
    suspend fun doesEmailExist(email: String): Boolean
    suspend fun loginUser(email: String, password: String): User?
    suspend fun signupUser(username: String, email: String, password: String): Long
    fun getUserDetails(id: Int): Flow<User?>
    
    fun setLoggedIn(isLoggedIn: Boolean)
    fun isLoggedIn(): Boolean
    fun setUserSaved(userId: Int)
    fun getSavedUser(): String?
    
    suspend fun saveUserProfilePicture(id: Int, uri: Uri)
    suspend fun removeUserProfilePicture(id: Int)
    
    suspend fun createUserProfile(user: User)
    suspend fun getUserProfile(uid: String): User?
    suspend fun updateProfilePicture(uid: String, profilePicture: String?)
}
