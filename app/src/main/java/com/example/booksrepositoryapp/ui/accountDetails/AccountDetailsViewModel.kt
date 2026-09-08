package com.example.booksrepositoryapp.ui.accountDetails

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.repository.AddressRepositoryImpl
import com.example.booksrepositoryapp.data.repository.UserRepositoryImpl
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.domain.model.Address
import com.example.booksrepositoryapp.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class AccountDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val appContext = application.applicationContext
    
    // Injected manually for now, should be Hilt later
    private val userRepo = UserRepositoryImpl(application)
    private val authRepo = AuthRepository()
    private val addressRepo = AddressRepositoryImpl()

    val id = authRepo.getCurrentUserId() ?: ""

    private val _userState = MutableStateFlow<AccountDetailsState>(AccountDetailsState.Idle)
    val userState: StateFlow<AccountDetailsState> = _userState
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun getUser() {
        val uid = authRepo.getCurrentUserId()
        if (uid == null) {
            _userState.value = AccountDetailsState.Error("User is not logged in")
            return
        }
        viewModelScope.launch {
            _userState.value = AccountDetailsState.Loading
            try {
                val user = userRepo.getUserProfile(uid)
                if (user != null) {
                    _user.value = user
                    _userState.value = AccountDetailsState.Success(user)
                } else {
                    _userState.value = AccountDetailsState.Error("User profile not found")
                }
            } catch (e: Exception) {
                _userState.value = AccountDetailsState.Error(e.message ?: "Failed to load profile")
            }
        }
    }

    val selectedAddress: Flow<Address?> = addressRepo.getSelectedAddress(id)

    fun logout() {
        userRepo.setLoggedIn(false)
        authRepo.logout()
    }

    fun saveUserProfilePicture(uri: Uri) {
        val uid = authRepo.getCurrentUserId()
        if (uid == null) {
            Log.e("ProfilePicture", "UID is null")
            return
        }
        viewModelScope.launch {
            try {
                Log.d("ProfilePicture", "Saving image locally")

                _user.value?.profilePicture?.let { oldFileName ->
                    deleteLocalProfilePicture(oldFileName)
                }

                val fileName = "profile_${uid}_${System.currentTimeMillis()}.jpg"
                val destFile = File(appContext.filesDir, fileName)

                appContext.contentResolver.openInputStream(uri)?.use { input ->
                    destFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                } ?: throw IOException("Could not open URI")

                Log.d("ProfilePicture", "Local file saved: $fileName")

                userRepo.updateProfilePicture(uid = uid, profilePicture = fileName)
                
                Log.d("ProfilePicture", "Firestore update successful with local filename")
                getUser()
            } catch (e: Exception) {
                Log.e("ProfilePicture", "Local profile picture operation failed", e)
                _userState.value = AccountDetailsState.Error(e.message ?: "Failed to save profile picture")
            }
        }
    }

    fun removeUserProfilePicture() {
        val uid = authRepo.getCurrentUserId() ?: return
        viewModelScope.launch {
            try {
                _user.value?.profilePicture?.let { fileName ->
                    deleteLocalProfilePicture(fileName)
                }
                userRepo.updateProfilePicture(uid = uid, profilePicture = null)
                getUser()
            } catch (e: Exception) {
                _userState.value = AccountDetailsState.Error(e.message ?: "Failed to remove profile picture")
            }
        }
    }

    private fun deleteLocalProfilePicture(fileName: String) {
        try {
            val file = File(appContext.filesDir, fileName)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            Log.e("ProfilePicture", "Failed to delete local file: $fileName", e)
        }
    }
}
