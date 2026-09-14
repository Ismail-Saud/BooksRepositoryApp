package com.example.booksrepositoryapp.data.repository

import android.content.Context
import android.net.Uri
import com.example.booksrepositoryapp.data.mapper.toDomain
import com.example.booksrepositoryapp.data.mapper.toProfile
import com.example.booksrepositoryapp.data.source.local.prefManager.GsonManager
import com.example.booksrepositoryapp.data.source.local.room.DatabaseInstance
import com.example.booksrepositoryapp.data.source.local.room.entity.UserModel
import com.example.booksrepositoryapp.data.source.local.sharedPref.PrefManager
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.UserProfile
import com.example.booksrepositoryapp.domain.model.User
import com.example.booksrepositoryapp.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(context: Context) : UserRepository {
    private val appContext = context.applicationContext
    private val dao = DatabaseInstance.getDatabase(appContext).UserDao()
    private val firestore = FirebaseFirestore.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val isLoggedInKey = "isLoggedIn"
    private val saveUserId = "User_Id"

    private val _loginState = MutableStateFlow(isLoggedIn())
    override val loginState: StateFlow<Boolean> = _loginState

    override suspend fun doesEmailExist(email: String): Boolean {
        return dao.doesEmailExists(email)
    }

    override suspend fun loginUser(email: String, password: String): User? {
        return dao.loginUser(email, password)?.toDomain()
    }

    override suspend fun signupUser(username: String, email: String, password: String): Long {
        val userEntity = UserModel(username = username, email = email, password = password)
        return dao.insert(userEntity)
    }

    override fun getUserDetails(id: Int): Flow<User?> {
        return dao.getUserDetails(id).map { it?.toDomain() }
    }

    override fun setLoggedIn(isLoggedIn: Boolean) {
        PrefManager.saveBoolean(appContext, isLoggedInKey, isLoggedIn)
        if (!isLoggedIn) {
            PrefManager.remove(appContext, saveUserId)
        }
        _loginState.value = isLoggedIn
    }

    override fun isLoggedIn(): Boolean {
        return PrefManager.getBoolean(appContext, isLoggedInKey, false)
    }

    override fun setUserSaved(userId: Int) {
        PrefManager.saveJson(appContext, saveUserId, GsonManager.toJson(userId))
    }

    override fun getSavedUser(): String? {
        return PrefManager.getJson(appContext, saveUserId)
    }

    override suspend fun saveUserProfilePicture(id: Int, uri: Uri) {
        dao.updateProfilePicture(id, uri.toString())
    }

    override suspend fun removeUserProfilePicture(id: Int) {
        dao.removeProfilePicture(id)
    }

    override suspend fun createUserProfile(user: User) {
        db.collection("users").document(user.id).set(user.toProfile()).await()
    }

    override suspend fun getUserProfile(uid: String): User? {
        return firestore
            .collection("users")
            .document(uid)
            .get()
            .await()
            .toObject(UserProfile::class.java)?.toDomain()
    }

    override suspend fun updateProfilePicture(uid: String, profilePicture: String?) {
        firestore
            .collection("users")
            .document(uid)
            .update("profilePicture", profilePicture)
            .await()
    }
}
