package com.example.booksrepositoryapp.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.booksrepositoryapp.data.local.prefManager.GsonManager
import com.example.booksrepositoryapp.data.local.room.DatabaseInstance
import com.example.booksrepositoryapp.data.local.room.entity.UserModel
import com.example.booksrepositoryapp.data.local.sharedPref.PrefManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserRepository private constructor(context: Context) {
    private val appContext = context.applicationContext
    private val dao = DatabaseInstance.getDatabase(appContext).UserDao()
    private val isLoggedInKey = "isLoggedIn"
    private val saveUserId = "User_Id"

    private val _loginState = MutableStateFlow(isLoggedIn())
    val loginState: StateFlow<Boolean> = _loginState

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null
        fun getInstance(context: Context): UserRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserRepository(context).also {
                    INSTANCE = it
                }
            }
        }
    }

    suspend fun doesEmailExist(email: String): Boolean {
        return dao.doesEmailExists(email)
    }

    suspend fun loginUser(email: String, password: String): UserModel? {
        return dao.loginUser(email, password)
    }

    suspend fun signupUser(user: UserModel): Long {
        return dao.insert(user)
    }

    fun getUserDetails(id: Int): Flow<UserModel?> {
        return dao.getUserDetails(id)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        PrefManager.saveBoolean(appContext, isLoggedInKey, isLoggedIn)
        if (!isLoggedIn) {
            PrefManager.remove(appContext, saveUserId)
        }
        _loginState.value = isLoggedIn
    }

    fun isLoggedIn(): Boolean {
        return PrefManager.getBoolean(
            appContext,
            isLoggedInKey,
            false
        )
    }

    fun setUserSaved(userId: Int) {
        PrefManager.saveJson(appContext, saveUserId, GsonManager.toJson(userId))
    }

    fun getSavedUser(): String? {
        return PrefManager.getJson(appContext, saveUserId)
    }

//    suspend fun updateAddress(id: Int, address: String) {
//        dao.updateAddress(id, address)
//    }
//
//    suspend fun getAddress(id: Int): List<String>? {
//        return dao.getAddress(id)
//    }

    suspend fun saveUserProfilePicture(id: Int, uri: Uri) {
        dao.updateProfilePicture(id, uri.toString())
    }

    suspend fun removeUserProfilePicture(id: Int) {
        dao.removeProfilePicture(id)
    }
}