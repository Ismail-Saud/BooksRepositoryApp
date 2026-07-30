package com.example.booksrepositoryapp.ui.account_details

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.local.room.entity.UserModel
import com.example.booksrepositoryapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AccountDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepo = UserRepository.getInstance(application)
    private val _userState = MutableStateFlow<AccountDetailsState>(AccountDetailsState.Idle)
    val userState: StateFlow<AccountDetailsState> = _userState
    private val _user = MutableStateFlow<UserModel?>(null)
    val user: StateFlow<UserModel?> = _user
    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> = _imageUri

    fun getUser() {
        val id = userRepo.getSavedUser()?.toInt() ?: 1
        Log.d("PROFILE", "Loading user id = $id")
        _userState.value = AccountDetailsState.Loading
        viewModelScope.launch {
            userRepo.getUserDetails(id).collect { user ->
                _user.value = user
                _userState.value = AccountDetailsState.Success(user)
            }
        }
    }

    fun logout() {
        userRepo.setLoggedIn(false)
    }

    fun setImageUri(uri: Uri) {
        _imageUri.value = uri
    }

    fun saveUserProfilePicture(uri: Uri) {
        val id = userRepo.getSavedUser()?.toInt() ?: return
        Log.d("PROFILE", "Saving picture for user id = $id, uri = $uri")
        viewModelScope.launch {
            userRepo.saveUserProfilePicture(id, uri)
        }
    }

    fun removeUserProfilePicture() {
        val id = userRepo.getSavedUser()?.toInt() ?: return
        Log.d("PROFILE", "Removing picture for user id = $id")
        viewModelScope.launch {
            userRepo.removeUserProfilePicture(id)
        }
    }
}