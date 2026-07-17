package com.example.booksrepositoryapp.data.repository

import android.content.Context
import com.example.booksrepositoryapp.data.local.prefManager.GsonManager
import com.example.booksrepositoryapp.data.local.room.DatabaseInstance
import com.example.booksrepositoryapp.data.local.room.entity.UserModel
import com.example.booksrepositoryapp.data.local.sharedPref.PrefManager
import kotlinx.coroutines.flow.Flow

class UserRepository(private val context: Context) {
    private val dao = DatabaseInstance.getDatabase(context).UserDao()
    private val isLoggedInKey = "isLoggedIn"
    private val isUserSaved = "isUserSaved"

    suspend fun doesEmailExist (email: String): Boolean {
        return dao.doesEmailExists(email)
    }

    suspend fun loginUser(email: String, password: String): UserModel? {
        return dao.loginUser(email, password)
    }

    suspend fun signupUser (user: UserModel) {
        dao.insert(user)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        PrefManager.saveJson(context, isLoggedInKey, GsonManager.toJson(isLoggedIn))
    }

    fun isLoggedIn(): Boolean {
        return GsonManager.fromJson(PrefManager.getJson(context, isLoggedInKey), Boolean::class.java) ?: false
    }

    fun setUserSaved(isLoggedIn: Boolean) {
        PrefManager.saveJson(context, isUserSaved, GsonManager.toJson(isLoggedIn))
    }

    fun isUserSaved(): Boolean {
        return GsonManager.fromJson(PrefManager.getJson(context, isUserSaved), Boolean::class.java) ?: false
    }
}