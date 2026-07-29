package com.example.booksrepositoryapp.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.booksrepositoryapp.data.local.prefManager.GsonManager
import com.example.booksrepositoryapp.data.local.room.DatabaseInstance
import com.example.booksrepositoryapp.data.local.room.entity.UserModel
import com.example.booksrepositoryapp.data.local.sharedPref.PrefManager
import kotlinx.coroutines.flow.Flow

class UserRepository(private val context: Context) {
    private val dao = DatabaseInstance.getDatabase(context).UserDao()
    private val isLoggedInKey = "isLoggedIn"
    private val saveUserId = "User_Id"

    suspend fun doesEmailExist (email: String): Boolean {
        return dao.doesEmailExists(email)
    }

    suspend fun loginUser(email: String, password: String): UserModel? {
        return dao.loginUser(email, password)
    }

    suspend fun signupUser (user: UserModel): Long {
        return dao.insert(user)
    }

    fun getUserDetails (id: Int): Flow<UserModel?> {
        return dao.getUserDetails(id)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        PrefManager.saveJson(context, isLoggedInKey, GsonManager.toJson(isLoggedIn))
        if (!isLoggedIn) {
            PrefManager.remove(context, saveUserId)
        }
    }

    fun isLoggedIn(): Boolean {
        return GsonManager.fromJson(PrefManager.getJson(context, isLoggedInKey), Boolean::class.java) ?: false
    }

    fun setUserSaved(userId: Int) {
        PrefManager.saveJson(context, saveUserId, GsonManager.toJson(userId))
    }

    fun getSavedUser() : String? {
        return PrefManager.getJson(context, saveUserId)
    }

    suspend fun updateAddress(id: Int, address: String) {
        dao.updateAddress(id, address)
    }

    suspend fun getAddress(id: Int) : String? {
        return dao.getAddress(id)
    }

    suspend fun saveUserProfilePicture(id: Int, uri: Uri) {
        val rows = dao.updateProfilePicture(id, uri.toString())
        Log.d("PROFILE", "Rows updated = $rows")
    }

    suspend fun removeUserProfilePicture(id: Int) {
        dao.removeProfilePicture(id)
    }
}