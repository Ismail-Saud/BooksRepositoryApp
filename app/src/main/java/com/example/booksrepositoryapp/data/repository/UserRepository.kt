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
    private val saveUserId = "User_Id"
    private fun prefs() = context.getSharedPreferences(saveUserId, Context.MODE_PRIVATE)

    suspend fun doesEmailExist (email: String): Boolean {
        return dao.doesEmailExists(email)
    }

    suspend fun loginUser(email: String, password: String): UserModel? {
        return dao.loginUser(email, password)
    }

    suspend fun signupUser (user: UserModel) {
        dao.insert(user)
    }

    fun getUserDetails (id: Int): Flow<UserModel?> {
        return dao.getUserDetails(id)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        PrefManager.saveJson(context, isLoggedInKey, GsonManager.toJson(isLoggedIn))
    }

    fun isLoggedIn(): Boolean {
        return GsonManager.fromJson(PrefManager.getJson(context, isLoggedInKey), Boolean::class.java) ?: false
    }

    fun setUserSaved(userId: Int) {
        PrefManager.saveJson(context, saveUserId, GsonManager.toJson(userId))
    }

    fun getSavedUser() : String? {
        return prefs().getString(saveUserId, null)
    }

    suspend fun updateAddress(id: Int, address: String) {
        dao.updateAddress(id, address)
    }
}